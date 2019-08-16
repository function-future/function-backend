## Communication - My Questionnaires [/api/communication/my-questionnaires{?search,page,size}]

+ Parameters
    + search (optional, string) - search parameter for searching spesific data in request
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request

### GET My Questionnaires [GET]

    GET questionnaires member as appraiser

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

## Communication - My Questionnaire appraisees [/api/communication/my-questionnaires/{questionnaireId}/appraisees]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved

### GET Get List Appraised by member login [GET]

    get list of appraisals that haven't been given a response based on the member who logged in and questionnaireId

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
                    "name" : "Priagung Satygama",
                    "role" :  "STUDENT",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university" : "Institut Teknologi Bandung"
                },
                {
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

## Communication - My Questionnaire data [/api/communication/my-questionnaires/{questionnaireId}/appraisees/{appraiseeId}]

### GET Appraisal data [GET]

    get data based on questionnaire and appraiseeId

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
                    "duedate" : 1558675267,
                    "duedate" : 1558675267
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
                }
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
        
## Communication - My Questionnaire Questions [/api/communication/my-questionnaires/{questionnaireId}/appraisees/{appraiseeId}/questions]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved

### GET Questionnaire Questions [GET]

    Create Questionnaire response on questionnaire with id same as questionnaireId.

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
                    "questionnaireId" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "desc" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo"
                },
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
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
        
## Communication - My Questionnaire Response [/api/communication/my-questionnaires/{questionnaireId}/appraisees/{appraiseeId}/responses]

+ Parameters
    + questionnaireId (string) - ID of questionnaire to be retrieved

### POST Create Questionnaire response [POST]

    Create Questionnaire response on questionnaire with id same as questionnaireId.

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "responses" : [
                    {
                        "idQuestion" : "Question-63dc9f59-a579-4b69-a80c-a3c48d794f16"
                        "score" : 5.0,
                        "comment" : "Lorem ipsum"
                    },
                    {
                        "idQuestion" : "Question-63dc9f59-a579-4b69-a80c-a3c48d794f16"
                        "score" : 5.0,
                        "comment" : "Lorem ipsum"
                    },
                    {
                        "idQuestion" : "Question-63dc9f59-a579-4b69-a80c-a3c48d794f16"
                        "score" : 5.0,
                        "comment" : "Lorem ipsum"
                    },
                    
                ]
            
            }
            

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
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
