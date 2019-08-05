# String-Frequency-Manager
String frequency manager - A SpringBoot application(SaaS)


# Implementation
Language - Spring Boot 2.1.6.RELEASE with Java 1.8
DB - MapDB
Unit Tests - Mockito
Logging - slf4j
Builder Tool - Lombok

# Requirements
Construct the SaaS with the following requirements and constraints:
1. Load all available logs for the last 24 hours
2. A periodic logic flow that process new log files
3. Provide an API endpoint,the caller can only send a string of up to 40 characters
4. The API shall return a json in this format: {“response”: “true|false”} If the string appears more than 5 times in the last 24 hours, return {“response”:“false”} Else return {“response”: “true”}
5. The API input is appended to the string of the URL as part of the GET parameter, in following way:http://{domain_name}/isStringValid?string={string}
6. No special authentication is needed to the API endpoint, assuming that it will only be called by another internal system located within the same network.
