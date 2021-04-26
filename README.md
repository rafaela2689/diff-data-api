# diff-data-api

API to compare diff from informed objects.

## Prerequisites

* [Java](https://www.java.com/) 11+
* [Gradle](https://gradle.org/) 5.6.4+


### Running the project

* Build the project and run tests:
```bash
cd <path>/diff_data
./gradlew clean build
```

* Run the project:

```bash
./gradlew bootRun
```


## Requests examples:
* Save diff right:
```
POST: v1/diff/{id}/right

curl --location --request POST 'localhost:8080/v1/diff/1a-2b-3c/right' \
--header 'Content-Type: text/plain' \
--data-raw 'eyAKICAgICJuYW1lIjogIlJhZmFlbGEgQ2F2YWxjYW50ZSBkZSBBcmHDumpvIiwKICAgICJhZ2UiOiAzMSwgCiAgICAiam9iIjogIlNvZnR3YXJlIERldmVsb3BlciIKfQ=='
```

* Save diff left:
```
POST: v1/diff/{id}/left

curl --location --request POST 'localhost:8080/v1/diff/1a-2b-3c/left' \
--header 'Content-Type: text/plain' \
--data-raw 'eyAKICAgICJuYW1lIjogIlJhZmFlbGEgQ2F2YWxjYW50ZSBkZSBBcmHDumpvIiwKICAgICJhZ2UiOiAzMSwgCiAgICAiam9iIjogIlNvZnR3YXJlIERldmVsb3BlciIKfQ=='
```

* Get the compared diff:
```
GET: v1/diff/{id}

Response: { result: "some respons here" }

curl --location --request GET 'localhost:8080/v1/diff/1a-2b-3c' \
--header 'Content-Type: text/plain' \
--data-raw 'eyAKICAgICJuYW1lIjogIlJhZmFlbGEgQ2F2YWxjYW50ZSBkZSBBcmHDumpvIiwKICAgICJhZ2UiOiAzMSwgCiAgICAiam9iIjogIlNvZnR3YXJlIERldmVsb3BlciIKfQ=='
```