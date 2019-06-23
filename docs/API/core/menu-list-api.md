FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Menu List [/api/core/user/menu-list]

### Get Available Menu (Navbar) for User [GET]

Accessible for all users. Get available menu for all type of users.

+ Request

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "courses": true,
            "files": true,
            "users": true,
            "grades": true,
            "chatroom": true
        }
