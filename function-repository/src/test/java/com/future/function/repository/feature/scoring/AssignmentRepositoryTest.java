package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import java.util.Date;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssignmentRepositoryTest {

  private static final String ASSIGNMENT_TITLE = "assignment-title";
  private static final String ASSIGNMENT_DESCRIPTION = "assignment-description";
  private static final long ASSIGNMENT_DEADLINE = new Date().getTime();
  private static final String ASSIGNMENT_QUESTION = "assignment-question";

  private static Assignment assignment = Assignment
          .builder()
          .title(ASSIGNMENT_TITLE)
          .description(ASSIGNMENT_DESCRIPTION)
          .deadline(ASSIGNMENT_DEADLINE)
          .question(ASSIGNMENT_QUESTION)
          .build();

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Before
  public void setUp() throws Exception {

    assignmentRepository.save(assignment);
  }

  @After
  public void tearDown() throws Exception {
    assignmentRepository.deleteAll();
  }

  @Test
  public void findAssignmentByIdAndDeletedFalse() {
    Optional<Assignment> result = assignmentRepository.findByIdAndDeletedFalse(assignment.getId());
    assertTrue(result.isPresent());
    assertEquals(assignment, result.get());
  }
}