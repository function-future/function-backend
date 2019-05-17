package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

    Page<Question> findAllByQuestionBankId(String questionBankId, Pageable pageable);

    Question findById(String id);

    Question createQuestion(Question question, String questionBankId);

    Question updateQuestion(Question question, String questionBankId);

    void deleteById(String id);

}
