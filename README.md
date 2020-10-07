Usage - 

1. To build the repository - 

From the repository root, 

1. run `./gradlew build test` for build
2. run `./gradlew bootrun` to start server

Example curl PUT command:

`curl -X PUT -H "Content-Type:application/json" -d '[{"companyName": "GroupGon","jobTitle": "Developer", "description": "Job Description String 1", "skillKeyWords": ["React","Front-End","Node.js","Debugging"], "location": "Bangalore, Karnataka", "expiryTime": "10"}]' http://localhost:8081/job-portal/put-job`

Example curl GET command:

`curl -X GET -G 'http://localhost:8081/job-portal/search-jobs' -d 'skills=debugging' -d 'location=Bangalore'`
