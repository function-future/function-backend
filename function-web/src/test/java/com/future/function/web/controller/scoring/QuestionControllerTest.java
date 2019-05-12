package com.future.function.web.controller.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuestionRequestMapper;
import com.future.function.web.mapper.response.scoring.QuestionResponseMapper;
import com.future.function.web.model.request.scoring.OptionWebRequest;
import com.future.function.web.model.request.scoring.QuestionWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.OptionWebResponse;
import com.future.function.web.model.response.feature.scoring.QuestionWebResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    private static final String QUESTION_ID = "question-id";
    private static final String QUESTION_TEXT = "question-text";
    private static final String OPTION_ID = "option-id";
    private static final String OPTION_LABEL = "option-label";
    private static final String QUESTION_BANK_ID = "question-bank-id";

    private static final String QUESTION_CREATE_REQUEST_JSON =
            "{\n" + "\"text\": \"" + QUESTION_TEXT + "\",\n" + "    " +
                    "\"options\": [\n" +
                    "{\"label\": \"" + OPTION_LABEL + "\",\n" + "\"correct\" : " + true + "}]}";

    private static final String QUESTION_UPDATE_REQUEST_JSON =
            "{\n" + "\"text\": \"" + QUESTION_TEXT + "\",\n" + "    " +
                    "\"options\": [\n" +
                    "{\"id\": \"" + OPTION_ID + "\",\n" +
                    "\"label\": \"" + OPTION_LABEL + "\",\n" +
                    "\"correct\": " + true + "}]}";

    private Question question;
    private Option option;

    private QuestionWebRequest questionWebRequest;
    private OptionWebRequest optionWebRequest;

    private Pageable pageable;
    private Page<Question> questionPage;

    private QuestionWebResponse questionWebResponse;
    private OptionWebResponse optionWebResponse;


    private DataResponse<QuestionWebResponse> DATA_RESPONSE;
    private DataResponse<QuestionWebResponse> CREATED_DATA_RESPONSE;
    private PagingResponse<QuestionWebResponse> PAGING_RESPONSE;
    private BaseResponse BASE_RESPONSE;

    private JacksonTester<DataResponse<QuestionWebResponse>> dataResponseJacksonTester;
    private JacksonTester<PagingResponse<QuestionWebResponse>> pagingResponseJacksonTester;
    private JacksonTester<BaseResponse> baseResponseJacksonTester;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private QuestionRequestMapper questionRequestMapper;

    @Before
    public void setUp() throws Exception {

        JacksonTester.initFields(this, new ObjectMapper());

        option = Option
                .builder()
                .label(OPTION_LABEL)
                .correct(true)
                .build();

        question = Question
                .builder()
                .text(QUESTION_TEXT)
                .options(Collections.singletonList(option))
                .build();

        optionWebRequest = OptionWebRequest
                .builder()
                .label(OPTION_LABEL)
                .correct(true)
                .build();

        questionWebRequest = QuestionWebRequest
                .builder()
                .text(QUESTION_TEXT)
                .options(Collections.singletonList(optionWebRequest))
                .build();

        optionWebResponse = OptionWebResponse
                .builder()
                .label(OPTION_LABEL)
                .correct(true)
                .build();

        questionWebResponse = QuestionWebResponse
                .builder()
                .text(QUESTION_TEXT)
                .options(Collections.singletonList(optionWebResponse))
                .build();

        pageable = new PageRequest(0, 10);
        questionPage = new PageImpl<>(Collections.singletonList(question), pageable, 10);

        DATA_RESPONSE = QuestionResponseMapper
                .toQuestionWebResponse(this.question);

        CREATED_DATA_RESPONSE = QuestionResponseMapper
                .toQuestionWebResponse(HttpStatus.CREATED, this.question);

        PAGING_RESPONSE = QuestionResponseMapper
                .toQuestionPagingResponse(questionPage);

        BASE_RESPONSE = ResponseHelper.toBaseResponse(HttpStatus.OK);

        when(questionService.findById(QUESTION_ID)).thenReturn(question);
        when(questionService.createQuestion(question, QUESTION_BANK_ID)).thenReturn(question);
        when(questionService.updateQuestion(question, QUESTION_BANK_ID)).thenReturn(question);
        when(questionService.findAllByQuestionBankId(QUESTION_BANK_ID, pageable)).thenReturn(questionPage);
        when(questionRequestMapper.toQuestion(questionWebRequest)).thenReturn(question);
        when(questionRequestMapper.toQuestion(questionWebRequest, QUESTION_ID)).thenReturn(question);

    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(questionService, questionRequestMapper);
    }

    @Test
    public void getAllQuestionForQuestionBank() throws Exception {
        mockMvc.perform(
                get("/api/scoring/question-banks/" + QUESTION_BANK_ID + "/questions")
                        .param("page", "1")
                        .param("size", "10")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        pagingResponseJacksonTester.write(PAGING_RESPONSE)
                                .getJson()
                ));

        verify(questionService).findAllByQuestionBankId(QUESTION_BANK_ID, pageable);
    }

    @Test
    public void getQuestionDetailById() throws Exception {
        mockMvc.perform(
                get("/api/scoring/question-banks/" + QUESTION_BANK_ID + "/questions/" + QUESTION_ID)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(DATA_RESPONSE)
                                .getJson()
                ));
        verify(questionService).findById(QUESTION_ID);
    }

    @Test
    public void createQuestionForQuestionBank() throws Exception {
        mockMvc.perform(
                post("/api/scoring/question-banks/" + QUESTION_BANK_ID + "/questions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(QUESTION_CREATE_REQUEST_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
                                .getJson()
                ));
        verify(questionService).createQuestion(question, QUESTION_BANK_ID);
        verify(questionRequestMapper).toQuestion(questionWebRequest);
    }

    @Test
    public void updateQuestionForQuestionBank() throws Exception {
        questionWebRequest.getOptions().get(0).setId(OPTION_ID);
        when(questionRequestMapper.toQuestion(questionWebRequest, QUESTION_ID)).thenReturn(question);

        mockMvc.perform(
                put("/api/scoring/question-banks/" + QUESTION_BANK_ID + "/questions/" + QUESTION_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(QUESTION_UPDATE_REQUEST_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        dataResponseJacksonTester.write(DATA_RESPONSE)
                                .getJson()
                ));
        verify(questionService).updateQuestion(question, QUESTION_BANK_ID);
        verify(questionRequestMapper).toQuestion(questionWebRequest, QUESTION_ID);
    }

    @Test
    public void deleteQuestionFromQuestionBank() throws Exception {
        mockMvc.perform(
                delete("/api/scoring/question-banks/" + QUESTION_BANK_ID + "/questions/" + QUESTION_ID)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        baseResponseJacksonTester.write(BASE_RESPONSE)
                                .getJson()
                ));
        verify(questionService).deleteById(QUESTION_ID);
    }
}