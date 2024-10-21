# The Coffee House

## Overview

**The Coffee House** is a web application built with Vue.js for the front-end and Spring Boot for the back-end. It provides an intuitive interface for users to browse, rate, and order products from the coffee shop. The system includes JWT authentication, product recommendations, a rating system, and comprehensive order management.

## Upload product video for showing example of app

https://github.com/user-attachments/assets/bc4a72fd-2b13-465d-a728-83f272d03fab

## Features

### 1. **User Authentication**
- JWT-based authentication.
- Token expiration handling with automatic re-login.

### 2. **Product Management**
- Browse products by category.
- Like and rate products.
- Search for products by hashtags.
- View recommended products based on ratings, popularity, and hashtags.

### 3. **Order Management**
- Place orders without logging in.
- Registered users can manage order history and update their profile.
- Points reward system for customers based on the amount spent.

### 4. **Admin Panel**
- Manage product inventory.
- View order history and sales reports.
- Quickly update delivery status with buttons.

### 5. **Real-time Updates**
- WebSocket-based real-time updates for order statuses.

### 6. **Reporting and Analytics**
- Revenue reports by day, week, month, and year.
- Product popularity reports using pie charts (Highcharts).

## Technologies

### **Front-end**
- Vue.js 3
- TypeScript
- Bootstrap 5
- Element Plus
- Highcharts

### **Back-end**
- Java 17
- Spring Boot
- Spring Security (JWT Authentication)
- MySQL Database
- JPA/Hibernate

### **Real-time**
- WebSocket

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/your-username/thecoffeehouse.git
cd thecoffeehouse
```

### 2. Backend Setup
Install Java 17.
Set up MySQL database.
Update the database connection settings in application.properties.

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/thecoffeehouse
spring.datasource.username=your-username
spring.datasource.password=your-password
jwt.secret=NYvcC//BWZ49hPLkFpgK5sObL3tO+/tB481ly3kc/hQ=
jwt.expiration=300000
```

### 3. Front-end Setup
- Navigate to the frontend folder:
  
```bash
cd frontend
```

- Install the required dependencies:
  
```bash
npm install
```

- Start the Vue.js development server:

```bash
npm run serve
```
