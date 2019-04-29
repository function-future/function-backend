package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.service.api.feature.scoring.QuizService;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class used to manipulate Quiz Entity
 * Used QuizRepository and QuestionBankService to help manipulate quiz entity
 */
@Service
public class QuizServiceImpl implements QuizService {

  private QuizRepository quizRepository;

//  private QuestionBankService questionBankService;

  @Autowired
  public QuizServiceImpl(QuizRepository quizRepository) {
    this.quizRepository = quizRepository;
  }

  /**
   * Used to find quiz from repository by passing the quiz id
   * @param id (String)
   * @return Quiz object
   */
  @Override
  public Quiz findById(String id) {
    return Optional.ofNullable(id)
            .filter(val -> !val.isEmpty())
            .map(quizRepository::findByIdAndDeletedFalse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .orElseThrow(() -> new NotFoundException("Quiz Not Found"));
  }

  /**
   * Used to find all quiz from repository in paging format by passing pageable, filter, and search parameter
   * @param pageable (Pageable)
   * @param filter (String)
   * @param search (String)
   * @return Page<Quiz> object
   */
  @Override
  public Page<Quiz> findAllByPageableAndFilterAndSearch(Pageable pageable, String filter, String search) {
    return quizRepository.findAll(pageable);
  }

  /**
   * Used to create new quiz in repository by passing the requested quiz entity object
   * @param request (Quiz)
   * @return Quiz object
   */
  @Override
  public Quiz createQuiz(Quiz request) {
    return Optional.of(request)
            .map(quizRepository::save)
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  /**
   * Used to update existing quiz from repository by passing the requested quiz entity object with its id
   * @param request (Quiz)
   * @return Quiz object
   */
  @Override
  public Quiz updateQuiz(Quiz request) {
    return Optional.of(request)
            .map(Quiz::getId)
            .map(this::findById)
            .map(val -> {
              BeanUtils.copyProperties(request, val);
              return val;
            })
            .map(quizRepository::save)
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  /**
   * Used to delete existing quiz from repository by passing the quiz id
   * @param id (String)
   */
  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
            .map(this::findById)
            .ifPresent(val -> {
              val.setDeleted(true);
              quizRepository.save(val);
            });
  }
}
