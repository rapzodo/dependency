# Dependency Management System

This project is a simple dependency management system written in Java. It allows users to manage software components and their dependencies through a command-line interface.

## Features

- **DEPEND**: Define dependencies between components.
- **INSTALL**: Install components along with their dependencies.
- **REMOVE**: Remove components if they are no longer needed.
- **LIST**: List all installed components.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Getting Started

### Clone the repository

```sh
git clone https://github.com/rapzodo/dependency-management-system.git
cd dependency-management-system
```

### Build the project

```sh
mvn clean install
```

### Run the application

```sh
mvn exec:java -Dexec.mainClass="com.danilo.tests.dependency.Main"
```

## Usage

The application reads commands from the standard input. The following commands are supported:

- `DEPEND <component> <dependency1> <dependency2> ...`
- `INSTALL <component>`
- `REMOVE <component>`
- `LIST`
- `END`

### Example

```sh
DEPEND A B C
INSTALL A
LIST
REMOVE A
END
```

## Project Structure

- `src/main/java/com/danilo/tests/dependency/Main.java`: Entry point of the application.
- `src/main/java/com/danilo/tests/dependency/service/DefaultComponentManagementService.java`: Implementation of the component management service.
- `src/main/java/com/danilo/tests/dependency/parsing/CommandLineParser.java`: Parses command-line input into commands.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
