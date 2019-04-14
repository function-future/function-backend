FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Sticky Note [/api/core/sticky-notes]

### Get Sticky Note [GET]

Accessible for all user. Get <b>latest</b> sticky note, if available; otherwise, 404 response is returned.

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "title": "Sticky Note Title",
                "description": "Note description goes here. Length is 
                undetermined.",
                "updatedAt": "2017-08-04T13:32:54Z"
            }
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

### Create Sticky Note [POST]

Accessible for admin only. Create new sticky note and store in database, if valid request, otherwise 400 response is returned; if valid session; otherwise 401 response is returned; if valid admin, otherwise 403
response is returned.

+ Request (application/json)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "title": "Sticky Note Title",
                "description": "Note description goes here. Length is 
                undetermined."
            }
            
+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "title": "Sticky Note Title",
                "description": "Note description goes here. Length is 
                undetermined.",
                "updatedAt": "2017-08-04T13:32:54Z"
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "title": [
                    "NotBlank"
                ],
                "noteDescription": [
                   "NotBlank"
               ]
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
