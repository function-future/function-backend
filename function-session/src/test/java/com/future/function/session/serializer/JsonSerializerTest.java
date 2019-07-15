package com.future.function.session.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.core.Role;
import com.future.function.session.model.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonSerializerTest {
  
  private static final String ID = "id";
  
  private static final String USER_ID = "user-id";
  
  private static final String BATCH_ID = "batch-id";
  
  private static final String EMAIL = "email";
  
  private static final Role ROLE = Role.STUDENT;
  
  private static final Session SESSION = new Session(ID, USER_ID, BATCH_ID,
                                                     EMAIL, ROLE
  );
  
  private JacksonTester<Session> sessionJacksonTester;
  
  private byte[] sessionBytes;
  
  @Mock
  private ObjectMapper objectMapper;
  
  @InjectMocks
  private JsonSerializer jsonSerializer;
  
  @Before
  public void setUp() throws Exception {
    
    JacksonTester.initFields(this, new ObjectMapper());
    
    sessionBytes = sessionJacksonTester.write(SESSION)
      .getJson()
      .getBytes();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(objectMapper);
  }
  
  @Test
  public void testGivenSessionBySerializingSessionReturnByteOfSerializedSession()
    throws Exception {
    
    Session session = new Session(ID, USER_ID, BATCH_ID, EMAIL, ROLE);
    String sessionJson = sessionJacksonTester.write(session)
      .getJson();
    byte[] bytes = sessionJson.getBytes();
    when(objectMapper.writeValueAsBytes(SESSION)).thenReturn(bytes);
    
    assertThat(jsonSerializer.serialize(SESSION)).isEqualTo(sessionBytes);
    
    verify(objectMapper).writeValueAsBytes(SESSION);
  }
  
  @Test
  public void testGivenSessionThenCauseExceptionBySerializingSessionReturnSerializationException()
    throws Exception {
    
    when(objectMapper.writeValueAsBytes(SESSION)).thenThrow(
      new JsonProcessingException("") {});
    
    catchException(() -> jsonSerializer.serialize(SESSION));
    
    assertThat(caughtException().getClass()).isEqualTo(
      SerializationException.class);
    assertThat(caughtException().getMessage()).isNotBlank();
    
    verify(objectMapper).writeValueAsBytes(SESSION);
  }
  
  @Test
  public void testGivenNullByteOfSerializedSessionByDeserializingSessionReturnSession() {
    
    assertThat(jsonSerializer.deserialize(null)).isNull();
    
    verifyZeroInteractions(objectMapper);
  }
  
  @Test
  public void testGivenByteOfSerializedSessionByDeserializingSessionReturnSession()
    throws Exception {
    
    Session session = new Session(ID, USER_ID, BATCH_ID, EMAIL, ROLE);
    when(objectMapper.readValue(sessionBytes, Session.class)).thenReturn(
      session);
    
    assertThat(jsonSerializer.deserialize(sessionBytes)).isEqualTo(SESSION);
    
    verify(objectMapper).readValue(sessionBytes, Session.class);
  }
  
  @Test
  public void testGivenByteOfSerializedSessionThenCauseExceptionByDeserializingSessionReturnSerializationException()
    throws Exception {
    
    when(objectMapper.readValue(sessionBytes, Session.class)).thenThrow(
      new IOException(""));
    
    catchException(() -> jsonSerializer.deserialize(sessionBytes));
    
    assertThat(caughtException().getClass()).isEqualTo(
      SerializationException.class);
    assertThat(caughtException().getMessage()).isNotBlank();
    
    verify(objectMapper).readValue(sessionBytes, Session.class);
  }
  
}
