## Communication - Chatrooms [/api/communication/chatrooms]
### GET List Chatrooms [GET]

    GET chatrooms belongs to authenticated user

+ Request

    + Headers
                
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Parameters
    + type (optional, string) - chatroom type
    + search (optional, string) - search parameter for searching specific data in request
    + page (optional, number) - describing the page currently displayed in request
    + size (optional, number) - describing how many data is currently displayed within the page in request

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "name" : null,
                    "participants" : [
                        {
                            "id" : "5f4d2dfd-6532-44b8-8a9e-34e6507563f0",
                            "name" : "Ricky Kennedy",
                            "avatar" : "https://dummyimage.com/600x400/000/fff"
                        },
                        {
                            "id" : "5f4d2dfd-6532-44b8-8a9e-34e6507563f0",
                            "name" : "Ricky Kennedy",
                            "avatar" : "https://dummyimage.com/600x400/000/fff"
                        }
                    ],
                    "type" : "PRIVATE",
                    "lastMessage" : {
                        "message" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                        "time" : 1558667802,
                        "isSeen" : false
                    }
                },
                 {
                    "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "name" : null,
                    "participants" : [
                        {
                            "id" : "5f4d2dfd-6532-44b8-8a9e-34e6507563f0",
                            "name" : "Ricky Kennedy",
                            "avatar" : "https://dummyimage.com/600x400/000/fff"
                        },
                        {
                            "id" : "5f4d2dfd-6532-44b8-8a9e-34e6507563f0",
                            "name" : "Ricky Kennedy",
                            "avatar" : "https://dummyimage.com/600x400/000/fff"
                        }
                    ],
                    "type" : "PRIVATE",
                    "lastMessage" : {
                        "message" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sit amet sollicitudin justo",
                        "time" : 1558667802,
                        "isSeen" : false
                    }
                },
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 13
            }
        }    

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
       
### POST Create Chatroom [POST /api/communication/chatrooms]
     Create new group chatroom belongs to authenticated user 

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

            {
                "name" : "Tim Future Bandung",
                "members" : ["63dc9f59-a579-4b69-a80c-a3c48d794f16", "63dc9f59-a579-4b69-a80c-a3c48d794f17", "63dc9f59-a579-4b69-a80c-a3c48d794f15"]
            }

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f22",
                "name" : "Tim Future Bandung",
                "type" : "GROUP",
                "members" : [
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f17",
                        "name" : "Ricky Kennedy",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f15",
                        "name" : "Felix Wimpy W",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : null,
                        "batch" : null,
                        "type" : "MENTOR"
                    }
                ]    
            }
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
    
## Communication - Chatroom Detail [/api/communication/chatrooms/{chatroomId}]
### GET Chatroom Details [GET]

    Detailed information of a certain group
    
+ Parameters
    + chatroomId (string) - chatroom id

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "name" : "Tim Future Bandung",
                                "type" : "GROUP",

                "members" : [
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f14",
                        "name" : "Ricky Kennedy",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f13",
                        "name" : "Felix Wimpy W",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : null,
                        "batch" : null,
                        "type" : "MENTOR"
                    }
                ]
            }
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }

### PUT Update Chatroom [PUT]
Update a group chatroom details

+ Parameters
    + chatroomId (string) - specify group room id

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

            {
                "name" : "Tim Future Bandung",
                "members" : ["63dc9f59-a579-4b69-a80c-a3c48d794f16", "63dc9f59-a579-4b69-a80c-a3c48d794f17", "63dc9f59-a579-4b69-a80c-a3c48d794f15"]
            }

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f22",
                "name" : "Tim Future Bandung",
                "members" : [
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f17",
                        "name" : "Ricky Kennedy",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch": {
                            "id": "sample-id",
                            "name": "Batch Name",
                            "code": "3"
                        },
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f15",
                        "name" : "Felix Wimpy W",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : null,
                        "batch" : null,
                        "type" : "MENTOR"
                    }
                ]    
            }
        }
    
+ Response 400 (application/json)

        {
            "code": 400,
            "status": "BAD_REQUEST",
            "errors": {
                "members": ["NotFound"]
            }
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }

## Communication - Chatroom Messages [/api/communication/chatrooms/{chatroomId}/messages]
### GET Messages [GET]

    Get all chat in a chatroom
    
+ Parameters
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request
    + chatroomId (string) - specify group room id or private room id

+ Request
    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id": "63dc9f59-a579-4b69-a80c-a3c48d794f46",
                    "sender" : {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "avatar" : "https://dummyimage.com/600x400/000/fff"
                    },
                    "text" : "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    "time" : 1558667802,
                },
                {
                    "id": "63dc9f59-a579-4b69-a80c-a3c48d794f26",
                    "sender" : {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Ricky Kennedy",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0"
                    },
                    "text" : "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    "time" : 1558667802,
                }
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 13
            }
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }
   
### POST Create Message [POST]

    Get all chat in a chatroom
    
+ Parameters
    + chatroomId (string) - specify group room id or private room id

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

            {
                "message" : "halo semuanya",
            }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED"
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }

## Communication - Chatroom Messages Before Pivot [/api/communication/chatrooms/{chatroomId}/messages/_before]
### Get Messages Before Pivot [GET]
Get all message in a chatroom before pivot
    
+ Parameters
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request
    + chatroomId (string) - specify group room id or private room id
    + messageId (string, required) - specify pivot message

+ Request
    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id": "63dc9f59-a579-4b69-a80c-a3c48d794f46",
                    "sender" : {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "avatar" : "https://dummyimage.com/600x400/000/fff"
                    },
                    "text" : "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    "time" : 1558667802,
                },
                {
                    "id": "63dc9f59-a579-4b69-a80c-a3c48d794f26",
                    "sender" : {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Ricky Kennedy",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0"
                    },
                    "text" : "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    "time" : 1558667802,
                }
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 13
            }
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }
 
## Communication - Chatroom Messages After Pivot [/api/communication/chatrooms/{chatroomId}/messages/_after]
### Get Messages Before Pivot [GET]
Get all message in a chatroom before pivot
    
+ Parameters
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request
    + chatroomId (string) - specify group room id or private room id
    + messageId (string, required) - specify pivot message

+ Request
    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id": "63dc9f59-a579-4b69-a80c-a3c48d794f46",
                    "sender" : {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "avatar" : "https://dummyimage.com/600x400/000/fff"
                    },
                    "text" : "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    "time" : 1558667802,
                },
                {
                    "id": "63dc9f59-a579-4b69-a80c-a3c48d794f26",
                    "sender" : {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Ricky Kennedy",
                        "avatar" : "https://dummyimage.com/600x400/000/ff0"
                    },
                    "text" : "Lorem ipsum dolor sit amet, consectetur adipiscing",
                    "time" : 1558667802,
                }
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 13
            }
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }  

## Communication - Update Chatroom Seen Status [/api/communication/chatrooms/{chatroomId}/messages/{messageId}/_read]
### PUT Update Group Chatroom Seen Status [PUT]
Update seen status of a chatroom

+ Parameters
    + chatroomId (string) - specify group room id or private room id

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
        }

+ Response 401 (application/json)

        {
            "code" : 401,
            "status" : "UNAUTHORIZED"
        }
        
+ Response 403 (application/json)

        {
            "code" : 403,
            "status" : "FORBIDDEN"
        }
        
+ Response 404 (application/json)

        {
            "code" : 404,
            "status" : "NOT_FOUND"
        }