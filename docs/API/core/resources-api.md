FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Resources Upload <Unversioned Files> [/api/core/resources{?source}]

+ Parameters
    + source (string) - Source of uploaded file (resource)

### Upload Resource [POST]

+ Request (multipart/form-data)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Parameters
        + name - Name of the file
        + resource - Select the file and place the file under parameter name 'resource'; only 1 file is allowed

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id": "sample-id",
                "name": "File Name",
                "file": {
                    "full": "",
                    "thumbnail": null
                }
            }
        }

## Core - Resources <Unversioned Files> [/api/core/resources/{resourceId}{?source}]

+ Parameters
    + resourceId (string) - ID of resource to be retrieved
    + source (string) - Source of uploaded file (resource)

### Get Resource [GET]

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/octet-stream)
        
+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }
