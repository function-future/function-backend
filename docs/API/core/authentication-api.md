FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Login [/api/core/auth]

### Attempt Login [POST]

Accessible for all users. Attempt login for a user based on request he/she sent to SERVER, if valid user; otherwise 401 response is returned.

+ Request (application/json)

    + Body

            {
                "email": "user@user.com",
                "password": "password"
            }

+ Response 200 (application/json)

    + Headers
    
            Cookie: Function-Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "code": 200,
                "status": "OK",
                "data": {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                }
            }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }

### Get Login Status [GET]

Accessible for all users. Check whether the user is logged in or not; if valid user; otherwise 401 response is returned. To be sent everytime a page is loaded in Vue.

+ Request

    + Headers
    
            Cookie: Function-Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
+ Response 200 (application/json)

    + Body

            {
                "code": 200,
                "status": "OK",
                "data": {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
                }
            }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }

### Attempt Logout [DELETE]

Accessible for all users except guest. Log user out of system by invalidating session, if valid token; otherwise 401 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200

        {
            "code": 200,
            "status": "OK"
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
