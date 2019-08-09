## Scoring - Student All Quiz [/api/scoring/students/{studentId}/quizzes]

+ Parameters
    + studentId (string) - specify student id

### Get All Student Quiz [GET]

    Get all student quiz and its detail by passing the student id in the url. It will return 401 if the user is not authenticated, and will return 403 if the user role / the user itself is not appropriate to access this url.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 2
            },
            "data" : [
                {
                    "id": "sample-id",
                    "trials": 3,
                    "quiz": {
                        "id" : "QZ0001",
                        "title" : "Quiz 2",
                        "description" : "Description for Quiz 2",
                        "startDate" : 1565343059389,
                        "endDate" : 1566343059389,
                        "timeLimit" : 3600,
                        "trials" : 3,
                        "questionCount": 10,
                        "questionBanks": ["QNK00001"],
                        "batchCode": "3"
                    }
                },
                {
                    "id": "sample-id",
                    "trials": 3,
                    "quiz": {
                        "id" : "QZ0001",
                        "title" : "Quiz 2",
                        "description" : "Description for Quiz 2",
                        "startDate" : 1565343059389,
                        "endDate" : 1566343059389,
                        "timeLimit" : 3600,
                        "trials" : 3,
                        "questionCount": 10,
                        "questionBanks": ["QNK00001"],
                        "batchCode": "3"
                    }
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
        
## Scoring - Student Quiz [/api/scoring/students/{studentId}/quizzes/{quizId}]

+ Parameters
    + studentId (string) - specify the student id
    + quizId (string) - specify the quiz id

### Get Student Quiz Detail[GET]

    Get Specific quiz and its detail along with student own actual questions and answers by passing the studentId and the quizId. It will return 401 if the user is not authenticated, will return 403 if the user role or the user itself is not appropriate to access this url, and will return 404 if the student or the quiz is not found.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                    "id": "sample-id",
                    "trials": 3,
                    "quiz": {
                        "id" : "QZ0001",
                        "title" : "Quiz 2",
                        "description" : "Description for Quiz 2",
                        "startDate" : 1565343059389,
                        "endDate" : 1566343059389,
                        "timeLimit" : 3600,
                        "trials" : 3,
                        "questionCount": 10,
                        "questionBanks": ["QNK00001"],
                        "batchCode": "3"
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
        
## Scoring - Student Quiz Questions [/api/scoring/students/{studentId}/quizzes/{quizId}/questions]

+ Parameters
    
    + studentId (string) - specify the student
    + quizId (string) - specify the quiz

### Get All Student Quiz Questions [GET]

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                        {
                            "number" : 1,
                            "text" : "Question Example 1",
                            "options" : [
                                {
                                    "id" : "OptionId1",
                                    "label" : "Answer Example 1"
                                },
                                {
                                    "id" : "OptionId2",
                                    "label" : "Answer Example 2"
                                },
                                {
                                    "id" : "OptionId3",
                                    "label" : "Answer Example 3"
                                },
                                {
                                    "id" : "OptionId4",
                                    "label" : "Answer Example 4"
                                }
                            ]
                        },
                        {
                            "number" : 2,
                            "text" : "Question Example 2",
                            "options" : [
                                {
                                    "id" : "OptionId1",
                                    "label" : "Answer Example 1"
                                },
                                {
                                    "id" : "OptionId2",
                                    "label" : "Answer Example 2"
                                },
                                {
                                    "id" : "OptionId3",
                                    "label" : "Answer Example 3"
                                },
                                {
                                    "id" : "OptionId4",
                                    "label" : "Answer Example 4"
                                }
                            ]
                        }
                ],
            "paging": {
                "page": 1,
                "size": 10,
                "totalRecords": 2
            }
        }
        
### Create Quiz Answers [POST]

    Insert the quiz answers by passing the studentId and the quizId in the url. It will return 400 if the body in the request is not valid, will return 401 if the user is not authenticated, will return 403 if the user role or the user itself is not appropriate to access this url, and will return 404 if the student or the quiz is not found.

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

                {
                    [
                        {
                            "number" : 1,
                            "optionId" : "OPT0001"
                        },
                        {
                            "number" : 2,
                            "optionId" : "OPT0005"
                        }
                    ]
                }
                
+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                    "point": 100
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "answers" : [
                    {
                        "number" : ["NotNull"],
                        "optionId" : ["NotNull"]
                    }
                ]
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
