## Scoring - Student Points [/api/scoring/summary/{studentId}]

+ Parameters
    + studentId (string) - specify student id

### GET Specific Student Point Details [GET]

    Get specific student quizes and assignments by passing the student id in the url. It will return 401 if the user is not authenticated, will return 403 if the user role / the user itself is not appropriate to access this url, and will return 404 if the data is not found.

+ Response 200 (application/json)

        {
            "code" : 200,
            "status" : "OK",
            "data" : {
                "studentId": "student-id",
                "studentName" : "Student 1",
                "batchCode": "3",
                "university" : "University 1",
                "avatar" : "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png",
                "scores" : [
                    {
                        "id" : "QZ0001",
                        "title" : "Quiz 1",
                        "type": "QUIZ",
                        "point" : 80
                    },
                    {
                        "id" : "QZ0002",
                        "title" : "Quiz 2",
                        "type": "QUIZ",
                        "point" : 100
                    },
                    {
                        "id" : "QZ0003",
                        "title" : "Quiz 3",
                        "type": "QUIZ",
                        "point" : 70
                    },
                    {
                        "id" : "ASG0001",
                        "title" : "Assignment 1",
                        "type": "ASSIGNMENT",
                        "point" : 80
                    },
                    {
                        "id" : "ASG0002",
                        "title" : "Assignment 2",
                        "type": "ASSIGNMENT",
                        "point" : 30
                    },
                    {
                        "id" : "ASG0003",
                        "title" : "Assignment 3",
                        "type": "ASSIGNMENT",
                        "point" : 100
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
