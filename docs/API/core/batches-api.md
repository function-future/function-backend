FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Batches [/api/core/batches]

### Get List of Batches [GET]

Accessible for admin, judge, and mentor. Get list of batches available in database, if valid session; otherwise 401 response is returned; if none found, return empty list.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "id",
                    "code": "3",
                    "name": "Batch Name"
                },
                {
                    "id": "id",
                    "code": "2",
                    "name": "Batch Name"
                },
                {
                    "id": "id",
                    "code": "1",
                    "name": "Batch Name"
                }
            ],
            "paging": {
                "page": 0,
                "size": 5,
                "totalRecords": 3
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

### Create New Batch [POST]

Accessible for admin only. Create new batch, if valid session; otherwise 401 response is returned.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "code": "3",
                "name": "Batch Name"
            }

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id": "id",
                "code": "3",
                "name": "Batch Name"
            }
        }
        
+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "code": ["UniqueBatchCode","NoSpace","NotBlank"]
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

## Core - Batch Detail [/api/core/batches/{batchId}]

+ Parameters
    + batchId (string) - Id of batch (not code)

### Get Batch Detail [GET]

Accessible for admin only. Get detail of batch based on batchId, if valid session; otherwise 401 response is returned; if available; otherwise 404 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "id",
                "code": "code",
                "name": "Name"
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

### Update Batch [PUT]

Accessible for admin only. Update batch based on batchId, if valid data; otherwise 400 response is returned; if valid session; otherwise 401 response is returned.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "code": "3",
                "name": "Batch Name"
            }

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "id",
                "code": "3",
                "name": "Batch Name"
            }
        }
        
+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "code": ["UniqueBatchCode","NoSpace","NotBlank"]
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

### Delete Batch [DELETE]

Accessible for admin only. Delete batch based on batchId,if valid session; otherwise 401 response is returned.

+ Request (application/json)

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
