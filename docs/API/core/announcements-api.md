FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Announcements [/api/core/announcements{?page,size}]

+ Parameters
    + page (optional,number) - Indicating number of page in request
    + size (optional,number) - Indicating number of items per page in request

### Get Announcements [GET]

Accessible for all user. Get announcements for all users with page data, if none found, return empty list.
        
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "sample-id",
                    "title": "Announcement 1",
                    "summary": "Summary goes here. Maximum 70 characters?",
                    "description": "Description goes here. Currently there is no limit to description length.",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "updatedAt": 1555980050616
                },
                {
                    "id": "sample-id",
                    "title": "Announcement 1",
                    "summary": "Summary goes here. Maximum 70 characters?",
                    "description": "Description goes here. Currently there is no limit to description length.",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "updatedAt": 1555980050616
                },
                {
                    "id": "sample-id",
                    "title": "Announcement 1",
                    "summary": "Summary goes here. Maximum 70 characters?",
                    "description": "Description goes here. Currently there is no limit to description length.",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "updatedAt": 1555980050616
                },
                {
                    "id": "sample-id",
                    "title": "Announcement 1",
                    "summary": "Summary goes here. Maximum 70 characters?",
                    "description": "Description goes here. Currently there is no limit to description length.",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "updatedAt": 1555980050616
                }
            ],
            "paging": {
                "page": 1,
                "size": 4,
                "totalRecords": 100
            }
        }

### Create Announcement [POST]

Accessible for admin, judge, and mentor. Saves new announcement, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned. Parameters page and size must not be 
passed in this request.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "title": "Saved Announcement Title",
                "summary": "Summary goes here. Maximum 70 characters?",
                "description": "Description goes here. Currently there is no limit to description length.",
                "files": ["sample-id"]
            }

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id": "sample-id",
                "title": "Announcement 1",
                "summary": "Summary goes here. Maximum 70 characters?",
                "description": "Description goes here. Currently there is no limit to description length.",
                "files": [
                    {
                        "id": "sample-id",
                        "file": {
                            "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                            "thumbnail": null
                        }
                    }
                ],
                "updatedAt": 1555980050616
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "title": ["NotBlank"],
                "summary": ["Size"],
                "description": ["NotNull"],
                "files": ["FileMustExist"]
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

## Core - Announcement Detail [/api/core/announcements/{announcementId}]

+ Parameters
    + announcementId (string) - Announcement ID generated to uniquely identify an announcement

### Get Announcement Detail [GET]

Accessible for all users. Get an announcement for all users, if available; otherwise, 404 response is returned.
        
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "sample-id",
                "title": "Announcement 1",
                "summary": "Summary goes here. Maximum 70 characters?",
                "description": "Description goes here. Currently there is no limit to description length.",
                "files": [
                    {
                        "id": "sample-id",
                        "file": {
                            "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                            "thumbnail": null
                        }
                    }
                ],
                "updatedAt": 1555980050616
            }
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

### Update Announcement [PUT]

Accessible for admin, judge, and mentor. Saves existing announcement, if valid request, otherwise 400 response is returned; if valid session; otherwise 401 response is returned. Parameters page and size must not
be passed in this request.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Body

            {
                "title": "Saved Announcement Title",
                "summary": "Summary goes here. Maximum 70 characters?",
                "description": "Description goes here. Currently there is no limit to description length."
                "files": ["sample-id"]
            }

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "sample-id",
                "title": "Announcement 1",
                "summary": "Summary goes here. Maximum 70 characters?",
                "description": "Description goes here. Currently there is no limit to description length.",
                "files": [
                    {
                        "id": "sample-id",
                        "file": {
                            "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                            "thumbnail": null
                        }
                    }
                ],
                "updatedAt": 1555980050616
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "title": ["NotBlank"],
                "summary": ["Size"],
                "description": ["NotNull"],
                "files": ["FileMustExist"]
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

### Delete Announcement [DELETE]

Accessible by admin, judge, and mentor. Deletes an announcement based on announcement ID located from URL; if valid session; otherwise 401 response is returned.

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

+ Response 403 (application/json)

        {
            "code": 403,
            "status": "FORBIDDEN"
        }
