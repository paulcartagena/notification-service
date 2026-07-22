# Notification Service

Microservice for sending email notifications via SendGrid. Built with Java 21, Spring Boot 4, and PostgreSQL.

## Features

- Async email sending via SendGrid
- Email templates for welcome and order status updates
- API Key authentication
- Notification history with status tracking (PENDING, SENT, FAILED)

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Data JPA / Hibernate
- PostgreSQL
- SendGrid
- Maven

## Authentication

All endpoints require an `X-API-Key` header:

```
X-API-Key: your-api-key
```

## Getting Started

### Requirements

- Java 21
- PostgreSQL
- SendGrid account

### Environment Variables

```
DB_NAME=your_database_name
DB_USERNAME=your_database_user
DB_PASSWORD=your_database_password
SENDGRID_API_KEY=your_sendgrid_api_key
SENDGRID_FROM_EMAIL=your_verified_sender_email
API_KEY=your_api_key
```

### Run

```
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

## API Endpoints

### Send Notification

```
POST /notifications
X-API-Key: your-api-key

{
  "recipient": "user@email.com",
  "type": "EMAIL",
  "template": "WELCOME",
  "payload": {
    "name": "Gary"
  }
}
```

### Get Notification by ID

```
GET /notifications/{id}
X-API-Key: your-api-key
```

### Get All Notifications

```
GET /notifications
X-API-Key: your-api-key
```

## Available Templates

### WELCOME

```json
{
  "template": "WELCOME",
  "payload": {
    "name": "Gary"
  }
}
```

### ORDER_STATUS_CHANGED

```json
{
  "template": "ORDER_STATUS_CHANGED",
  "payload": {
    "userName": "Gary",
    "restaurantName": "Pizza Hut",
    "status": "CLOSED",
    "total": "11.84"
  }
}
```

## Live Demo

API deployed on Railway: https://notification-service-production-d4ac.up.railway.app