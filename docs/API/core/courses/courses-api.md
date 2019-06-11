FORMAT: 1A
HOST: http://function.apiblueprint.org/


## Courses [/api/core/courses{?page,batch}]

+ Parameters
    + page (optional,number) - Indicating number of page in request
    + batch (optional,number) - Batch number

### Get Courses [GET]

Accessible for all users except guest. Get courses for specified batch number (if given) or courses for all batch (if batch number is not given), if valid session; otherwise 401 response is returned; if valid
user; otherwise 403 response is returned; if none found, return empty list.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "courseId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                    "courseTitle": "Course Title",
                    "courseDescription": "Course Description Goes Here",
                    "courseThumbnailUrl": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "courseFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                },
                {
                    "courseId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                    "courseTitle": "Course Title",
                    "courseDescription": "Course Description Goes Here",
                    "courseThumbnailUrl": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "courseFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                }
            ],
            "paging": {
                "currentPage": 1,
                "pageSize": 2,
                "totalRecords": 20,
                "totalPage": 10
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

### Create Course [POST]

Accessible for admin, judge, and mentor. Create course, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned; if valid user; otherwise 403 response is
returned. Request parameters must not be present in request.

+ Request (multipart/form-data)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Parameters
        + data - Provide data in form of json under parameter 'data'
            {
                "courseTitle": "Updated Course Title",
                "courseDescription": "Updated Course Description",
                "batchNumber": 1
            }
        + file - Select the file and place the file under parameter name 'file'; only 1 file is allowed

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "courseId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                "courseTitle": "Updated Course Title",
                "courseDescription": "Updated Course Description",
                "courseThumbnailUrl": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "courseFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "courseTitle": ["NotBlank"],
                "courseDescription": ["NotBlank"],
                "batchNumbers": ["BatchesMustExist", "BatchesMustBeDistinct"]
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

## Course Detail [/api/core/courses/{courseId}]

+ Parameters
    + courseId (string) - Course ID generated to uniquely identify a course

### Get Course Detail [GET]

Accessible for all users except guest. Get course detail based on passed courseId parameter, if valid session; otherwise 401 response is returned; if valid user; otherwise 403 response is returned; if available; 
otherwise 404 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "courseId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                "courseTitle": "Updated Course Title",
                "courseDescription": "Updated Course Description",
                "courseThumbnailUrl": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "courseFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
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
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

### Update Course [PUT]

Accessible for admin, judge, and mentor. Update course, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned; if valid user; otherwise 403 response is
returned; if course exists; otherwise 404 response is returned.

+ Request (multipart/form-data)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Parameters
        + data - Provide data in form of json under parameter 'data'
            {
                "courseTitle": "Updated Course Title",
                "courseDescription": "Updated Course Description",
                "batchNumber": 1
            }
        + file - Select the file and place the file under parameter name 'file'; only 1 file is allowed

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "courseId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                "courseTitle": "Updated Course Title",
                "courseDescription": "Updated Course Description",
                "courseThumbnailUrl": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "courseFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "courseTitle": ["NotBlank"],
                "courseDescription": ["NotBlank"],
                "batchNumbers": ["BatchesMustExist", "BatchesMustBeDistinct"]
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
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

### Delete Course [DELETE]

Accessible for admin, judge, and mentor. Delete course based on passed courseId parameter, if valid session; otherwise 401 response is returned; if valid user; otherwise 403 response is returned; if available;
otherwise 404 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
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
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

## Copy Courses to Batch [/api/core/courses/_copy]

### Copy Courses [POST]

Accessible for admin, judge, and mentor. Copy all course available for selected batch (originBatch) to another selected batch (targetBatch), if valid request; otherwise 400 response is returned; if valid session;
otherwise 401 response is returned; if valid user; otherwise 403 response is returned; if available; otherwise 404 response is returned.

+ Request (application/json)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "originBatch": 2,
                "targetBatch": 3
            }

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED"
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "batchNumbers": ["BatchesMustExist", "BatchesMustBeDistinct"]
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
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }
