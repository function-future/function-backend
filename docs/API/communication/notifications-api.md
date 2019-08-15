## Communication - Notifications [/api/communication/notifications]

### GET Notifications [GET]

    List all notification belong to authentiated user.

+ Request

    + Headers
                
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
            
+ Parameters
    + search (optional, string) - search parameter for searching specific data in request
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request
    
+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "e4bf60ae-e62b-42eb-8fbb-cd3296a92b1e",
                    "targetUser" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "title" : "Judul Notifikasi",
                    "description" : "Lorem ipsum dolor sit amet t, consectetur adipiscing elit. Aenean pulvinar"
                    "createdAt" : 1558667802
                },
                {
                    "id" : "e4bf60ae-e62b-42eb-8fbb-cd3296a92b1e",
                    "targetUser" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                    "title" : "Judul Notifikasi",
                    "description" : "Lorem ipsum dolor sit amet t, consectetur adipiscing elit. Aenean pulvinar"
                    "createdAt" : 1558667802
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
        
### POST Notificiations [POST]
        
    Create notification

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

            {
                "targetUser" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "title" : "Judul Notifikasi",
                "description" : "Lorem ipsum dolor sit amet t, consectetur adipiscing elit. Aenean pulvinar"
            }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "e4bf60ae-e62b-42eb-8fbb-cd3296a92b1e",
                "targetUser" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                "title" : "Judul Notifikasi",
                "description" : "Lorem ipsum dolor sit amet t, consectetur adipiscing elit. Aenean pulvinar"
                "createdAt" : 1558667802
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

## Communication - Count Total Unseen Notifications [/api/communication/notifications/_unseen_total]
### GET Total Unseen Notifications [GET]

    Get total number of unseen notifications.

+ Request

    + Headers
                
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "total": 10
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

## Communication - Update Notification Seen Status [/api/communication/notifications/{notificationId}/_read]
### PUT Update Notification Seen Status [PUT]
Update seen status of a chatroom

+ Parameters
    + notificationId (string) - specify notificationId

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
