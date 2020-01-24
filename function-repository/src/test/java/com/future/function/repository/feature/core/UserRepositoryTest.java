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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class UserRepositoryTest {

  private static final String NAME_1 = "name-1";

  private static final String NAME_2 = "name-2";

  private static final String EMAIL_1 = "email-1";

  private static final String EMAIL_2 = "email-2";

  private static final String BATCH_CODE = "batch-code";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BatchRepository batchRepository;

  private Batch batch;

  private User user1;

  private User user2;

  @Before
  public void setUp() {

    batch = Batch.builder()
      .code(BATCH_CODE)
      .build();

    user1 = User.builder()
      .role(Role.ADMIN)
      .name(NAME_1)
      .email(EMAIL_1)
      .build();
    user1.setDeleted(false);

    user2 = User.builder()
      .role(Role.STUDENT)
      .name(NAME_2)
      .email(EMAIL_2)
      .batch(batchRepository.save(batch))
      .build();
    user2.setDeleted(false);

    userRepository.save(user1);
    userRepository.save(user2);
  }

  @After
  public void tearDown() {

    userRepository.deleteAll();
    batchRepository.deleteAll();
  }

  @Test
  public void testGivenUserEmailByFindingUserByEmailReturnUserData() {

    Optional<User> foundUser1 = userRepository.findByEmailAndDeletedFalse(
      EMAIL_1);

    assertThat(foundUser1).isNotEqualTo(Optional.empty());
    assertThat(foundUser1.get()
                 .getRole()
                 .name()).isEqualTo(user1.getRole()
                                      .name());

    Optional<User> foundUser2 = userRepository.findByEmailAndDeletedFalse(
      EMAIL_2);

    assertThat(foundUser2).isNotEqualTo(Optional.empty());
    assertThat(foundUser2.get()
                 .getRole()
                 .name()).isEqualTo(user2.getRole()
                                      .name());
  }

  @Test
  public void testGivenBatchAndRoleAndPageableByFindingUsersByRoleAndPageableReturnPageOfUsers() {

    Page<User> foundUsersPage =
      userRepository.findAllByBatchAndRoleAndDeletedFalse(batch, Role.STUDENT, new PageRequest(0, 5));

    assertThat(foundUsersPage).isNotNull();
    assertThat(foundUsersPage.getContent()).isNotEmpty();
    assertThat(foundUsersPage.getNumberOfElements()).isEqualTo(1);
  }

  @Test
  public void testGivenRoleAndBatchByFindingUsersByRoleAndBatchReturnListOfUsers() {

    List<User> foundUsers = userRepository.findAllByRoleAndBatchAndDeletedFalse(
      Role.STUDENT, batch);

    assertThat(foundUsers).isNotEmpty();
    assertThat(foundUsers.size()).isEqualTo(1);
  }

  @Test
  public void testGivenNameByFindingUsersByNameContainsIgnoreCaseReturnListOfUsers() {

    Page<User> foundUsers =
      userRepository.findAllByNameContainsIgnoreCaseAndDeletedFalse("E-1",
                                                                    new PageRequest(
                                                                      0, 10)
      );

    assertThat(foundUsers.getContent()).isNotEmpty();
    assertThat(foundUsers.getContent()).isEqualTo(
      Collections.singletonList(user1));
  }

  @Test
  public void testGivenRoleAndNameByFindingUsersByRoleAndNameContainsIgnoreCaseReturnPageOfUsers() {

    Page<User> foundUsers =
      userRepository.findAllByRoleAndNameContainsIgnoreCaseAndDeletedFalse(
        Role.ADMIN, "E-1", new PageRequest(0, 10));

    assertThat(foundUsers.getContent()).isNotEmpty();
    assertThat(foundUsers.getContent()).isEqualTo(
      Collections.singletonList(user1));
  }

  @Test
  public void testGivenBatchAndRoleAndNameByFindingUsersByBatchAndRoleAndNameContainsIgnoreCaseReturnPageOfUsers() {

    Page<User> foundUsers =
      userRepository.findAllByBatchAndRoleAndNameContainsIgnoreCaseAndDeletedFalse(
        batch, Role.STUDENT, "E-2", new PageRequest(0, 10));

    assertThat(foundUsers.getContent()).isNotEmpty();
    assertThat(foundUsers.getContent()).isEqualTo(
      Collections.singletonList(user2));
  }

}
