FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Sticky Note [/api/core/sticky-notes]

### Get Sticky Note [GET]

Accesible for anyone. Get latest sticky note from database.

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "sample-id",
                    "title": "Sticky Note Title",
                    "description": "Note noteDescription goes here. Length is undetermined.",
                    "updatedAt": 1555333551046
                }
            ],
            "paging": {
                "page": 1,
                "size": 1,
                "totalRecords": 100
            }
        }

### Create Sticky Note [POST]

Accessible for admin only. Create new sticky note and store in database, if valid request, otherwise 400 response is returned; if valid session; otherwise 401 response is returned.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "title": "Sticky Note Title",
                "description": "Note description goes here. Length is undetermined."
            }
            
+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id": "507f1f77bcf86cd799439011",
                "title": "Sticky Note Title",
                "description": "Note description goes here. Length is undetermined.",
                "updatedAt": 1555333551046
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "title": ["NotBlank"],
                "description": ["NotBlank"]
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
