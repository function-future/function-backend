FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Discussion [/api/core/batches/{batchCode}/courses/{courseId}/discussions{?page}]

+ Parameters
    + batchCode (string) - Code of current user batch OR selected batch
    + courseId (string) - Course ID generated to uniquely identify a course
    + page (optional,number) - Indicating number of page in request

### Get Course Discussions [GET]

Accessible for all users except guest. Get discussion data for a course specified by courseId parameter, if valid session; otherwise 401 response is returned; if none found, return empty list.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK"
            "data": [
                {
                    "id" : "sample-id",
                    "author" : {
                        "id" : "sample-id",
                        "name" : "User 1"
                    },
                    "comment" : "Comment Example 1",
                    "createdAt" : 1500000000
                },
                {
                    "id" : "sample-id",
                    "author" : {
                        "id" : "sample-id",
                        "name" : "User 1"
                    },
                    "comment" : "Comment Example 1",
                    "createdAt" : 1500000000
                },
                {
                    "id" : "sample-id",
                    "author" : {
                        "id" : "sample-id",
                        "name" : "User 1"
                    },
                    "comment" : "Comment Example 1",
                    "createdAt" : 1500000000
                },
                {
                    "id" : "sample-id",
                    "author" : {
                        "id" : "sample-id",
                        "name" : "User 1"
                    },
                    "comment" : "Comment Example 1",
                    "createdAt" : 1500000000
                },
            ],
            "paging": {
                "page": 1,
                "size": 4,
                "totalRecords": 20
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }

### Create Discussion [POST]

Accessible for all users except guest. Create a discussion (reply) for a course, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned. Parameter page must not
be passed in this request.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "comment": "Discussion Description Goes Here"
            }

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id" : "sample-id",
                "author" : {
                    "id" : "sample-id",
                    "name" : "User 1"
                },
                "comment" : "Comment Example 1",
                "createdAt" : 1500000000
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "comment": ["NotBlank"]
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
