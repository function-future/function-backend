package com.future.function.repository.feature.communication.questionnaire;


import com.future.function.repository.TestApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationQuestionnaire.class)
public class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;


}
