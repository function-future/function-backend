package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class UserQuestionnaireSummaryRepositoryTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String ID_1 = "id_1";

  private static final String ID_2 = "id_2";

  private static final String ID_3 = "id_3";

  private static final String ID_USER_1 = "id_user1";

  private static final String ID_USER_2 = "id_user2";

  private static final String ID_MENTOR = "id_mentor";

  private static final String BATCH_ID = "batch_id";

  private User user1 = User.builder()
    .id(ID_USER_1)
    .build();

  private User user2 = User.builder()
    .id(ID_USER_2)
    .build();

  private User mentor = User.builder()
    .id(ID_MENTOR)
    .build();

  private Batch batch = Batch.builder()
    .id(BATCH_ID)
    .build();

  @Autowired
  private UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  @Autowired
  private UserRepository userRepository;

  @Before
  public void SetUp() {

    UserQuestionnaireSummary uQS1 = UserQuestionnaireSummary.builder()
      .id(ID_1)
      .appraisee(user1)
      .batch(batch)
      .role(Role.STUDENT)
      .build();

    UserQuestionnaireSummary uQS2 = UserQuestionnaireSummary.builder()
      .id(ID_2)
      .appraisee(user2)
      .batch(batch)
      .role(Role.STUDENT)
      .build();

    UserQuestionnaireSummary mentorSummary = UserQuestionnaireSummary.builder()
      .id(ID_3)
      .appraisee(mentor)
      .role(Role.MENTOR)
      .build();

    userRepository.save(user1);
    userRepository.save(user2);
    userRepository.save(mentor);
    userQuestionnaireSummaryRepository.save(uQS1);
    userQuestionnaireSummaryRepository.save(uQS2);
    userQuestionnaireSummaryRepository.save(mentorSummary);
  }

  @After
  public void TearDown() {

    userQuestionnaireSummaryRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testByFindingAllUserQuestionnaireSummaryReturnedPagedUserQuestionnaireSummary() {

    Page<UserQuestionnaireSummary> userQuestionnaireSummaries =
      userQuestionnaireSummaryRepository.findAllByDeletedFalse(PAGEABLE);

    assertThat(userQuestionnaireSummaries.getTotalElements()).isEqualTo(3);
    assertThat(userQuestionnaireSummaries.getContent()
                 .get(0)
                 .getId()).isEqualTo(ID_1);
    assertThat(userQuestionnaireSummaries.getContent()
                 .get(1)
                 .getId()).isEqualTo(ID_2);
    assertThat(userQuestionnaireSummaries.getContent()
                 .get(2)
                 .getId()).isEqualTo(ID_3);
  }

  @Test
  public void testGivenApraiseeByFindingUserQuestionnaireSummaryReturnUserQuestionnaireSummary() {

    Optional<UserQuestionnaireSummary> userQuestionnaireSummary1 =
      userQuestionnaireSummaryRepository.findFirstByAppraiseeAndDeletedFalse(
        user1);
    Optional<UserQuestionnaireSummary> userQuestionnaireSummary2 =
      userQuestionnaireSummaryRepository.findFirstByAppraiseeAndDeletedFalse(
        user2);

    assertThat(userQuestionnaireSummary1.get()
                 .getAppraisee()
                 .getId()).isEqualTo(ID_USER_1);
    assertThat(userQuestionnaireSummary2.get()
                 .getAppraisee()
                 .getId()).isEqualTo(ID_USER_2);
  }

  @Test
  public void testGivenRoleAndBatchByFindingAllUserQuestionnaireSummaryReturnedPagedUserQuestionnaireSummary() {

    Page<UserQuestionnaireSummary> userQuestionnaireSummaries =
      userQuestionnaireSummaryRepository.findAllByRoleOrRoleAndBatchAndDeletedFalse(
        Role.MENTOR, Role.STUDENT, batch, PAGEABLE);

    assertThat(userQuestionnaireSummaries.getTotalElements()).isEqualTo(3);
  }

  @Test
  public void testGivenRoleByFindingAllUserQuestionnaireSummaryReturnedPagedUserQuestionnaireSummary() {

    Page<UserQuestionnaireSummary> userQuestionnaireSummaries =
      userQuestionnaireSummaryRepository.findAllByRoleAndDeletedFalse(
        Role.MENTOR, PAGEABLE);

    assertThat(userQuestionnaireSummaries.getTotalElements()).isEqualTo(1);
  }

}
