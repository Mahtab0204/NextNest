# NextNest - Apartment Finder Platform

NextNest is a modern real-estate platform that allows users to rent, buy, and manage apartments across different locations.

The platform supports:

* Apartment listing management
* Property search and filtering
* Featured property promotion
* Owner dashboard
* Admin dashboard
* Property approval workflow
* Image management
* JWT Authentication
* Role-based access control
* Location-based apartment discovery

---

## Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* MySQL
* Maven

### Frontend

* React.js
* React Router
* Axios
* Bootstrap 5
* React Toastify

---

## User Roles

### Customer

* Search apartments
* View apartment details
* Filter properties
* Save recently viewed properties

### Owner

* Create apartment listings
* Upload apartment images
* Manage properties
* Promote apartments as Featured
* View owner dashboard

### Admin

* Manage users
* Manage owners
* Approve or reject apartment listings
* View platform statistics

---

## Main Features

### Apartment Management

* Create apartment
* Edit apartment
* Delete apartment
* Upload multiple images
* Set cover image

### Search & Filtering

* Search by title
* Search by location
* Filter by price
* Filter by bedrooms
* Filter by purpose

### Featured Property System

* 7 Days Promotion
* 15 Days Promotion
* 30 Days Promotion

Featured properties appear before normal properties throughout the platform.

### Admin Approval System

Every newly created apartment requires approval before becoming visible to customers.

---

## Project Structure

NextNest
├── nextnest-backend
└── nextnest-frontend

---

## Installation

### Backend

```bash
cd nextnest-backend
mvn clean install
mvn spring-boot:run
```

### Frontend

```bash
cd nextnest-frontend
npm install
npm start
```

---

## Future Improvements

* bKash Payment Integration
* Nagad Payment Integration
* SSLCommerz Integration
* Property Analytics
* Owner Subscription Packages
* Email Notifications
* SMS Notifications

---

## Author

Developed by Kazi Mahtab
