package com.future.function.web.controller.communication;

import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

  @Autowired
  UserService userService;

  @MessageMapping("/chatrooms/{userId}")
  public User handle(@DestinationVariable String userId, String message) {
    System.out.println(userId);
    System.out.println(message);
    return userService.getUser(userId);
  }

  @MessageMapping("/chatrooms")
  public String handle(String message) {
    System.out.println(message);
    return "HEHEHE";
  }

}
