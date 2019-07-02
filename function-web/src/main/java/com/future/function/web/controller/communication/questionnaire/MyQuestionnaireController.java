package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.communication.questionnaire.MyQuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraiseeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/communication/my-questionnaires")
@WithAnyRole(roles = { Role.STUDENT, Role.MENTOR })
public class MyQuestionnaireController {

  private final MyQuestionnaireService myQuestionnaireService;

  private final UserService userService;

  @Autowired
  public MyQuestionnaireController(
    MyQuestionnaireService myQuestionnaireService,
    UserService userService) {
    this.myQuestionnaireService = myQuestionnaireService;
    this.userService = userService;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse getMyQuestionnaires(@RequestParam(required = false) String search,
                                            @RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            Session session
                                            )
  {
   if(search != null) {
     return null;
   } else {
     return QuestionnaireResponseMapper
              .toPagingQuestionnaireDetailResponse(
                myQuestionnaireService.getQuestionnairesByMemberLoginAsAppraiser(
                  userService.getUser(session.getUserId()),
                  PageHelper.toPageable(page, size)
                )
              );
   }
  }

//  @ResponseStatus(HttpStatus.OK)
//  @GetMapping(value = "/{questionnaireId}/appraisees")
//  public DataResponse<AppraiseeResponse> get
}
