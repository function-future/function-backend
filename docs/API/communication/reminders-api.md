## Communication - Reminders [/api/communication/reminders]

### GET All Reminder [GET]
    
    Get all active reminders
    
+ Request
    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "f5af5a85-f57e-4112-9ebe-6ef7b01a1db5"
                    "title" : "Judul Scheduler",
                    "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pulvinar, eros elementum hendrerit ornare",
                    "isRepeatedMonthly" : false,
                    "monthlyDate" : null,
                    "repeatDays" : ["MON", "TUE", "WED", "FRI"],
                    "membersCount" : 24,
                    "time": "12:00"
                },
                {
                    "id" : "f5af5a85-f57e-4112-9ebe-6ef7b01a1db5"
                    "title" : "Judul Scheduler",
                    "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pulvinar, eros elementum hendrerit ornare",
                    "isRepeatedMonthly" : true,
                    "monthlyDate" : 24,
                    "repeatDays" : null,
                    "membersCount" : 24,
                    "time": "12:00"
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

### POST Reminder [POST]

    Create reminder

+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

            {
                "title" : "Judul Scheduler",
                "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pulvinar, eros elementum hendrerit ornare",
                "isRepeatedMonthly" : false,
                "monthlyDate" : null,
                "hour" : 12,
                "minute" : 10,
                "repeatDays" : ["MON", "TUE", "WED", "FRI"],
                "members" : ["63dc9f59-a579-4b69-a80c-a3c48d794f16", "63dc9f59-a579-4b69-a80c-a3c48d794f17", "63dc9f59-a579-4b69-a80c-a3c48d794f15"]
            }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f22",
                "title" : "Judul Scheduler",
                "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pulvinar, eros elementum hendrerit ornare",
                "isRepeatedMonthly" : false,
                "repeatDays" : ["MON", "TUE", "WED", "FRI"],
                "monthlyDate" : null,
                "time": "12:00",
                "members" : [
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch" : 3,
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f14",
                        "name" : "Ricky Kennedy",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch" : 3,
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f13",
                        "name" : "Felix Wimpy W",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
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

## Communication - Reminder Detail [/api/communication/reminders/{reminderId}]

### GET Reminder Detail [GET]
    Detailed information of a specific reminder


+ Parameters
    + reminderId (String) - specify reminder id

+ Request
    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "id" : "f5af5a85-f57e-4112-9ebe-6ef7b01a1db5"
                "title" : "Judul Scheduler",
                "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pulvinar, eros elementum hendrerit ornare",
                "isRepeatedMonthly" : false,
                "monthlyDate" : null,
                "repeatDays" : ["MON", "TUE", "WED", "FRI"],
                "time": "12:00",
                "members" : [
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch" : 3,
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f14",
                        "name" : "Ricky Kennedy",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch" : 3,
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f13",
                        "name" : "Felix Wimpy W",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
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

### PUT Reminder [PUT]

    Update reminder

+ Parameters
    + reminderId (String) - specify reminder id
   
+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2
                
    + Body

            {
                "title" : "Judul Scheduler",
                "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pulvinar, eros elementum hendrerit ornare",
                "isRepeatedMonthly" : false,
                "monthlyDate" : null,
                "hour" : 12,
                "minute" : 10,
                "repeatDays" : ["MON", "TUE", "WED", "FRI"],
                "members" : ["63dc9f59-a579-4b69-a80c-a3c48d794f16", "63dc9f59-a579-4b69-a80c-a3c48d794f17", "63dc9f59-a579-4b69-a80c-a3c48d794f15"]
            }

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
            "data" : {
                "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f22",
                "title" : "Judul Scheduler",
                "description" : "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean pulvinar, eros elementum hendrerit ornare",
                "isRepeatedMonthly" : false,
                "repeatDays" : ["MON", "TUE", "WED", "FRI"],
                "monthlyDate" : null,
                "time": "12:00",
                "members" : [
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f16",
                        "name" : "Priagung Satyagama",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch" : 3,
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f14",
                        "name" : "Ricky Kennedy",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
                        "university" : "Institut Teknologi Bandung",
                        "batch" : 3,
                        "type" : "STUDENT"
                    },
                    {
                        "id" : "63dc9f59-a579-4b69-a80c-a3c48d794f13",
                        "name" : "Felix Wimpy W",
                        "picture" : "https://dummyimage.com/600x400/000/ff0",
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

### DELETE Reminder [DELETE]
    
    Delete specific reminder

+ Parameters
    + reminderId (String) - specify reminder id
    
+ Request

    + Headers
    
                Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK"
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