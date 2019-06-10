package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
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

  private static final Pageable PAGEABLE = new PageRequest(0,10);

  private static final String ID_1 = "id_1";

  private static final String ID_2 = "id_2";

  private static final String ID_USER_1 = "id_user1";

  private static final String ID_USER_2 = "id_user2";

  private User user1 = User.builder()
          .id(ID_USER_1)
          .build();

  private User user2 = User.builder()
          .id(ID_USER_2)
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
            .build();

    UserQuestionnaireSummary uQS2 = UserQuestionnaireSummary.builder()
            .id(ID_2)
            .appraisee(user2)
            .build();

    userRepository.save(user1);
    userRepository.save(user2);
    userQuestionnaireSummaryRepository.save(uQS1);
    userQuestionnaireSummaryRepository.save(uQS2);
  }

  @After
  public void TearDown() {
    userQuestionnaireSummaryRepository.deleteAll();
  }

  @Test
  public void testByFindingAllUserQuestionnaireSummaryReturnedPagedUserQuestionnaireSummary() {
    Page<UserQuestionnaireSummary> userQuestionnaireSummaries = userQuestionnaireSummaryRepository.findAll(PAGEABLE);

    assertThat(userQuestionnaireSummaries.getTotalElements()).isEqualTo(2);
    assertThat(userQuestionnaireSummaries.getContent().get(0).getId()).isEqualTo(ID_1);
    assertThat(userQuestionnaireSummaries.getContent().get(1).getId()).isEqualTo(ID_2);
  }

  @Test
  public void testGivenApraiseeByFindingUserQuestionnaireSummaryReturnUserQuestionnaireSummary() {
    Optional<UserQuestionnaireSummary> userQuestionnaireSummary1 = userQuestionnaireSummaryRepository.findFirstByAppraisee(user1);
    Optional<UserQuestionnaireSummary> userQuestionnaireSummary2 = userQuestionnaireSummaryRepository.findFirstByAppraisee(user2);

    assertThat(userQuestionnaireSummary1.get().getAppraisee().getId()).isEqualTo(ID_USER_1);
    assertThat(userQuestionnaireSummary2.get().getAppraisee().getId()).isEqualTo(ID_USER_2);
  }
}
