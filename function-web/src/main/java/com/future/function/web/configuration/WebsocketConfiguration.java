package com.future.function.web.configuration;

import com.future.function.common.exception.UnauthorizedException;
import com.future.function.common.properties.core.SessionProperties;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebsocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

  private final SessionProperties sessionProperties;

  private final AuthService authService;

  @Value("${function.stomp-endpoint}")
  private String endpoint;

  @Autowired
  public WebsocketConfiguration(AuthService authService, SessionProperties sessionProperties) {
    this.authService = authService;
    this.sessionProperties = sessionProperties;
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    super.configureClientInboundChannel(registration);
    registration.interceptors(httpSessionChannelInterceptorAdapter());
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
    stompEndpointRegistry
            .addEndpoint(endpoint)
            .addInterceptors(httpSessionHandshakeInterceptor())
            .setAllowedOrigins("*")
            .withSockJS();
  }

  @Bean
  public HandshakeInterceptor httpSessionHandshakeInterceptor() {
    return new HandshakeInterceptor() {

      @Override
      public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) serverHttpRequest;
        ServletServerHttpResponse servletServerHttpResponse = (ServletServerHttpResponse) serverHttpResponse;
        HttpServletRequest servletRequest = servletServerHttpRequest.getServletRequest();
        Cookie cookie = WebUtils.getCookie(servletRequest, sessionProperties.getCookieName());
        if (cookie == null) {
          return false;
        }
        User user = authService.getLoginStatus(cookie.getValue(), servletServerHttpResponse.getServletResponse());
        map.put(sessionProperties.getCookieName(), user.getId());
        return true;
      }

      @Override
      public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) { }
    };
  }

  @Bean
  public ChannelInterceptorAdapter httpSessionChannelInterceptorAdapter() {
    return new ChannelInterceptorAdapter() {

      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
          String destination = accessor.getDestination();
          String userId = accessor.getSessionAttributes().get(sessionProperties.getCookieName()).toString();
          log.debug("destination : " + destination);
          log.debug("userId : " + userId);
          authService.authorizeInboundChannel(destination, userId);
        }
        return message;
      }
    };
  }
}
