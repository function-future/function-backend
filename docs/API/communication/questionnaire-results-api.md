## Communication - Questionnaire Results Members [/api/communication/questionnaire-results{?batchCode, page, size}]

+ Parameters
    + search (optional, string) - search parameter for searching spesific data in request
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request

### GET UserSummary[GET]
        
    GET user summary response by batch
    
+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {   
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "member" :{
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "role": "STUDENT",
                        "name": "Priagung Satyagama",
                        "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "university": "Institut Teknologi Bandung"
                    },
                    "rating" : 5.1
                },
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "member" :{
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "role": "STUDENT",
                        "name": "Priagung Satyagama",
                        "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "university": "Institut Teknologi Bandung"
                    },
                    "rating" : 5.1
                }
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 2
            }
        }    

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }

## Communication - Questionnaire Results Members [/api/communication/questionnaire-results/{batchCode}/user-summary-response/{userSummaryId}]

+ Parameters
    + batchCode - batchCode from user will be retrieve
    + userSummaryId - user summary will be retrieve with id same as userSummaryId

### GET UserSummaryById[GET]
        
    GET user summary response by batch
    
+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {   
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "member" :{
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "role": "STUDENT",
                    "name": "Priagung Satyagama",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Institut Teknologi Bandung"
                },
                "rating" : 5.1
            }
        }    

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }
        
## Communication - Questionnaires Results list questionnaire summary appraisee [/api/communication/questionnaire-response/{?userSummaryId,page,size}]

+ Parameters
    + userSummaryId (string) - userSummaryId parameter for search questionnaire related with the userSummary Id in request
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request

### GET Questionnaires summaries based on appraisee [GET]
    
    Get All Questionnaires appraised on member by memberId.

+ Parameters
    + memberId (string) -  retrieved questionnaires with the same appriasedId as memberId

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "title" : "Kuisioner Mentoring Bandung",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                    "status" : "FINISHED",
                    "startDate" : 1557675267,
                    "duedate" : 1558675267,
                    "score" : 6.0
                },
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "title" : "Kuisioner Mentoring Jakarta",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                    "status" : "ON_GOING",
                    "duedate" : 1558675267,
                    "score" : 6.0
                }
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 2
            }
        }    

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }

## Communication - Questionnaire Results questionnaire summary detail [/api/communication/questionnaire-response/{questionnaireResponseSummaryId}]

+ Parameters
    + questionnaireResponseSummaryId (string) - questionnaireResponseSummary parameter for searching spesific data questionnaireResponseSummary in request

### GET Questionnaire summary details [GET]

    
+ Parameters
    + memberId (string) - Id of member to be retrieved

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "questionnaireDetail" : {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "title" : "Kuisioner Mentoring Bandung",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                    "startDate" : 1558675267,
                    "dueDate" : 1558675267
                },
                "appraisee" : { 
                    "id" : "user-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "name" : "Priagung Satygama",
                    "role" : "STUDENT",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university" : "Institut Teknologi Bandung"
                },
                "rating" : 5.1
                
            }
        }  
        

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }

//## Communication - Questionnaire Results details based on member  [/api/communication/question-response-summaries/{?apraisee,questionnaireId}]

## Communication - Questionnaire Results details based on member  [/api/communication/questionnaire-response/{questionnaireResponseSummaryId}/questions/{userSummaryId}]

+ Parameters
    + questionnaireResponseSummaryId (string) - questionnaireResponseSummary parameter for searching spesific data questionnaireResponseSummary in request
    + userSummaryId (string) - userSummaryId parameter for search questionnaire related with the userSummary Id in request

### GET Questionnaire details appraised on member [GET]

    GET questionnaire details apraised on memberId and quetionnareId.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "question" : {
                        "id" : "questions-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo"
                    },
                    "score" : 6.0
                },
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "question" : {
                        "id" : "questions-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo"
                    },
                    "score" : 6.0
                },
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "question" : {
                        "id" : "questions-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo"
                    },
                    "score" : 6.0
                },
            ]
        }    

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }


## Communication - Questionnaire Results get question desc based on question response id [/api/communication/question-response-summaries/{questionResponseSummaryId}]
### GET getQuestionQuestionnaireSummaryResponse [GET]

    GET spesific questions response from by questionnaireId
    
+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16", 
                "quetion" : {
                    "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16"
                    "title" : "Kuesioner Mentoring Bandung",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                },
                "rating" : 6
            }
        }    

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }
        
## Communication - Questionnaire Result get question response bases on question response id  [/api/communication/question-response-summaries/{questionResponseSummaryId}/responses]
### GET getQuestionnaireAnswerDetailSummary [GET]

    GET list questionnaire response from by questionResponseSummaryId

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "name" : "Ricky Satyagama",
                    "avatar" : "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "score" : 6.0
                },
                {
                    "name" : "Ricky Satyagama",
                    "avatar" : "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "score" : 6.0
                },
                {
                    "name" : "Ricky Satyagama",
                    "avatar" : "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "score" : 6.0
                }
           ]
        }    

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }
