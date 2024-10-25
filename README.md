# User Aggregation API

## Overview

This is a Spring Boot application that aggregates user data from multiple databases. It provides a single REST endpoint for retrieving user information with filtering capabilities.

## Features

- Aggregate user data from multiple databases (PostgreSQL, MySQL, etc.).
- Support for filtering users by username, name, and surname.
- Declarative configuration using YAML for specifying data sources.

## API Documentation

### Get Aggregated Users

**GET** `/api/v1/users`

Retrieves a list of users aggregated from multiple databases.

#### Query Parameters

- `username` (optional): Filter users by username.
- `name` (optional): Filter users by name.
- `surname` (optional): Filter users by surname.

#### Success Response

- **Status Code**: 200 OK
- **Response Body**:

```json
[
  {
    "id": "example-user-id-1",
    "username": "user-1",
    "name": "User",
    "surname": "Userenko"
  },
  {
    "id": "example-user-id-2",
    "username": "user-2",
    "name": "Testuser",
    "surname": "Testov"
  }
]
```

#### Error Response
- **Status Code**: 400 Bad Request
- **Description**: Invalid request parameters.

## Running the Application

1. Clone the repository.
2. Navigate to the project directory.
3. Set up Postgres, create databases named `primary_db` and `secondary_db` with username `postgres` and password `postgres` (if your credentials are different, insert them into `application.yml` file under databases username/password).
4. Run the application using:

`./gradlew bootRun`

## Running Tests
To run the integration tests, you can use the following command:

`./gradlew test`