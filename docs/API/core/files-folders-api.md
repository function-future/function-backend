FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Files [/api/core/files/{parentId}]

+ Parameters
    + parentId (string) - ID of parent of file, default value is 'root'

### Get Files [GET]

Accessible for only user with specified email. Get files on root/following path directory belonging to user with specified email, if valid session; otherwise 401 response is 
returned; if none found, return empty list.

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
                    "type": "FOLDER",
                    "name": "Name 1",
                    "parentId": "parent-id",
                    "author": {
                        "id": "sample-id",
                        "name": "name"
                    }
                },
                {
                    "id": "id",
                    "type": "FOLDER",
                    "name": "Name 2",
                    "parentId": "parent-id",
                    "author": {
                        "id": "smaple-id",
                        "name": "name"
                    }
                },
                {
                    "id": "id",
                    "type": "FILE",
                    "name": "Name",
                    "file": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                    "versions": {
                        "2": {
                            "timestamp": 1555980050616,
                            "url": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                        },
                        "1": {
                            "timestamp": 1555980050616,
                            "url": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                        }
                    },
                    "parentId": "parent-id"",
                    "author": {
                       "id": "smaple-id",
                       "name": "name"
                    }
                }
            ]
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }

### Create File/Folder [POST]

Accessible for only user with specified email. Create file at the specified directory, if valid request; otherwise 400 response is returned; if valid session; otherwise 401
response is returned.

+ Request (multipart/form-data)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Parameters
        + data - Provide data in form of json under parameter 'data', type is limited to FILE/FOLDER only. For folder, no multipartfile must exist in request
            {
                "name": "File Name",
                "type": "FILE"
            }
        + file - Select the file and place the file under parameter name 'file'; only 1 file is allowed

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id": "id",
                "type": "FILE",
                "name": "File Name",
                "file": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                "versions": {
                    "1": {
                        "timestamp": 1555980050616,
                        "url": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                    }
                },
                "parentId": "parent-id"",
                "author": {
                    "id": "smaple-id",
                    "name": "name"
                }
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "name": ["NotBlank"],
                "type": ["TypeMustExist", "TypeAndBytesMustBeValid"]
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

## Core - File Detail [/api/core/files/{parentId}/{id}{?version}]

+ Parameters
    + parentId (string) - ID of parent of file, default value is 'root'
    + id (string) - ID generated for a file/folder
    + version (optional,string) - Version ID generated for a file on uploading new file or editing existing file (1, 2, etc.)

### Get File/Folder Detail [GET]

Accessible for only user with specified email. Get detail of file (latest version if version is not specified) located on folder with the specified folderId, if valid session; 
otherwise 401 response is returned; if available; otherwise 404 response is returned.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "id",
                "type": "FILE",
                "name": "File Name",
                "file": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                "versions": {
                    "2": {
                        "timestamp": 1555980050616,
                        "url": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                    },
                    "1": {
                        "timestamp": 1555980050616,
                        "url": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                    }
                },
                "parentId": "parent-id"",
                "author": {
                    "id": "smaple-id",
                    "name": "name"
                }
            }
        }

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

### Update File [PUT]

Accessible for only user with specified email. Update file with the specified fileId, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 
response is returned; if available; otherwise 404 response is returned. Parameter version must not be included in request.

+ Request (multipart/form-data)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Parameters
        + data - Provide data in form of json under parameter 'data', type is limited to FILE/FOLDER only. For folder, no multipartfile must exist in request
            {
                "name": "File Name",
                "type": "FILE"
            }
        + file - Select the file and place the file under parameter name 'file'; only 1 file is allowed

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "id",
                "type": "FILE",
                "name": "File Name",
                "file": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                "versions": {
                    "2": {
                        "timestamp": 1555980050616,
                        "url": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                    },
                    "1": {
                        "timestamp": 1555980050616,
                        "url": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png"
                    }
                },
                "parentId": "parent-id"",
                "author": {
                    "id": "smaple-id",
                    "name": "name"
                }
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "name": ["NotBlank"],
                "type": ["TypeMustExist", "TypeAndBytesMustBeValid"]
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

### Delete File [DELETE]

Accessible for only user with specified email. Delete a file (and all version of it) from the folder it is in and removes all sharing access to that file, if valid session; 
otherwise 401 response is returned.

+ Request (application/octet-stream)

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
