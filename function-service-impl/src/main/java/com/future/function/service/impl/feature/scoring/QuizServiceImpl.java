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
import org.springframework.util.StringUtils;

@Service
public class QuizServiceImpl implements QuizService {

  private QuizRepository quizRepository;

//  private QuestionBankService questionBankService;

  @Autowired
  public QuizServiceImpl(QuizRepository quizRepository) {
    this.quizRepository = quizRepository;
  }

  @Override
  public Quiz findById(String id) {
    return Optional.ofNullable(id)
            .filter(val -> !val.isEmpty())
            .map(quizRepository::findByIdAndDeletedFalse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .orElseThrow(() -> new NotFoundException("Quiz Not Found"));
  }

  @Override
  public Page<Quiz> findAllByPageableAndFilterAndSearch(Pageable pageable, String filter, String search) {
    filter = Optional.ofNullable(filter)
            .orElse("");
    search = Optional.ofNullable(search)
            .orElse("");
    return quizRepository.findAll(pageable);
  }

  @Override
  public Quiz createQuiz(Quiz request) {
    return Optional.of(request)
            .map(quizRepository::save)
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

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
