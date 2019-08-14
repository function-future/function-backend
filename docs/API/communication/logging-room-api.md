### communication - Logging Rooms [/api/communication/logging-rooms/{?search, page, size}]

+ Parameters
    + search (optional, string) - search parameter for searching spesific data in request
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request


## Get getLoggingRoomByMemberLogin [GET]

    get logging Rooms

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
            "code": 200,
              "status": "OK",
              "data": [
                {
                  "id": "5d4bd71fb3c96d1d80a1df9d",
                  "title": "Logging Room",
                  "description": "logging roommmmm",
                  "members": [
                    {
                      "id": "5d4b956db3c96d3f28649677",
                      "name": "Priagung Satyagama",
                      "avatar": "http://localhost:8080/api/core/resources/user/64908f4c-90e0-4d63-87d5-2716dc9f8cb5-thumbnail.jpg",
                      "role": "STUDENT",
                      "university": "Institut Teknologi Bandung",
                      "batchName": "Future 3"
                    },
                    {
                      "id": "5d4a89e4c299ea036cfa6f80",
                      "name": "Karnando Sepryan",
                      "avatar": "http://localhost:8080/api/core/resources/user/2c688130-1611-4db9-9cbc-df8a98102e9f-thumbnail.jpg",
                      "role": "STUDENT",
                      "university": "BINUS",
                      "batchName": "Future 4"
                    },
                    {
                      "id": "5d4a8a08c299ea036cfa6f81",
                      "name": "Jonathan",
                      "avatar": "http://localhost:8080/api/core/resources/user/d0b19498-a67f-432d-8229-592b1287f5c5-thumbnail.jpg",
                      "role": "STUDENT",
                      "university": "BINUS",
                      "batchName": "Future 3"
                    },
                    {
                      "id": "5d53bdec04fa850eb0fa69c0",
                      "name": "Ricky",
                      "avatar": "http://localhost:8080/api/core/resources/user/84a41e9d-8442-4c85-a23f-56b7c7a5def6-thumbnail.jpg",
                      "role": "STUDENT",
                      "university": "Institut Teknologi Bandung",
                      "batchName": "Future 3"
                    }
                  ]
                }
              ],
              "paging": {
                "page": 1,
                "size": 10,
                "totalRecords": 1
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

## POST create Logging Room [POST]

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title": "Logging Room",
                "description": "lorem ipsum",
                "members": [
                    "5d4b956db3c96d3f28649677",
                    "5d4a89e4c299ea036cfa6f80",
                    "5d4a8a08c299ea036cfa6f81",
                    "5d53bdec04fa850eb0fa69c0"
                ]
            }
            

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
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

### communication - Logging Room Detail [/api/communication/logging-rooms/{loggingRoomId}]

+ Parameters
    + loggingRoomId ( string ) - retrieve logging room with specific id same as loggingRoomId

