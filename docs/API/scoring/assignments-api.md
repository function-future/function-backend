## Scoring - Assignment [/api/scoring/batches/{batchCode}/assignments{?page,size}]

+ Parameters
    + batchCode (string) - specify the batch
    + page (optional, number) - describing the currently displayed page in request
    + size (optional, number) - describing how many data currently displayed on the page in request

### Get Assignment List [GET]

    Get the assignment list for all users. The parameters are search for searching specific assignment by (ID, Title, and Deadline), page for specifying the page number currently showed, and size for specifying how many data currently showed. It will return 401 if the user is not authenticated, and will return 403 if the user role / the user itself is not appropriate to access this url.
    
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "ASG0001",
                    "title" : "Assignment 1",
                    "description" : "Description Number 1",
                    "batchCode" : "3",
                    "file": "http://function-src.com/asdasdasd",
                    "fileId": "file-id",
                    "deadline" : 1565442236338,
                    "uploadedDate" : 1565342236338
                },
                {
                    "id" : "ASG0001",
                    "title" : "Assignment 1",
                    "description" : "Description Number 1",
                    "batchCode" : "3",
                    "file": "http://function-src.com/asdasdasd",
                    "fileId": "file-id",
                    "deadline" : 1565442236338,
                    "uploadedDate" : 1565352236338
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
        
### Create New Assignment [POST]

    Create new assignment by sending the request that consist of header Cookie and body of assignment attributes. Assignment can only be created by admin. It will return 400 if the body of the request is not valid, will return 401 if the user is not authenticated, and will return 403 if the user role is not appropriate to access this url.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

                providing data in form of json that consist of : 
                    {
                        "title" : "Assignment 1",
                        "description" : "Description Number 1",
                        "deadline" : 1565442236338,
                        "files" : ["file-id"]
                    }
            
+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "ASG0001",
                "title" : "Assignment 1",
                "description" : "Description Number 1",
                "file" : "function-static.com/fileName.docx",
                "fileId": "file-id",
                "batchCode" : "3",
                "deadline" : 1565442236338,
                "uploadedDate" : 1565342236338
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "title" : ["notBlank"],
                "deadline" : ["DateFormat"]
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
        
## Scoring - Assignment Copy [/api/scoring/batches/{batchCode}/assignments/copy]

+ Parameters
    + batchCode (string) - specify the batch

### Copy Assignment [POST]

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "batchCode" : "batch-code",
                "assignmentId" : "quiz-id"
            }
        
+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id" : "ASG0001",
                "title" : "Assignment 1",
                "description" : "Description Number 1",
                "file" : "function-static.com/fileName.docx",
                "fileId": "file-id",
                "batchCode" : "batch-code",
                "deadline" : 1565442236338,
                "uploadedDate" : 1565342236338
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "batchCode" : ["NotBlank"],
                "assignmentId" : ["NotBlank"]
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
        
## Scoring - Assignment Detail [/api/scoring/batches/{batchCode}/assignments/{assignmentId}]

+ Parameters
    + batchCode (string) - specify the batch
    + assignmentId (string) - ID of specific assignment

### Get Assignment Detail [GET]

    Get assignment detail by passing the assignment id in the url. It will return 401 if the user is not authenticated, and will return 404 if the assignment is not found.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "ASG0001",
                "title" : "Assignment 1",
                "description" : "Description Number 1",
                "file" : "http://function-static.com/ASG0001/fileName.docx",
                "fileId": "file-id",
                "batchCode" : "3",
                "deadline" : 1565442236338,
                "uploadedDate": 1565342236338
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
        
### Update Assignment Detail [PUT]

    Update the assignment detail by passing the assignment id in the url and sending request that consist of header Cookie and body of assignment attributes. It will return 400 if the body of the request is not valid, will return 401 if the user is not authenticated, will return 403 if the user role is not appropriate to access this url, and will return 404 if the assignment is not found.

+ Request (application/json)

    + Headers
            
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

                    {
                        "title" : "Assignment #1",
                        "description" : "Description Number 1",
                        "deadline" : 1565442236338,
                        "files" : ["file-id"]
                    }
            
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "ASG0001",
                "title" : "Assignment #1",
                "description" : "Description Number 1",
                "file" : "http://function-static.com/ASG0001/fileName.docx",
                "fileId": "file-id",
                "batchCode" : "3",
                "deadline" : 1565442236338,
                "uploadedDate": 1565342236338
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
        
### Delete Assignment [DELETE]
    
    Delete the assignment by passing the assignment id in the url. It will return 401 if the user is not authenticated, and will return 403 if the user role is not appropriate to access this url.

+ Request
    
    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
        }
        
## Scoring - Assignment Room [/api/scoring/batches/{batchCode}/assignments/{assignmentId}/rooms{?page,size}]

+ Parameters
    + assignmentId (string) - specify the assignment
    + page (number) - specify the current page
    + size (number) - specify how many data currently showed in the page

