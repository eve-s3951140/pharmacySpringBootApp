# Pharmacy Inventory Management System

This is a simple pharmacy inventory management system that allows the user to add, update, delete and view the inventory of the pharmacy, such as medicines and equipments, as well as the supplier's information.

The application is built using Java and Spring Boot, with a web interface created using HTML/CSS and Thymeleaf. The data is stored in an H2 in-memory database, which can be accessed through the H2 database console.

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
  - [Client-Side Technologies](#client-side-technologies)
  - [Server-Side Technologies](#server-side-technologies)
  - [Testing Technologies](#testing-technologies)
- [Run Locally](#run-locally)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Build](#build)
  - [Run](#run)
  - [Testing](#testing)
- [Usage](#usage)
  - [Access the Application](#access-the-application)
  - [Access the H2 Database](#access-the-h2-database)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features
- **Manage Medicines**: Add, update, delete, and view information about medicines in the inventory.
- **Manage Equipment**: Handle inventory for medical equipment, including details like name, warranty, and purchase date.
- **Supplier Information**: Maintain a record of suppliers, including their contact information and associated products.
- **Search Functionality**: Easily search for medicines, equipments, or suppliers by name.
- **User-Friendly Interface**: A simple and intuitive web interface built with HTML/CSS and Thymeleaf.

## Technologies
This project utilizes a combination of the following technologies:

### Client-Side Technologies
- **HTML/CSS**: For creating the web interface.
- **Thymeleaf**: A modern server-side Java template engine for rendering web pages.
- **JavaScript**: For client-side scripting.

### Server-Side Technologies
- **Java**: Programming language for application development.
- **Spring Boot**: Framework for building Java applications.
- **Spring Data JPA**: Part of the Spring Data project that makes it easy to implement JPA-based repositories.
- **Spring Web**: Provides basic web support.
- **Hibernate**: ORM (Object-Relational Mapping) framework for database operations.
- **H2 Database**: In-memory database for development and testing.

### Testing Technologies
- **Maven**: Build automation tool for Java projects.
- **JUnit 5**: Framework for unit testing Java applications.
- **Mockito**: Library for mocking objects in unit tests.

## Run Locally
### Prerequisites
To build and run the application, ensure you have the following installed:
- **Java 17**: Download and install from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://openjdk.java.net/install/).
- **Maven 3.8.3**: Download and install from [Apache Maven](https://maven.apache.org/download.cgi).

### Installation
1. **Clone the repository:**
   ```bash
   git clone https://github.com/eve-s3951140/pharmacySpringBootApp.git
   ```
2. **Change the directory:**
   ```bash
   cd pharmacySpringBootApp
   ```

### Build
To compile and package the application, run the following command:
```bash
mvn clean package
```

### Run
To start the application, execute:
```bash
./mvnw spring-boot:run
```
**Note:** On Windows, use `mvnw.cmd spring-boot:run`.

### Test
To run the unit tests, use the following command:
```bash
mvn test
```

**Notes:**
- The tests are located in the `src/test` directory.
- The tests will not be able to run if the application is running.

## Usage
### Access the Application
1. After running the application, open a web browser and go to [http://localhost:8080](http://localhost:8080) to access the application.
2. You can access different sections of the application through the navigation bar:
   - **Medicines:** View, add, update, and delete medicines.
   - **Equipments:** Manage the inventory of medical equipment.
   - **Suppliers:** Maintain supplier information.
3. Utilise the search bar to quickly find specific medicines or equipment.

### Access the H2 Database
1. To access the H2 database console, go to [http://localhost:8080/h2-console](http://localhost:8080/h2-console).
2. Make sure the following details are set:
   - **Saved Settings:** Generic H2 (Embedded)
   - **Setting Name:** Generic H2 (Embedded)
   - **Driver Class:** `org.h2.Driver`
3. Enter the following details:
   - **JDBC URL:** `jdbc:h2:file:./pharmacydb`
   - **Username:** `sa`
   - **Password:** `password`
4. Click on the **Connect** button to access the database.
5. You can view the tables and run SQL queries to interact with the database.