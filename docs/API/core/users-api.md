FORMAT: 1A
HOST: http://function.apiblueprint.org/

## Core - Users [/api/core/users{?page,role}]

+ Parameters
    + page (optional,number) - Indicating number of page in request
    + role (string) - Selected role to be viewed, options are (case-sensitive): [admin, mentor, judge, student]

### Get Users [GET]

Accessible for admin only. Get data of all users based on specified role, and fetch data based on parameter page, if valid session; otherwise 401 response is returned; if none found, return empty list. Fields
batch and university will appear only when the specified parameter role's value is student.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
            ],
            "paging": {
                "page": 1,
                "size": 5,
                "totalRecords": 20
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

### Create User [POST]

Accessible for admin only. Create new user, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned. Fields batch and university should appear only when the
specified new/existing user role's value is student; parameters page and role must not be passed in this request.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "role": "STUDENT",
                "email": "user@user.com",
                "name": "User Name",
                "phone": "088888888888",
                "address": "Jl. Address 1 Address 2",
                "avatar": ["sample-id"],
                "batch": "sample-id",
                "university": "Bina Nusantara University"
            }
        
+ Response 201 (application/json)

        {
            "code": 201,
            "status": "CREATED",
            "data": {
                "id": "sample-id",
                "role": "STUDENT",
                "email": "user@user.com",
                "name": "User Name",
                "phone": "088888888888",
                "address": "Jl. Address 1 Address 2",
                "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "avatarId": "sample-id",
                "batch": {
                    "id": "sample-id",
                    "name": "Batch Name",
                    "code": "3"
                },
                "university": "Bina Nusantara University"
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "role": ["OnlyStudentCanHaveBatchAndUniversity","NotNull"],
                "email": ["Email","EmailMustBeUnique","NotBlank"],
                "name": ["Name","NotBlank"],
                "phone": ["Phone"],
                "address": ["NotBlank"],
                "avatar": ["FileMustExist", "FileMustBeImage", "Size"],
                "batch": ["BatchMustExist"]
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

## Core - User Detail [/api/core/users/{userId}]

+ Parameters
    + userId (string) - ID of user who is to be viewed his/her detail

### Get User Detail [GET]

Accessible for admin only. Get data of a user based on specified email, if valid session; otherwise 401 response is returned; if available, otherwise 404 response is returned. Fields batch and university will
appear only when the specified user's role value is student.

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
                "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "avatarId": "sample-id",
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

### Update User [PUT]

Accessible for admin only. Update existing user, if valid request; otherwise 400 response is returned; if valid session; otherwise 401 response is returned. Fields batch and university should appear only when the
specified new/existing user role's value is student; parameters page and role must not be passed in this
request.

+ Request (application/json)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
    + Body

            {
                "role": "STUDENT",
                "email": "user@user.com",
                "name": "User Name",
                "phone": "088888888888",
                "address": "Jl. Address 1 Address 2",
                "avatar": ["sample-id"],
                "batch": "sample-id",
                "university": "Bina Nusantara University"
            }
        
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
                "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "avatarId": "sample-id",
                "batch": {
                    "id": "sample-id",
                    "name": "Batch Name",
                    "code": "3"
                },
                "university": "Bina Nusantara University"
            }
        }

+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "role": ["OnlyStudentCanHaveBatchAndUniversity","NotNull"],
                "email": ["Email","EmailMustBeUnique","NotBlank"],
                "name": ["Name","NotBlank"],
                "phone": ["Phone"],
                "address": ["NotBlank"],
                "avatar": ["FileMustExist", "FileMustBeImage", "Size"],
                "batch": ["BatchMustExist"]
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

### Delete User [DELETE]

Accessible for admin only. Delete a user, if valid session; otherwise 401 response is returned.

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

## Core - User Detail [/api/core/users/search{?name}]

+ Parameters
    + name (optional,string) - Part of name to be searched; default value is 
    empty string

### Get Users By Name [GET]

Accessible for authenticated users only. Get data of all users by name, if 
valid session; otherwise 401 response is returned; if none found, return 
empty list. Fields batch and university will appear only when the specified 
parameter role's value is student.

+ Request (application/octet-stream)

    + Headers
    
            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": [
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
                {
                    "id": "sample-id",
                    "role": "STUDENT",
                    "email": "user@user.com",
                    "name": "User Name",
                    "phone": "088888888888",
                    "address": "Jl. Address 1 Address 2",
                    "avatar": "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                    "avatarId": "sample-id",
                    "batch": {
                        "id": "sample-id",
                        "name": "Batch Name",
                        "code": "3"
                    },
                    "university": "Bina Nusantara University"
                },
            ]
        }

+ Response 401 (application/json)

        {
            "code": 401,
            "status": "UNAUTHORIZED"
        }
