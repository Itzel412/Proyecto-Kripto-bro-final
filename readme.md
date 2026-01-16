[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/QPiIKUzC)

# Los Kripto bros

> *This repository is made as a project for the subject Object-Oriented Programming (OPP)*.
>
> Go here to read the Spanish version of the [readme](readme.es.md) (Ven aquí para leer la version en español del [readme](readme.es.md)).

# 🧠 Technical Report: Class Design and OOP Application

This report aims to describe and analyze the class design of an application based on **Object-Oriented Programming (OOP)** principles.  
It outlines the architectural decisions, applied OOP concepts, implemented design patterns, and how a modular, scalable, and maintainable structure was achieved.

---

## 📦 Project Structure

The application is organized into different packages according to the responsibility of each component:

### `ve.edu.ucab.domain.model`
Contains the main domain classes such as `User`, which encapsulate fundamental business data and behavior. These classes model real-world entities.

### `ve.edu.ucab.domain.exceptions`
Includes custom exceptions such as `ExistingUserException`, which enable domain-specific error handling and improve code clarity and flow control.

### `ve.edu.ucab.domain.usecase`
Contains the system’s use cases, such as `RegisterUserUseCase` and `RegisterInput`, which encapsulate business logic separately from the UI and data access layers.

### `ve.edu.ucab.infrastructure.repository`
Defines the classes responsible for data persistence, such as `UserRepository`. This package abstracts storage implementation details following the dependency inversion principle.

### `ve.edu.ucab.presentation.controller`
Contains GUI controllers like `SignupController`, which handle user events and delegate logic to domain use cases.

---

## 🧩 Applied OOP Concepts

### ✨ Abstraction
Achieved by separating business logic (use cases) and data access (repositories), hiding complex implementation and exposing only required interfaces.

### 🔒 Encapsulation
Class fields are private and accessed via getters and setters, protecting the internal state of objects.

### 🧬 Inheritance and Polymorphism
Interfaces or abstract classes (e.g., for repositories) allow for multiple interchangeable implementations, enabling flexibility and extensibility.

### 🗂️ Modularity
The code is organized into packages by responsibility, making it cleaner, easier to maintain, and scalable.

---

## 💡 Illustrative Example

- `User` encapsulates user data and represents a domain model entity.  
- `ExistingUserException` is thrown when a user already exists.  
- `RegisterUserUseCase` contains the registration logic, using the repository and throwing domain-specific exceptions.  
- `SignupController` handles UI events and delegates the operation to the use case.

---

## 🧱 Design Patterns Used

### 🧭 MVC Pattern (Model–View–Controller)
- **Model:** Classes in `ve.edu.ucab.domain.model` represent data and business rules.  
- **View:** FXML files (e.g., `login.fxml`) define the UI layout.  
- **Controller:** Classes like `SignupController` handle user interaction and coordinate logic.

### 🧼 Clean Code Principles

- **Single Responsibility:** Each class has a focused purpose (e.g., `SignupController` only handles signup logic).  
- **Separation of Concerns:** UI, business logic, and data access are decoupled.  
- **Descriptive Naming:** Classes and methods are clearly named according to their role.  
- **Error Handling:** Domain-specific exceptions (e.g., `ExistingUserException`) improve error clarity.  
- **Dependency Injection:** Use cases receive repositories as dependencies, improving flexibility and testability.

### 🔁 Singleton Pattern
The `UserRepository` class uses a `getInstance()` method to ensure a single shared instance across the application.

### 🎯 Use Case Pattern
Business logic is encapsulated in dedicated use case classes (e.g., `RegisterUserUseCase`), simplifying controller responsibilities.

### 🏭 Factory Pattern
The `getInstance()` method in `UserRepository` acts as a Factory (specifically a Singleton Factory), centralizing and abstracting instance creation.

---

The system design demonstrates a solid application of **Object-Oriented Programming** principles.  
The modular structure and use of design patterns like **MVC**, **Singleton**, **Factory**, and **Use Case** contribute to a clean, robust, and scalable architecture.  
Design decisions such as custom exception handling, dependency injection, and separation of responsibilities promote sustainable development and facilitate testing, maintenance, and system evolution.

---

> _Designed with best practices, built for change, and ready to scale._

