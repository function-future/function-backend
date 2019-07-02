FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Access List [/api/core/user/access-list{?url}]

+ Parameters
    + url (optional,string) - URL path from FE. Default value is '' indicating home page.

### Get Access List for FE URL [GET]

Accessible for all users. Sends cookie and URL from FE to get active components (config) for specified FE URL.

+ Request

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "add": true,
            "delete": true
        }
