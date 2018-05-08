# Homework for TWINO

## Problem definition
Create a tiny RESTful web service with the following business requirements:
- Application must expose REST API endpoints for the following
functionality:
    - apply for loan (loan amount, term, name, surname and personal id must
be provided)
    - list all approved loans
    - list all approved loans by user
- Service must perform loan application validation according to the following
rules and reject application if:
    - Application comes from blacklisted personal id
    - N application / second are received from a single country
(essentially we want to limit number of loan applications coming
from a country in a given timeframe)

- Service must perform origin country resolution using a web service (you
should choose one) and store country code together with the loan
application. Because network is unreliable and services tend to fail, let&#39;s
agree on default country code - &quot;lv&quot;.

## Technical requirements
You have total control over framework and tools, as long as application is written
in Java. Feel free to write tests in any JVM language.
What gets evaluated
- Conformance to business requirements
- Code quality, including testability
- How easy it is to run and deploy the service (don't make us install Oracle
database please ;)

# Operations
## Run app
mvn spring-boot:run
## H2 console
http://localhost:8080/h2-console/
Driver class: org.h2.Driver
JDBC url: jdbc:h2:mem:testdb
User name: sa
Password: <Empty>
## Requests
Sample of requests could be found in Homework.postman_collections.json

# TODO
- Define caller country
- Add rate limit by country