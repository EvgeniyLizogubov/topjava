Curl's for MealRestController:

- Get by id:

`curl localhost:8080/topjava/rest/meals/100003`

- Get all:

`curl localhost:8080/topjava/rest/meals`

- Delete by id:

`curl -X DELETE localhost:8080/topjava/rest/meals/100012`

- Create new 
(for Windows instead single-quotes (') need to use double (")
Example: "{ """name""":"""Frodo""", """age""":123 }"
https://stackoverflow.com/a/39787948/18231993):

`curl -X POST -H "Content-Type: application/json" -d '{"dateTime":"2020-01-30T13:05:00","description":"fdg","calories":1000}' 
localhost:8080/topjava/rest/meals`

- Update:

`curl -X PUT -H "Content-Type: application/json" -d "{"""dateTime""":"""2020-01-30T13:05:00""","""description""":"""fdg""","""calories""":555}" 
localhost:8080/topjava/rest/meals/100014`

- Get filtered by time 
(for using '&' in URL put the entire URL inside double quotes
https://stackoverflow.com/questions/13339469/how-to-include-an-character-in-a-bash-curl-statement):

`curl "localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=10%3A00&endTime=10%3A01"`