### Get Assignment Rooms [GET]

    Get list of rooms of specific assignment by passing the assignmentId for specifying the assignment, page for specifying the current page, and size for specifying how many data should be displayed int the url. It will return 401 if the user is not authenticated, and will return 403 if the user role / user itself is not appropriate to access this url.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "ROM0001",
                    "point" : 80,
                    "student" : {
                        "id": "SDT00001",
                        "name": "Oliver Sebastian",
                        "phone": "+6285774263075",
                        "role": "STUDENT",
                        "address": "Tangerang",
                        "email": "oliver@gmail.com",
                        "avatar": "http://function-src.com/asdasd",
                        "avatarId": "avatar-id",
                        "batch": {
                            "code": "3"
                        },
                        "university": "BINUS"
                    },
                    "assignment": {
                        "id" : "ASG0001",
                        "title" : "Assignment 1",
                        "description" : "Description Number 1",
                        "deadline" : 1565442236338,
                        "file" : "http://function-static.com/ASG0001/fileName.docx",
                        "fileId": "file-id",
                        "batchCode" : "3",
                        "uploadedDate": 1565342236338
                    }
                },
                {
                    "id" : "ROM0002",
                    "point" : 90,
                    "student" : {
                        "id": "SDT00001",
                        "name": "Oliver Sebastian",
                        "phone": "+6285774263075",
                        "role": "STUDENT",
                        "address": "Tangerang",
                        "email": "oliver@gmail.com",
                        "avatar": "http://function-src.com/asdasd",
                        "avatarId": "avatar-id",
                        "batch": {
                            "code": "3"
                        },
                        "university": "BINUS"
                    },
                    "assignment": {
                        "id" : "ASG0001",
                        "title" : "Assignment 1",
                        "description" : "Description Number 1",
                        "deadline" : 1565442236338,
                        "file" : "http://function-static.com/ASG0001/fileName.docx",
                        "fileId": "file-id",
                        "batchCode" : "3",
                        "uploadedDate": 1565342236338
                    }
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
        
## Scoring - Assignment Room Detail [/api/scoring/batches/{batchCode}/assignments/{assignmentId}/rooms/{roomId}]

+ Parameters
    + assignmentId (string) - specify the id of assignment
    + roomId (string) - specify the id of room

### Get Assignment Room Detail [GET]

    Get the specific room detail of an assignment by passing the assignmentId and roomId in the url. It will return 401 if the user is not authenticated, and will return 403 if the user role or the user itself is not appropriate to access this url.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "ROM0001",
                "student": {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "avatar-id",
                    "batch": {
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                "point" : 100,
                "assignment": {
                    "id" : "ASG0001",
                    "title" : "Assignment 1",
                    "description" : "Description Number 1",
                    "deadline" : 1565442236338,
                    "file" : "http://function-static.com/ASG0001/fileName.docx",
                    "fileId": "file-id",
                    "batchCode" : "3",
                    "uploadedDate": 1565342236338
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
        
### Update Assignment Room Detail Point [PUT]

    Update the specific room score/point by passing the assignmentId and roomId in the url and sending the request that consist of header Cookie and body that contains roomId and score/point attribute and its value. It will return 400 if the request body is not valid, will return 401 if the user is not authenticated, will return 403 if the user role or the user itself is not appropriate to access this url, and will return 404 if the data is not found

+ Request
    
    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

                {
                    "point" : 300
                }

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "ROM0001",
                "point" : 300,
                "student": {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "batch": {
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                "assignment": {
                    "id" : "ASG0001",
                    "title" : "Assignment 1",
                    "description" : "Description Number 1",
                    "deadline" : 1565442236338,
                    "file" : "http://function-static.com/ASG0001/fileName.docx",
                    "batchCode" : "3",
                    "uploadedDate": 1565342236338
                }
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "point" : ["NotNull"]
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
        
## Scoring - Assignment Room Comments [/api/scoring/batches/{batchCode}/assignments/{assignmentId}/rooms/{roomId}/comments{?page,size}]

+ Parameters

    + batchCode (string) - specify the batch
    + assignmentId (string) - specify the assignment
    + roomId (string) - specify the room
    + page (number, optional) - specify the current page of comments
    + size (number, optional) - specify how many comments should be displayed in the current page

### Get Comments From Specific Room [GET]

    Get all comments from specific room by passing the assignment id and the room id in the url. It will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "CMT00001",
                    "author" : {
                        "id" : "USR00001",
                        "name" : "User 1"
                    },
                    "comment" : "Comment Example 1",
                    "createdAt" : 1500000000
                },
                {
                    "id" : "CMT00002",
                    "user" : {
                        "id" : "USR00001",
                        "name" : "User 1"
                    },
                    "comment" : "Comment Example 2",
                    "createdAt" : 1500000000
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

### Create Comment in Room Detail [POST]

        create new comment for specific room by passing the assignment id and room id in the url, and also sending a request that consist of header COOKIE and body that contains comment text. It will return 400 if the request body is invalid, will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Request

    + Headers
    
                COOKIE:Function-Session=asdasdasd
                
    + Body

                {
                    "comment" : "Comment Example 1"
                }
                
+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "CMT00002",
                "author" : {
                    "id" : "USR00001",
                    "name" : "User 1"
                },
                "comment" : "Comment Example 2",
                "createdAt" : 1500000000
            }
        }
        
+ Response 400 (application/json)

        {
            "code" : 400,
            "status" : "BAD_REQUEST",
            "errors" : {
                "comment" : ["NotBlank"]
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
