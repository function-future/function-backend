## Scoring - Final Judging [/api/scoring/batches/{batchCode}/judgings{?page,size}]

+ Parameters
    + page (optional, number) - describing the page currently displayed in request
    + size (optional, number) - describing how many data is currently displayed within the page in request

### Get Final Judging List [GET]

    Get final comparison list that includes certain parameters. Those parameters are filter for filtering the final comparison by Used Date, sort for sorting the final comparison by (id, name, uploaded date, used date), searh for searching certain final comparison by (id and name), page for specifying the page number currently showed, and size for specifying how many data currently showed

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "FNC0001",
                    "name" : "Final Comparison #1",
                    "description" : "Final Comparison of Students",
                    "batchCode": "3",
                    "studentCount" : 4,
                    "uploadedDate" : 1565343685149,
                    "students" : [
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                       },
                       "university": "BINUS"
                    },
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                        },      
                       "university": "BINUS"
                    }              
                    ]
                },
                {
                    "id" : "FNC0002",
                    "name" : "Final Comparison #2",
                    "description" : "Final Comparison of Students",
                    "batchCode": "3",
                    "studentCount" : 3,
                    "uploadedDate" : 15000000000,
                    "students" : [
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                       },
                       "university": "BINUS"
                    },
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                        },      
                       "university": "BINUS"
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
        
### Create Final Judging [POST]

    Create new final comparison by sending the request that consist of header Cookie and body of final comparison attributes. It will return 400 if the request body is not valid, will return 401 if the user is not authenticated, and will return 403 if the user role / the user itself is not appropriate to access this url.

+ Request

    + Headers
                
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

            {
                "name" : "Final Comparison #1",
                "description" : "Final Comparison Decription #1",
                "students" : ["student-id", "student-id-2"]
            }
            
+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "FNC0001",
                "name" : "Final Comparison #1",
                "description" : "Final Comparison Decription #1",
                "batchCode": "3",
                "studentCount": 3,
                "uploadedDate" : 15000000000,
                "students" : [
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                       },
                       "university": "BINUS"
                    },
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                        },      
                       "university": "BINUS"
                    }              
                    ]
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "name" : ["NotBlank"]
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
        
## Scoring - Final Judging Detail [/api/scoring/batches/{batchCode}/judgings/{judgingId}]

+ Parameters
    + batchCode (string) - specify the batch
    + judgingId (string) - id of specific Final Judging

### Get Final Judging Detail [GET]

    Get specific final comparison by passing the final comparison id in the url. It will return 401 if the user is not authenticated, will return 403 if the user role is not appropriate to access this url, and will return 404 if the data is not found.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "FNC0001",
                "name" : "Final Comparison 1",
                "description" : "Final Comparison of Students",
                "batchCode": "3",
                "uploadedDate" : 15000000000,
                "studentCount": 3,
                "students" : [
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                       },
                       "university": "BINUS"
                    },
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                        },      
                       "university": "BINUS"
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
        
### Update Final Judging Detail [PUT]

    Update the specific final comparison by passing the final comparison id in the url and sending a request that consist of header COOKIE:Function-Session=asdasdasd and body contains final comparison new data. It will return 400 if the request is invalid, will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Request (application/json)

    + Headers

            Cookie: Function-Session=asdasdasdasd
                
    + Body

            {
                "id" : "FNC0001",
                "name" : "Final Comparison 1",
                "description" : "Final Comparison of Students",
                "students" : ["student-id", "student-id-2"]
            }
                
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "FNC0001",
                "name" : "Final Comparison 1",
                "description" : "Final Comparison of Students",
                "uploadedAt" : 150000000,
                "batchCode": "3",
                "studentCount": 3,
                "students" : [
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                       },
                       "university": "BINUS"
                    },
                    {
                       "id": "SDT00001",
                       "name": "Oliver Sebastian",
                       "phone": "+6285774263075",
                       "role": "STUDENT",
                       "address": "Tangerang",
                       "email": "oliver@gmail.com",
                       "avatar": "http://function-src.com/asdasd".
                       "batch": {
                         code": "3"
                        },      
                       "university": "BINUS"
                    }              
                    ]
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "name" : ["NotBlank"],
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
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }
        
