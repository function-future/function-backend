FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Change Password [/api/core/user/password]

### Update Password [PUT]

Accessible for all users except guest. Update password of user based from received token/session, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "oldPassword": "Old Password",
                "newPassword": "New Password"
            }

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK"
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "oldPassword": ["NotBlank"],
                "newPassword": ["NotBlank"]
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
