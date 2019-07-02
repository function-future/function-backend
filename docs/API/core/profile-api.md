FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Profile [/api/core/user/profile]

### Get Profile Data [GET]

Accessible for all users except guest. Get profile data for user from database based from received token/session. Note that the data sent is not to be edited as all edit must come only from user with admin role.
If valid session; otherwise 401 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "sample-id",
                "role": "STUDENT",
                "email": "user@user.com",
                "name": "User Name",
                "phone": "088888888888",
                "address": "Jl. Address 1 Address 2",
                "deleted": false,
                "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "batch": {
                    "id": "sample-id",
                    "name": "Batch Name",
                    "code": "3"
                },
                "university": "Bina Nusantara University"
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
