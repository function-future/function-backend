## Scoring - Question Bank [/api/scoring/question-banks{?page,size}]

+ Parameters
    + page (optional, number) - describing the currently displayed page in request
    + size (optional, number) - describing how many data currently displayed on the page in request

### Get Question Bank List [GET]

        Get all question bank list. The question bank list can be modified by parameters. Those parameters are filter for filtering the question bank list, sort for sorting the question bank list, search for searching a specific question bank list, page for specifying the page number currently showed, and size for specifying how many data is showed

+ Response 200 (application/json)

        {
            "code" : 200,
            "satatus" : "OK",
            "data" : [
                {
                    "id" : "QNK0001",
                    "title" : "Question Bank 1"
                    "description" : "Question Bank Number 1"
                },
                {
                    "id" : "QNK0002",
                    "title" : "Question Bank 2",
                    "description" : "Question Bank Number 2"
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
        
### Create Question Bank [POST]

    Create new question bank by sending the request that consist of header Cookie and body that contains question bank attributes. It will return 400 if the request body is not valid, will return 401 if the user is not authenticated, and will return 403 if the user role is not appropriate to access this url.

+ Request (application/json)

    + Headers
            
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "title" : "Question Bank #2",
                "description" : "Question Bank Number 2"
            }
            
+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "QNK0001",
                "title" : "Question Bank #2",
                "description" : "Question Bank Number 2"
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "id" : ["NotBlank"],
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
        
## Scoring - Question Bank Detail [/api/scoring/question-banks/{id}]

+ Parameters
    + id - describing the id of specific question bank

### Get Question Bank Detail [GET]

    Get specific question bank detail by passing question bank id in the url. It will return 401 if the user is not authenticated, will return 403 if the user role is not appropriate to access this url, and will return 404 if the data is not found.

+ Request

    + Headers
                
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "QNK0001",
                "title" : "Question Bank #2",
                "description" : "Question Bank Number 2"
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
        
### Update Question Bank [PUT]

    Update specific question bank by passing the question bank id in the url and sending the request that consist of header Cookie and body that includes question bank attributes. It will return 400 if the request body is invalid, will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

                {
                    "title" : "Question Bank #2",
                    "description" : "Question Bank Number 2"
                }
                
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                    "id" : "QNK0001",
                    "title" : "Question Bank #2",
                    "description" : "Question Bank Number 2"
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "title" : ["NotNull"]
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
        
### Delete Question Bank [DELETE]

    Delete specific question bank by passing the question bank id in the url. It will return 401 if the user is not authenticated and will return 403 if the user role / the user itself is not appropriate to access this url.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
        }
        
## Scoring - Question Bank Questions [/api/scoring/question-banks/{bankId}/questions]

+ Parameters
    + bankId (string) - specify the question bank with id

### Get All Questions from Question Bank [GET]

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                        "id" : "QST0001",
                        "label" : "Question Sample 1",
                        "options" : [
                            {
                                "id" : "OPT0001",
                                "label" : "Answer Sample 1-1"
                            },
                            {
                                "id" : "OPT0002",
                                "label" : "Answer Sample 1-2"
                            },
                            {
                                "id" : "OPT0003",
                                "label" : "Answer Sample 1-3"
                            },
                            {
                                "id" : "OPT0004",
                                "label" : "Answer Sample 1-4",
                                "correct" : true
                            }
                        ]
                    }
            ],
            "paging" : {
                "page" : 1,
                "size" : 10,
                "totalRecords" : 1
            }
        }
        
### Create New Question to Question Bank [POST]

    Create new question to be inserted into specific question bank by passing the question bank id in the url and sending the request that consist of header Cookie and body that is question attribute. It will return 400 if the request is not valid, will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Request

    + Headers
                
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

                {
                    "label" : "Question Example 1",
                    "options" : [
                        {
                            "label" : "Answer Example 1-1"
                        },
                        {
                            "label" : "Answer Example 1-2"
                        },
                        {
                            "label" : "Answer Example 1-3",
                            "correct" : true
                        },
                        {
                            "label" : "Answer Example 1-4"
                        }
                    ]
                }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                                "id" : "QST0001",
                                "label" : "Question Sample 1",
                                "options" : [
                                    {
                                        "id" : "OPT0001",
                                        "label" : "Answer Sample 1-1"
                                    },
                                    {
                                        "id" : "OPT0002",
                                        "label" : "Answer Sample 1-2"
                                    },
                                    {
                                        "id" : "OPT0003",
                                        "label" : "Answer Sample 1-3",
                                        "correct" : true
                                    },
                                    {
                                        "id" : "OPT0004",
                                        "label" : "Answer Sample 1-4"
                                    }
                            ]
                    }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "label" : ["Alphanumeric"],
                "options": ["Size", "NotEmpty"]
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
        
## Scoring - Question Detail [/api/scoring/question-banks/{bankId}/questions/{id}]

### Get Question Detail of Question Bank [GET]

    Get specific question from specifc question bank by passing the question bank id and question id in the url. It will return 401 if the user is not authenticated, and will return 404 if the data is not found

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "QST0001",
                "label" : "Question Example 1",
                "options" : [
                    {
                        "id" : "OPT0001",
                        "label" : "Answer Example 1-1"
                    },
                    {
                        "id" : "OPT0002",
                        "label" : "Answer Example 1-2"
                    },
                    {
                        "id" : "OPT0003",
                        "label" : "Answer Example 1-3",
                        "correct" : true
                    },
                    {
                        "id" : "OPT0004",
                        "label" : "Answer Example 1-4"
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
            "code" : 401,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }
        
### Update Question of Question Bank [PUT]

    Update specific question from question bank by passing the question bank id and question id in the url and sending the request that consist of header Cookie and body of question attributes. It will return 400 if the request body is not valid, will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Request
    
    + Headers
                
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

                {
                    "id" : "QST0001",
                    "label" : "Question Example 33",
                    "options" : [
                        {
                            "id" : "OPT0001",
                            "label" : "Answer Example 1-1"
                        },
                        {
                            "id" : "OPT0002",
                            "label" : "Answer Example 1-2"
                        },
                        {
                            "id" : "OPT0003",
                            "label" : "Answer Example 1-3",
                            "correct" : true
                        },
                        {
                            "id" : "OPT0004",
                            "label" : "Answer Example 1-4"
                        }
                    ]
                }
                
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "QST0001",
                "label" : "Question Example 1",
                "options" : [
                    {
                        "id" : "OPT0001",
                        "label" : "Answer Example 1-1"
                    },
                    {
                        "id" : "OPT0002",
                        "label" : "Answer Example 1-2"
                    },
                    {
                        "id" : "OPT0003",
                        "label" : "Answer Example 1-3",
                        "correct" : true
                    },
                    {
                        "id" : "OPT0004",
                        "label" : "Answer Example 1-4"
                    }
                ]
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "label" : ["NotBlank"]
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
        
### Delete Question of Question Bank [DELETE]

    Delete specific question from question bank by passing the question bank id and question id in the url. It will return 401 if the user is not authenticated and will return 403 if the user role or the user itself is not appropriate to access this url.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
        }
