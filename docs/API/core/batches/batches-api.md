FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Batch [/api/core/batches]

### Get List of Batches [GET]

Accessible for admin, judge, and mentor. Get list of batches available in database, if valid session; otherwise 401 response is returned; if valid user; otherwise 403 response is returned; if none found, return
empty list.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                3,
                2,
                1
            ]
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

Accessible for admin only. Create new batch (increment is done in server), if valid session; otherwise 401 response is returned; if valid user; otherwise 403 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "batchNumber": 3
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
