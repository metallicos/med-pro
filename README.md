# Link to the project in google drive

https://drive.google.com/drive/folders/1yC53Hn-Lpy5uRtOy9c486NrxH9jcrATO?usp=drive_link

# MediCare Pro - Medical Appointment Management System

A comprehensive JavaFX application for managing medical appointments, patients, and doctors.
google drive link for the project : https://drive.google.com/drive/folders/1yC53Hn-Lpy5uRtOy9c486NrxH9jcrATO?usp=drive_link

## Overview

MediCare Pro is a robust medical appointment management system designed to help medical practitioners efficiently manage their patient appointments. The application provides an intuitive interface for managing patients, doctors, and scheduling appointments.

## Features

- **User Authentication**: Secure login system with role-based access control
- **Patient Management**: Add, update, search, and delete patient records
- **Doctor Management**: Maintain doctor information including specialties and license numbers
- **Appointment Scheduling**: Book, reschedule, or cancel appointments
- **Search Functionality**: Search capabilities across patients, doctors, and appointments
- **Notification System**: Sends reminders for upcoming appointments

## Technology Stack

- **Backend**: Java 11
- **Frontend**: JavaFX 11 for the graphical user interface
- **Database**: MySQL for data persistence
- **Build Tool**: Maven
- **Testing**: JUnit 5 and Mockito

## Project Structure

The project follows a layered architecture:

- **Models**: Data structures representing domain objects (Patient, Doctor, Appointment)
- **Data Access Objects (DAO)**: Database interaction layer
- **Services**: Business logic and transaction management
- **Controllers**: UI logic and event handling
- **Views**: FXML-based user interface definitions

## Database Schema

The application uses a MySQL database with the following tables:
- `patients`: Stores patient information
- `medecins`: Stores doctor information
- `rendez_vous`: Stores appointment data
- `users`: Manages user authentication and authorization

## Getting Started

### Prerequisites

- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/metallicos/med-pro.git
   ```
   ou
   ```
   decompresser le fichier zip sur google drive  https://drive.google.com/drive/folders/1yC53Hn-Lpy5uRtOy9c486NrxH9jcrATO?usp=drive_link
   ```

2. Set up the database:
   ```
   mysql -u your_username -p < db_schema.sql
   ```

3. Configure database connection:
   Edit the database connection parameters in the `DatabaseConnectionManager` class if needed.

4. Build the application:
   ```
   mvn clean package
   ```

5. Run the application:
   ```
   mvn javafx:run
   ```

### Default Login

- **Username**: admin
- **Password**: admin123

## Screenshots

(Add screenshots of your application here)

## Development

### Building from Source

```
mvn clean install
```

### Running Tests

```
mvn test
```

## Contributors

- Abdellah Saaidi & Chihab-Eddine Etthamry - Initial work and maintenance
