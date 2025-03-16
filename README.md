# Lost and Found Web Application

## Overview
The **Lost and Found** web application helps users report lost and found items in a centralized system. This application is built using **Spring Boot (backend)**, **jQuery (frontend)** and **Vue.js (frontend)**, with an **H2 in-memory database** for temporary data storage.

## Features
- User authentication (separate login for Users and Admins)
- Report found items with descriptions and dates
- Contact the users who posted the found items, in order to claim them
- Search and filter items based on categoty
- Admin dashboard for statistics
- Admin management of users

## Tech Stack
### Backend:
- **Spring Boot** (Java)
- **Spring Security** (for authentication)
- **Spring Data JPA** (for database interactions)
- **H2 Database** (for development/testing)

### Frontend:
- **jQuery** (for AJAX requests)
- **Vue.js** (Single Page Application - SPA)
- **Bootstrap CSS** (for styling)

## Installation and Setup
### Prerequisites:
- Java 17+
- Maven 3+

### Setup:
1. Clone the repository:
   ```sh
   git clone https://github.com/katerinasperanskaya/LostAndFound.git
   cd LostAndFound
   ```
2. Build and run the backend:
   ```sh
   mvn spring-boot:run
   ```
   This will start the application on `http://localhost:9091`.

3. Log in using:

**Username:** `user`  
**Password:** `user`

or

**Username:** `admin`  
**Password:** `admin`


## API Endpoints
**See Postman API collection:** https://app.getpostman.com/join-team?invite_code=d5334e7d18a4cdcbd5f0212c2ac7b256a438c5ce4e7f67f56ddc3927343986a4&target_code=e854e41bad77689910f376473045a811


## Database Schema
The H2 database schema initializes automatically using `schema.sql`. You can access the database console at:
```
http://localhost:9091/h2-console
```
Use the following credentials:
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** *(leave empty)*

## Future Enhancements
- Implementing HATEOAS
- Adding images to the found items
- Writing tests

## License
This project is submitted as a part of Masters in Applied Software Engineering in TUS, Athlone.
