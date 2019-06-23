package com.future.function.repository.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class UserRepositoryTest {
  
  private static final String EMAIL_1 = "email-1";
  
  private static final String EMAIL_2 = "email-2";
  
  private static final String BATCH_CODE = "batch-code";
  
  private static final String BATCH_ID = "batch-id";
  
  private static final Batch BATCH = Batch.builder()
    .id(BATCH_ID)
    .code(BATCH_CODE)
    .build();
  
  @Autowired
  private UserRepository userRepository;
  
  private User user1;
  
  private User user2;
  
  @Before
  public void setUp() {
    
    user1 = User.builder()
      .role(Role.ADMIN)
      .email(EMAIL_1)
      .build();
    user2 = User.builder()
      .role(Role.STUDENT)
      .email(EMAIL_2)
      .batch(BATCH)
      .build();
    
    userRepository.save(user1);
    userRepository.save(user2);
  }
  
  @After
  public void tearDown() {
    
    userRepository.deleteAll();
  }
  
  @Test
  public void testGivenUserEmailByFindingUserByEmailReturnUserData() {
    
    Optional<User> foundUser1 = userRepository.findByEmailAndDeletedFalse(EMAIL_1);
    
    assertThat(foundUser1).isNotEqualTo(Optional.empty());
    assertThat(foundUser1.get()
                 .getRole()
                 .name()).isEqualTo(user1.getRole()
                                      .name());
    
    Optional<User> foundUser2 = userRepository.findByEmailAndDeletedFalse(EMAIL_2);
    
    assertThat(foundUser2).isNotEqualTo(Optional.empty());
    assertThat(foundUser2.get()
                 .getRole()
                 .name()).isEqualTo(user2.getRole()
                                      .name());
  }
  
  @Test
  public void testGivenRoleAndPageableByFindingUsersByRoleAndPageableReturnPageOfUsers() {
    
    Page<User> foundUsersPage1 = userRepository.findAllByRoleAndDeletedFalse(
      Role.ADMIN, new PageRequest(0, 5));
    
    assertThat(foundUsersPage1).isNotNull();
    assertThat(foundUsersPage1.getContent()).isNotEmpty();
    assertThat(foundUsersPage1.getNumberOfElements()).isEqualTo(1);
    
    Page<User> foundUsersPage2 = userRepository.findAllByRoleAndDeletedFalse(
      Role.JUDGE, new PageRequest(0, 5));
    
    assertThat(foundUsersPage2).isNotNull();
    assertThat(foundUsersPage2.getContent()).isEmpty();
    assertThat(foundUsersPage2.getNumberOfElements()).isEqualTo(0);
  }
  
  @Test
  public void testGivenRoleAndBatchByFindingUsersByRoleAndBatchReturnListOfUsers() {
    
    List<User> foundUsers = userRepository.findAllByRoleAndBatchAndDeletedFalse(Role.STUDENT,
                                                                                BATCH);
    
    assertThat(foundUsers).isNotEmpty();
    assertThat(foundUsers.size()).isEqualTo(1);
  }
  
}
