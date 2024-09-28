# Pharmacy Inventory Management System

This is a simple pharmacy inventory management system that allows the user to add, update, delete and view the inventory of the pharmacy, such as medicines and equipments, as well as the supplier's information.

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
> Note: On Windows, use ```mvnw.cmd spring-boot:run```.

### Test
To run the unit tests, use the following command:
```bash
mvn test
```

## Usage
1. After running the application, open a web browser and go to [http://localhost:8080](http://localhost:8080) to access the application.
2. You can access different sections of the application through the navigation bar:
    - **Medicines:** View, add, update, and delete medicines.
    - **Equipments:** Manage the inventory of medical equipment.
    - **Suppliers:** Maintain supplier information.
3. Utilise the search bar to quickly find specific medicines or equipment.