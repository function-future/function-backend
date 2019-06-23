FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - All Blogs [/api/core/activity-blogs{?page,size,search}]

+ Parameters
    + page (optional,number) - Indicating number of page in request
    + size (optional,number) - Indicating number of items per page in request
    + search (optional,string) - Indicating search query for searching in request

### Get All Activity Blogs [GET]

Accessible for all users. Get activity blogs from all user, if none found, return empty list.

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                }
            ],
            "paging": {
                "page": 1,
                "size": 5,
                "totalRecords": 24
            }
        }

## Core - User's Blogs [/api/core/activity-blogs{?userId,page,size,search}]

+ Parameters
    + userId (string) - ID of user who authors activity blog(s)
    + page (optional,number) - Indicating number of page in request
    + size (optional,number) - Indicating number of items per page in request
    + search (optional,string) - Indicating search query for searching in request

### Get User's Activity Blogs [GET]

Accessible for all users. Get activity blogs from a user based on passed email parameter, if none found, return empty list.

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                },
                {
                    "id": "sample-id",
                    "title": "Activity Blog Title 5",
                    "description": "Description in markdown format goes here",
                    "files": [
                        {
                            "id": "sample-id",
                            "file": {
                                "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                                "thumbnail": null
                            }
                        }
                    ],
                    "author": {
                        "id": "sample-id",
                        "name": "Student 1"
                    }
                }
            ],
            "paging": {
                "page": 1,
                "size": 5,
                "totalRecords": 24
            }
        }

### Create Activity Blog [POST]

Accessible for all users except guest. Create activity blog by a user, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned. Request parameters must not be 
present in request.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "title": "Activity Blog Title",
                "description": "",
                "files": ["sample-id"]
            }
            
+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id": "sample-id",
                "title": "Activity Blog Title 5",
                "description": "Description in markdown format goes here",
                "files": [
                    {
                        "id": "sample-id",
                        "file": {
                            "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                            "thumbnail": null
                        }
                    }
                ],
                "author": {
                    "id": "sample-id",
                    "name": "Student 1"
                }
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "title": ["NotBlank"],
                "description": ["NotBlank"],
                "files": ["FileMustExist"]
            }
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }

## Core - Blog Detail [/api/core/activity-blogs/{activityBlogId}]

+ Parameters
    + activityBlogId (string) - Generated ID for an activity blog

### Get Activity Blog Detail [GET]

Accessible for all users. Get detail of activity blog specified by parameter activityBlogId, if available; otherwise 404 response is returned.

+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "sample-id",
                "title": "Activity Blog Title 5",
                "description": "Description in markdown format goes here",
                "files": [
                    {
                        "id": "sample-id",
                        "file": {
                            "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                            "thumbnail": null
                        }
                    }
                ],
                "author": {
                    "id": "sample-id",
                    "name": "Student 1"
                }
            }
        }
        
+ Response 404 (application/json)

        {
            "code": 404,
            "status": "NOT_FOUND"
        }

### Update Activity Blog [PUT]

Accessible for all users except guest. Update activity blog by a user, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "title": "Activity Blog Title",
                "description": "",
                "files": ["sample-id"]
            }
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "sample-id",
                "title": "Activity Blog Title 5",
                "description": "Description in markdown format goes here",
                "files": [
                    {
                        "id": "sample-id",
                        "file": {
                            "full": "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png",
                            "thumbnail": null
                        }
                    }
                ],
                "author": {
                    "id": "sample-id",
                    "name": "Student 1"
                }
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "title": ["NotBlank"],
                "description": ["NotBlank"],
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

### Delete Activity Blog [DELETE]

Accessible for user who authors the blog. Delete an activity blog based on passed activityBlogId parameter, if valid session; otherwise 401 response is returned.

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
