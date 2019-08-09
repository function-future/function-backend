## Scoring - Quiz [/api/scoring/batches/{batchCode}/quizzes{?page,size}]

+ Parameters
    + batchCode (string) - specify the batch
    + page (optional, number) - Describing the page currently displayed in request
    + size (optional, number) - Describing how many data should be displayed on the current page in request

### Get Quiz List [GET]

    Get quiz list for all user. Parameters are sort for sorting the quiz list by (uploaded date, status, deadline, etc), search for searching the quiz based on (ID, Title, and Description), page for specifying the page number currently showed, and size for specifying how many data wanted to be showed. It will return 401 if the user is not authenticated, and will return 403 if the user role / the user itself is not appropriate to access this url.

+ Request

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "quizes" : [
                {
                    "id" : "QZ00001",
                    "title" : "Quiz Number 1",
                    "description" : "Description Number 1",
                    "startDate" : 1565341924738,
                    "endDate" : 1565342924738,
                    "timeLimit" : 3600,
                    "trials" : 3,
                    "questionCount": 10,
                    "questionBanks": ["QNK00001"],
                    "batchCode": "3"
                },
                {
                    "id" : "QZ00002",
                    "title" : "Quiz Number 2",
                    "description" : "Description Number 2",
                    "startDate" : 1565341924738,
                    "endDate" : 1565342924738,
                    "timeLimit" : 3600,
                    "trials" : 3,
                    "questionCount": 10,
                    "questionBanks": ["QNK00001"],
                    "batchCode": "3"
                },
                {
                    "id" : "QZ00003",
                    "title" : "Quiz Number 3",
                    "description" : "Description Number 3",
                    "startDate" : 1565341924738,
                    "endDate" : 1565342924738,
                    "timeLimit" : 3600,
                    "trials" : 3,
                    "questionCount": 10,
                    "questionBanks": ["QNK00001"],
                    "batchCode": "3"
                }
                ]
            },
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 3
            }
        }
        
+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
### Create New Quiz [POST]

    Create new quiz by sending request with the format below. Cookie for authenticate the user and verify the user role as admin, and body that consist of quiz attributes. It will return 400 if the body passed in the request is not valid, will return 401 if the user is not authenticated, and will return 403 if the user role is not admin

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title" : "Quiz 1",
                "description" : "Description Number 1",
                "startDate" : 1565341991034,
                "endDate" : 1565342924738,
                "timeLimit" : 3600,
                "trials" : 5,
                "questionBanks" : ["QNK0001"],
                "questionCount" : 10
            }
        
+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "QZ00002",
                "title" : "Quiz 1",
                "description" : "Description Number 1",
                "startDate" : 1565341991034,
                "endDate" : 1565342924738,
                "timeLimit" : 3600,
                "trials" : 5,
                "questionBanks" : ["QNK0001"],
                "questionCount" : 10,
                "batchCode": "3"
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "title" : ["NotEmpty"],
                "description" : ["NotBlank"]
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
        
## Scoring - Quiz Batch Copy [/api/scoring/batches/{batchCode}/quizzes/copy]

+ Parameters
    + batchCode (string) - specify the batch

### Copy Quiz [POST]

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "batchCode" : "batch-code",
                "quizId" : "quiz-id"
            }
        
+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id" : "QZ00002",
                "title" : "Quiz 1",
                "description" : "Description Number 1",
                "startDate" : 1565342059690,
                "endDate" : 1565352059690,
                "timeLimit" : 3600,
                "trials" : 5,
                "questionBanks" : ["QNK0001"],
                "questionCount" : 10,
                "batchCode": "batch-code"
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "batchId" : ["NotBlank"],
                "quizId" : ["NotBlank"]
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
        
## Scoring - Quiz Detail [/api/scoring/batches/{batchCode}/quizzes/{quizId}]

+ Parameter

    + batchCode (string) - specify the batch
    + quizId (string) - ID of specific quiz

### Get Quiz Detail [GET]
    
    Get quiz detail of specific quiz by passing the quiz ID in the url. It will return the code, status, and data that contain the quiz detail data. It will return 401 if the user is not authenticated, will return 403 if the user role is not admin, and will return 404 if the quiz is not found

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "QZ0001",
                "title" : "Quiz Title 1",
                "description" : "Description Number 1",
                "startDate" : 1565342108802,
                "endDate" : 1565352108802,
                "timeLimit" : 3600,
                "trials" : 3,
                "questionCount": 10,
                "questionBanks": ["QNK00001"],
                "batchCode": "3"
            }
        }
        
+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }
        
### Update Quiz Detail [PUT]

    Update the quiz detail by sending the request that consist of Cookie header, body, and quiz id in the url. it will return 400 if the body in the request is not valid, will return 401 if the user is not authenticated, will return 403 if the user role is not appropriate to access this url, and will return 404 if the quiz is not found.

+ Request (application/json)
    
    + Headers
        
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
        
    + Body

            {
                "title" : "Quiz 1",
                "description" : "Description Number 1",
                "startDate" : 1565342108802,
                "endDate" : 1565352108802,
                "timeLimit" : 6000,
                "trials" : 2,
                "questionCount": 15,
                "questionBanks": ["QNK00001"]
            }
        
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : 
            {
                "id" : "QZ0001",
                "title" : "Quiz 1",
                "description" : "Description Number 1",
                "startDate" : 1565342108802,
                "endDate" : 1565352108802,
                "timeLimit" : 6000,
                "trials" : 2,
                "questionBanks" : ["QNK0002"],
                "questionCount" : 15,
                "batchCode": "3"
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "title" : ["NotBlank"]
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
        
### Delete Quiz [DELETE]

    Delete the quiz by passing the quiz id in the url. It will return 401 if the user is not authenticated, and will return 403 if the user role is not appropriate to access this url.

+ Request (application/octet)
    
    + Headers
        
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
        
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
        }
