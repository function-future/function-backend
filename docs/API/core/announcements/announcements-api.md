FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Announcements [/api/core/announcements{?page,size}]

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
                    "announcementId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                    "announcementTitle": "Announcement 1",
                    "announcementSummary": "Summary goes here. Maximum 70 characters?",
                    "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length.",
                    "announcementFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                    "createdAt": 1555980050616,
                    "updatedAt": 1555980050616
                },
                {
                    "announcementId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                    "announcementTitle": "Announcement 2",
                    "announcementSummary": null,
                    "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length.",
                    "announcementFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                    "createdAt": 1555980050616,
                    "updatedAt": 1555980050616
                },
                {
                    "announcementId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                    "announcementTitle": "Announcement 3",
                    "announcementSummary": "Second summary goes here",
                    "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length.",
                    "announcementFileUrl": null,
                    "createdAt": 1555980050616,
                    "updatedAt": 1555980050616
                },
                {
                    "announcementId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                    "announcementTitle": "Announcement 4",
                    "announcementSummary": null,
                    "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length.",
                    "announcementFileUrl": null,
                    "createdAt": 1555980050616,
                    "updatedAt": 1555980050616
                }
            ],
            "paging": {
                "currentPage": 1,
                "pageSize": 4,
                "totalRecords": 100,
                "totalPage": 25
            }
        }

### Create Announcement [POST]

Accessible for admin, judge, and mentor. Saves new announcement, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned; if valid admin, otherwise 403 response
is returned. Parameters page and size must not be passed in this request.

+ Request (multipart/form-data)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Parameters
        + data - Provide data in form of json under parameter 'data'
            {
                "announcementTitle": "Saved Announcement Title",
                "announcementSummary": "Summary goes here. Maximum 70 characters?",
                "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length."
            }
        + image - Select the file and place the file under parameter name 'image'; only 1 image is allowed

+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "announcementId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                "announcementTitle": "Announcement 1",
                "announcementSummary": "Summary goes here. Maximum 70 characters?",
                "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length.",
                "announcementFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                "createdAt": 1555980050616,
                "updatedAt": 1555980050616
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "announcementTitle": ["NotBlank"],
                "announcementSummary": ["NotBlank","Max"],
                "announcementDescriptionHtml": ["NotBlank"]
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

## Announcement Detail [/api/core/announcements/{announcementId}]

+ Parameters
    + announcementId (string) - Announcement ID generated to uniquely identify an announcement

### Get Announcement Detail [GET]

Accessible for all users. Get an announcement for all users, if available; otherwise, 404 response is returned.
        
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "announcementId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                "announcementTitle": "Announcement 1",
                "announcementSummary": "Summary goes here. Maximum 70 characters?",
                "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length.",
                "announcementFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                "createdAt": 1555980050616,
                "updatedAt": 1555980050616
            }
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

### Update Announcement [PUT]

Accessible for admin, judge, and mentor. Saves existing announcement, if valid request, otherwise 400 response is returned; if valid session; otherwise 401 response is returned; if valid user; otherwise 403
response is returned; if available, otherwise 404 response is returned. Parameters page and size must not be passed in this request.

+ Request (multipart/form-data)

    + Headers
    
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
    
    + Parameters
        + data - Provide data in form of json under parameter 'data'
            {
                "announcementTitle": "Saved Announcement Title",
                "announcementSummary": "Summary goes here. Maximum 70 characters?",
                "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length."
            }
        + image - Select the file and place the file under parameter name 'image'; only 1 image is allowed

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "announcementId": "f532e5f8-1036-42cd-8f22-d10fd7fd6bb2",
                "announcementTitle": "Announcement 1",
                "announcementSummary": "Summary goes here. Maximum 70 characters?",
                "announcementDescriptionHtml": "Description goes here. Currently there is no limit to description length.",
                "announcementFileUrl": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                "createdAt": 1555980050616,
                "updatedAt": 1555980050616
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "announcementTitle": ["NotBlank"],
                "announcementSummary": ["NotBlank","Max"],
                "announcementDescriptionHtml": ["NotBlank"]
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "FAIL",
            "data": {
                "errorCode": "ERR_UNAUTHORIZED",
                "errorMessage": "Unauthorized access to resource/operation"
            }
        }

+ Response 403 (application/json)

        {
            "code": 403,
            "status": "FAIL",
            "data": {
                "errorCode": "ERR_FORBIDDEN",
                "errorMessage": "Forbidden access to resource/operation"
            }
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "FAIL",
            "data": {
                "errorCode": "ERR_NOT_FOUND",
                "errorMessage": "Requested data not found"
            }
        }

### Delete Announcement [DELETE]

Accessible by admin, judge, and mentor. Deletes an announcement based on announcement ID located from URL; if valid session; otherwise 401 response is returned; if valid user, otherwise 403 response is returned;
if available; otherwise 404 response is returned.

+ Request (application/octet-stream)

    + Headers
            
            Cookie: SESSION=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

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
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }