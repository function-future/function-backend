FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Create Courses for Batch [/api/core/batches/{batchCode}/courses]

+ Parameters
    + batchCode (string) - Code of origin batch

### Get Courses of Batch [GET]

Accessible for all users except guest. Get courses for specified batchCode, if valid session; otherwise 401 response is returned; if none found, return empty list.

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
                    "title": "Course Title",
                    "description": "Course Description Goes Here",
                    "material": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                },
                {
                    "id": "sample-id",
                    "title": "Course Title",
                    "description": "Course Description Goes Here",
                    "material": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                }
            ],
            "paging": {
                "page": 1,
                "size": 2,
                "totalRecords": 20
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }

### Create/'Copy' Courses [POST]

Accessible for admin, judge, and mentor. Copy all course available for targeted batch (batchCode) from another selected batch (origin), if valid request; otherwise 400 response is returned; if valid session;
otherwise 401 response is returned; if available; otherwise 404 response is returned.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "originBatch": "3",
                "courses" : ["sample-id", "sample-id"]
            }

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": [
                {
                    "id": "sample-id",
                    "title": "Course Title",
                    "description": "Course Description Goes Here",
                    "material": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                },
                {
                    "id": "sample-id",
                    "title": "Course Title",
                    "description": "Course Description Goes Here",
                    "material": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                }
            ]
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "targetBatch": ["BatchesMustExist"],
                "courses": ["CoursesMustExist", "CoursesMustBeDistinct", "NotEmpty"]
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

## Core - Course Detail for Batch [/api/core/batches/{batchCode}/courses/{courseId}]

+ Parameters
    + batchCode (string) - Code of current user batch OR selected batch

### Get Course Detail for Batch [GET]

Accessible for all users except guest. Get course detail based on passed courseId parameter, if valid session; otherwise 401 response is returned; if available; otherwise 404 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "sample-id",
                "title": "Course Title",
                "description": "Course Description Goes Here",
                "material": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

### Update Course for Batch [PUT]

Accessible for admin, judge, and mentor. Update course, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned; if course exists; otherwise 404 response is
returned.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "title": "Updated Course Title",
                "description": "Updated Course Description",
                "material": ["sample-id"]
            }

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "sample-id",
                "title": "Course Title",
                "description": "Course Description Goes Here",
                "material": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "title": ["NotBlank"],
                "description": ["NotBlank"],
                "material": ["FileMustExist", "Size"]
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

### Delete Course for Batch [DELETE]

Accessible for admin, judge, and mentor. Delete course based on passed courseId parameter, if valid session; otherwise 401 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK"
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
