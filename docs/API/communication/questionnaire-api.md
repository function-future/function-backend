## Communication - Questionnaires [/api/communication/questionnaires{?search,page,size}]

+ Parameters
    + search (optional, string) - search parameter for searching spesific data in request
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request

### GET Questionnaires [GET]

    GET questionnaires

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
                    "startDate" : 1558650121,
                    "dueDate" : 1558675267
                },
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "title" : "Kuisioner Mentoring Jakarta",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                    "startDate" : 1558650121,
                    "dueDate" : 1558675267
                },
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "title" : "Kuisioner Mentoring Medan",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                    "startDate" : 1558650121,
                    "dueDate" : 1558675267
                }                
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 3
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

### POST Create Questionnaire [POST]

    Create Questionnaire with author is authenticated admin.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title" : "Kuisioner Mentoring Bandung",
                "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                "startDate" : 1558650121,
                "dueDate" : 1558675267
            }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "title" : "Kuisioner Mentoring Bandung",
                "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                "startDate" : 1558650121,
                "dueDate" : 1558675267
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

## Communication - Questionnaire Description [/api/communication/questionnaires/{questionnaireId}]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved

### GET Questionnaire Descriptions [GET]

    GET questionnaire details by id

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
        
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "title" : "Kuisioner Mentoring Bandung",
                "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                "startDate" : 1558650121,
                "dueDate" : 1558675267
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

### PUT Update Questionnaire Descriptions [PUT]

    Update the Questionnaire with the same id as questionnaireId.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title" : "Kuisioner Mentoring Bandung",
                "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                "startDate" : 1558650121,
                "dueDate" : 1558675267,
                "authorId" : "admin-63dc9f59-a579-4b69-a80c-a3c48d794f16"
            }

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "title" : "Kuisioner Mentoring Bandung",
                "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                "startDate" : 1558650121,
                "dueDate" : 1558675267
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

### DELETE Delete Questionnaire  [DELETE]

    Delete the Questionnaire with the same id as questionnaireId.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
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


## Communication - Questionnaire Questions [/api/communication/questionnaires/{questionnaireId}/questions]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved

### GET Questionnaire Questions [GET]

    GET questionnaire quetions by id

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
        
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "questions-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo"
                },
                {
                    "id" : "questions-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo"
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
        
### POST Create Questions [POST]

    Create QuestionQuestionnaire with author is authenticated admin.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
            }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo"
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
        
## Communication - Questionnaire Questions [/api/communication/questionnaires/{questionnaireId}/questions/{questionId}]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved
    + questionId (string) - ID of questions to be retrieved

### PUT Update Questionnaire Question

    update the question with Id same as questionId

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "desc" : "Lorem ipsum"
            }

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "desc" : "Lorem ipsum"
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

### DELETE Delete Questionnaire Questions [DELETE]

    Delete the Question with the same id as questionId inside questionnaire.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
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
        
## Communication - Questionnaire Appraisee [/api/communication/questionnaires/{questionnaireId}/appraisee]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved

### GET Questionnaire Appraisees [GET]

    GET questionnaire appraisees by id questionnaire

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
        
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "user-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "participantId" : "user-63dc9f59-a579-4b69-a80c-a3c48d794f18",
                    "name" : "Priagung",
                    "university" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "role" : "STUDENT"
                    "batch" : "3",
                    "avatar" : "https://dummyimage.com/600x400/000/fff"
                },
                {
                    "id" : "user-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "name" : "Priagung",
                    "university" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "role" : "STUDENT"
                    "batch" : "3",
                    "avatar" : "https://dummyimage.com/600x400/000/fff"
                }
            ],
            "paging" : {
                "page" : 2,
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

### POST Add Appraisee [POST]
Add questionnaire participant as appraisee.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "idParticipant" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
            }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "questionnaire" : "q3dc9f59-a579-4b69-a80c-a3c48d794f16",
                "memberId" : "q3dc9f59-a579-4b69-a80c-a3c48d794f16",
                "particpantType" : "appraisee"
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

## Communication - Questionnaire appraisee [/api/communication/questionnaires/{questionnaireId}/appraisee/{appraiseeId}]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved
    + questionId (string) - ID of questions to be retrieved

### DELETE Delete Questionnaire Appraisee [DELETE]

    Delete the Question with the same id as questionId inside questionnaire.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
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

## Communication - Questionnaire Appraiser [/api/communication/questionnaires/{questionnaireId}/appraiser?page,size]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved

### GET Questionnaire Appraiser [GET]

    GET questionnaire quetions by id

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
        
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "user-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "participantId" : "user-63dc9f59-a579-4b69-a80c-a3c48d794f18",
                    "name" : "Priagung",
                    "university" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "role" : "STUDENT"
                    "batch" : "3",
                    "avatar" : "https://dummyimage.com/600x400/000/fff"
                },
                {
                    "id" : "user-63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "name" : "Priagung",
                    "university" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "role" : "STUDENT"
                    "batch" : "3",
                    "avatar" : "https://dummyimage.com/600x400/000/fff"
                }
            ],
            "paging" : {
                "page" : 2,
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

### POST Add Appraiser [POST]
    Add questionnaire participant as appraiser.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "idParticipant" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
            }

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "questionnaireId" : "q3dc9f59-a579-4b69-a80c-a3c48d794f16",
                "memberId" : "q3dc9f59-a579-4b69-a80c-a3c48d794f16",
                "particpantType" : "appraiser"
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
        
## Communication - Questionnaire appraiser [/api/communication/questionnaires/{questionnaireId}/appraiser/{questionnaireParticipantId}]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved
    + questionId (string) - ID of questions to be retrieved

### DELETE Delete Questionnaire Appraisers [DELETE]

    Delete the Question with the same id as questionId inside questionnaire.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
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
