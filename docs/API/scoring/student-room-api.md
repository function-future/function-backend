## Scoring - Assignment Student Room [/api/scoring/batches/{batchCode}/assignments/{assignmentId}/students/{studentId}/rooms{?page,size}]

+ Parameters
    + assignmentId (string) - specify the assignment
    + studentId (string) - specify the student by id
    + page (number) - specify the current page
    + size (number) - specify how many data currently showed in the page

### Get Assignment Student Rooms [GET]

    Get list of rooms of specific assignment by passing the assignmentId for specifying the assignment, page for specifying the current page, and size for specifying how many data should be displayed int the url. It will return 401 if the user is not authenticated, and will return 403 if the user role / user itself is not appropriate to access this url.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : [
                {
                    "id" : "ROM0001",
                    "point" : 80,
                    "student" : {
                        "id": "SDT00001",
                        "name": "Oliver Sebastian",
                        "phone": "+6285774263075",
                        "role": "STUDENT",
                        "address": "Tangerang",
                        "email": "oliver@gmail.com",
                        "avatar": "http://function-src.com/asdasd".
                        "batch": {
                            "code": "3"
                        },
                        "university": "BINUS"
                    },
                    "assignment": {
                        "id" : "ASG0001",
                        "title" : "Assignment 1",
                        "description" : "Description Number 1",
                        "deadline" : 1566343378410,
                        "file" : "http://function-static.com/ASG0001/fileName.docx",
                        "fileId": "file-id",
                        "batchCode" : "3"
                    }
                },
                {
                    "id" : "ROM0002",
                    "point" : 90,
                    "student" : {
                        "id": "SDT00001",
                        "name": "Oliver Sebastian",
                        "phone": "+6285774263075",
                        "role": "STUDENT",
                        "address": "Tangerang",
                        "email": "oliver@gmail.com",
                        "avatar": "http://function-src.com/asdasd".
                        "batch": {
                           "code": "3"
                        },
                        "university": "BINUS"
                    },
                    "assignment": {
                        "id" : "ASG0001",
                        "title" : "Assignment 1",
                        "description" : "Description Number 1",
                        "deadline" : 1565343378410,
                        "file" : "http://function-static.com/ASG0001/fileName.docx",
                        "fileId": "file-id",
                        "batchCode" : "3"
                    }
                }
            ],
            "paging" : {
                "page" : 1,
                "size" : 12,
                "totalRecords" : 2
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