### Delete Final Judging [DELETE]

    Delete specific final comparison by passing the final comparison id in the url. It will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
        }
        
## Scoring - Final Judging Comparison [/api/scoring/batches/{batchCode}/judgings/{judgingId}/comparisons]

    + Parameters
        + batchCode (string) - specify the batch code
        + judgingId (string) - specify the judging id

### Get Final Judging Detail Comparison [GET]

    Get Final Judging detail comparison of its student. Will return 401 if the user is not authorized, and return 403 if the user is not allowed to access this API

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data" : [
                    {
                        "studentId": "student-id-1",
                        "studentName" : "Student 1",
                        "batchCode" : "3",
                        "university": "Binus University",
                        "avatar": "http://function-src.com/avatar",
                        "point": 100,
                        "totalPoint": 100,
                        "scores" : [
                            {
                                "title" : "Quiz #1",
                                "type": "QUIZ",
                                "point" : 100
                            },
                            {
                                "title" : "Quiz #2",
                                "type": "QUIZ",
                                "point" : 80
                            },
                            {
                                "title" : "Assignment #1",
                                "type": "ASSIGNMENT",
                                "point" : 80
                            }
                        ]
                    },
                    {
                        "studentId": "student-id-2",
                        "studentName" : "Student 2",
                        "batchCode" : "3",
                        "university": "Binus University",
                        "avatar": "http://function-src.com/avatar",
                        "point": 100,
                        "totalPoint": 100,
                        "scores" : [
                            {
                                "title" : "Quiz #1",
                                "type": "QUIZ",
                                "point" : 100
                            },
                            {
                                "title" : "Quiz #2",
                                "type": "QUIZ",
                                "point" : 80
                            },
                            {
                                "title" : "Assignment #1",
                                "type": "ASSIGNMENT",
                                "point" : 80
                            }
                        ]
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
        
### Score All Student Within Final Judging [POST]

    Used to give student final score. Can only be accessed by Judge. Will return 401 if the user is not authorized, and will return 403 if the user role is not appropriate or the user itself is not allowed.

+ Request

    + Headers
    
                COOKIE:SESSION-abcabcabc
                
    + Body

                {
                    "scores" : [
                        {
                            "studentId" : "student-id-1",
                            "score" : 90
                        },
                        {
                            "studentId" : "student-id-2",
                            "score" : 80
                        },
                        {
                            "studentId" : "student-id-3",
                            "score" : 70
                        }
                    ]
                }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : [
                        {
                            "studentId": "student-id-1",
                            "studentName" : "Student 1",
                            "batchCode" : 1,
                            "university": "Binus University",
                            "avatar": "http://function-src.com/avatar",
                            "point": 90,
                            "totalPoint": 90,
                        },
                        {
                            "studentId": "student-id-2",
                            "studentName" : "Student 2",
                            "batchCode" : 1,
                            "university": "Binus University",
                            "avatar": "http://function-src.com/avatar",
                            "point": 80,
                            "totalPoint": 80,
                        },
                        {
                            "studentId": "student-id-3",
                            "studentName" : "Student 3",
                            "batchCode" : 1,
                            "university": "Binus University",
                            "avatar": "http://function-src.com/avatar",
                            "point": 70,
                            "totalPoint": 70,
                        }
                    ]
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "studentId" : ["NotBlank"]
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


## Scoring - Final Judging Students List Within Batch [/api/scoring/batches/{batchCode}/judgings/students{?page,size}]       

    + page (optional, number) - specifying the page number
    + size (optional, number) - specifying the page size
    + batchCode (string) - specifying the batch by code
 
### Get Students Within Batch [GET]

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University",
                    "finalPoint": 100
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University",
                    "finalPoint": 100
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University",
                    finalPoint": 100
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University",
                    "finalPoint": 100
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University",
                    "finalPoint": 100
                },
            ],
            "paging": {
                "page": 1,
                "size": 5,
                "totalRecords": 20
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }

+ Response 403 (application/json)

        {
            "code": 403,
            "status": "FORBIDDEN"
        }