## GET get logging room

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
            "code": 200,
            "status": "OK",
            "data": {
                "id": "5d4bd71fb3c96d1d80a1df9d",
                "title": "Logging Room",
                "description": "logging roommmmm",
                "members": [
                    {
                        "id": "5d4b956db3c96d3f28649677",
                        "name": "Priagung Satyagama",
                        "avatar": "http://localhost:8080/api/core/resources/user/64908f4c-90e0-4d63-87d5-2716dc9f8cb5-thumbnail.jpg",
                        "role": "STUDENT",
                        "university": "Institut Teknologi Bandung",
                        "batchName": "Future 3"
                    },
                    {
                        "id": "5d4a89e4c299ea036cfa6f80",
                        "name": "Karnando Sepryan",
                        "avatar": "http://localhost:8080/api/core/resources/user/2c688130-1611-4db9-9cbc-df8a98102e9f-thumbnail.jpg",
                        "role": "STUDENT",
                        "university": "BINUS",
                        "batchName": "Future 4"
                    },
                    {
                        "id": "5d4a8a08c299ea036cfa6f81",
                        "name": "Jonathan",
                        "avatar": "http://localhost:8080/api/core/resources/user/d0b19498-a67f-432d-8229-592b1287f5c5-thumbnail.jpg",
                        "role": "STUDENT",
                        "university": "BINUS",
                        "batchName": "Future 3"
                    },
                    {
                        "id": "5d53bdec04fa850eb0fa69c0",
                        "name": "Ricky",
                        "avatar": "http://localhost:8080/api/core/resources/user/84a41e9d-8442-4c85-a23f-56b7c7a5def6-thumbnail.jpg",
                        "role": "STUDENT",
                        "university": "Institut Teknologi Bandung",
                        "batchName": "Future 3"
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


## PUT update Logging Room

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title": "Logging Room",
                "description": "lorem ipsum",
                "members": [
                    "5d4b956db3c96d3f28649677",
                    "5d4a89e4c299ea036cfa6f80",
                    "5d4a8a08c299ea036cfa6f81",
                    "5d53bdec04fa850eb0fa69c0"
                ]
            }
            

+ Response 200 (application/json)

        {
              "code": 200,
              "status": "OK",
              "data": {
                "id": "5d4bd71fb3c96d1d80a1df9d",
                "title": "Logging Room",
                "description": "lorem ipsum",
                "members": [
                  {
                    "id": "5d4b956db3c96d3f28649677",
                    "name": "Priagung Satyagama",
                    "avatar": "http://localhost:8080/api/core/resources/user/64908f4c-90e0-4d63-87d5-2716dc9f8cb5-thumbnail.jpg",
                    "role": "STUDENT",
                    "university": "Institut Teknologi Bandung",
                    "batchName": "Future 3"
                  },
                  {
                    "id": "5d4a89e4c299ea036cfa6f80",
                    "name": "Karnando Sepryan",
                    "avatar": "http://localhost:8080/api/core/resources/user/2c688130-1611-4db9-9cbc-df8a98102e9f-thumbnail.jpg",
                    "role": "STUDENT",
                    "university": "BINUS",
                    "batchName": "Future 4"
                  },
                  {
                    "id": "5d4a8a08c299ea036cfa6f81",
                    "name": "Jonathan",
                    "avatar": "http://localhost:8080/api/core/resources/user/d0b19498-a67f-432d-8229-592b1287f5c5-thumbnail.jpg",
                    "role": "STUDENT",
                    "university": "BINUS",
                    "batchName": "Future 3"
                  },
                  {
                    "id": "5d53bdec04fa850eb0fa69c0",
                    "name": "Ricky",
                    "avatar": "http://localhost:8080/api/core/resources/user/84a41e9d-8442-4c85-a23f-56b7c7a5def6-thumbnail.jpg",
                    "role": "STUDENT",
                    "university": "Institut Teknologi Bandung",
                    "batchName": "Future 3"
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


## DELETE delete logging room

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
        
### communication - Logging Room Topics [/api/communication/logging-rooms/{loggingRoomId}/topics{?page, size}]

+ Parameters
    + loggingRoomId ( string ) - retrieve logging room with specific id same as loggingRoomId
    + page (number) - describing the page currently displayed in request
    + size (number) - describing how many data is currently displayed within the page in request


## Get get Logging Room Topic [GET]

    get topic from specific logging room

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
            "code": 200,
              "status": "OK",
              "data": [
                {
                  "id": "5d4bd887b3c96d23e42801fc",
                  "title": "Testtttt ?"
                },
                {
                  "id": "5d4bf8e1a8e20e27d2a89a22",
                  "title": "TOPIC 2"
                },
                {
                  "id": "5d53ba9c04fa8537d8f51e7b",
                  "title": "what have you done this week?"
                }
              ],
              "paging": {
                "page": 1,
                "size": 10,
                "totalRecords": 3
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

## POST create topic [POST]

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title": "Logging Room"
            }
            

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
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

### communication - Logging Room Topic [/api/communication/logging-rooms/{loggingRoomId}/topics/{topicId}]

+ Parameters
    + loggingRoomId ( string ) - retrieve logging room with specific id same as loggingRoomId
    + topicId ( string ) -  retrieve topic with specific id same as topicId
    
## GET get topic

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
              "code": 200,
              "status": "OK",
              "data": {
                "id": "5d53ba9c04fa8537d8f51e7b",
                "title": "what have you done this week?"
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


## PUT update Logging Room

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title": "test"
            }
            

+ Response 200 (application/json)

        {
          "code": 200,
          "status": "OK",
          "data": {
            "id": "5d53ba9c04fa8537d8f51e7b",
            "title": "test"
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


## DELETE delete topic

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

### communication - Log messages [/api/communication/logging-rooms/{loggingRoomId}/topics/{topicId}/log-messages]

+ Parameters
    + loggingRoomId ( string ) - retrieve logging room with specific id same as loggingRoomId
    + topicId ( string ) -  retrieve topic with specific id same as topicId
    

## Get get log messages from base on topic [GET]

    get log messages

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2


+ Response 200 (application/json)

        {
            "code": 200,
              "status": "OK",
              "data": [
                {
                  "id": "5d53c2c704fa850eb0fa69c2",
                  "text": "test test",
                  "createdAt": 1565770439880,
                  "senderName": "Ricky",
                  "senderAvatar": "http://localhost:8080/api/core/resources/user/84a41e9d-8442-4c85-a23f-56b7c7a5def6-thumbnail.jpg"
                },
                {
                  "id": "5d53c27504fa850eb0fa69c1",
                  "text": "Abcdef",
                  "createdAt": 1565770357236,
                  "senderName": "Ricky",
                  "senderAvatar": "http://localhost:8080/api/core/resources/user/84a41e9d-8442-4c85-a23f-56b7c7a5def6-thumbnail.jpg"
                }
              ],
              "paging": {
                "page": 1,
                "size": 10,
                "totalRecords": 2
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

## POST create log messages [POST]

+ Request

    + Headers

            Cookie: Function-Session=f532e5f8-1036-42cd-8f22-d10fd7fd6bb2

    + Body

            {
                "title": "i done something"
            }
            

+ Response 201 (application/json)

        {
            "code" : 201,
            "status" : "CREATED",
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
