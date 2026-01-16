[![Fecha límite de revisión de la tarea](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/QPiIKUzC)

# Los Kripto bros

> *Este repositorio se creó como proyecto para la asignatura Programación Orientada a Objetos (OPP)*.


# 🧠 Informe Técnico: Diseño de Clases y Aplicación de POO

Este informe tiene como objetivo describir y analizar el diseño de clases de una aplicación basada en los principios de la **Programación Orientada a Objetos (POO)**.  
Se detallan las decisiones arquitectónicas, los conceptos de POO aplicados, los patrones de diseño implementados y la manera en que se logró una estructura modular, escalable y mantenible.

---

## 📦 Estructura del Proyecto

La aplicación está organizada en diferentes paquetes según la responsabilidad de cada componente:

### `ve.edu.ucab.domain.model`
Contiene las clases principales del dominio, como `User`, que encapsulan los datos y comportamientos fundamentales del negocio. Estas clases modelan entidades del mundo real.

### `ve.edu.ucab.domain.exceptions`
Incluye excepciones personalizadas como `ExistingUserException`, que permiten un manejo de errores específico del dominio, mejorando la claridad del flujo y los mensajes de error.

### `ve.edu.ucab.domain.usecase`
Contiene los casos de uso del sistema, como `RegisterUserUseCase` y `RegisterInput`, que encapsulan la lógica de negocio y la mantienen separada de la interfaz de usuario y del acceso a datos.

### `ve.edu.ucab.infrastructure.repository`
Define las clases encargadas de la persistencia de datos, como `UserRepository`. Este paquete abstrae los detalles de almacenamiento, siguiendo el principio de inversión de dependencias.

### `ve.edu.ucab.presentation.controller`
Contiene los controladores de la interfaz gráfica, como `SignupController`, que manejan los eventos del usuario y delegan la lógica a los casos de uso del dominio.

---

## 🧩 Aplicación de Conceptos de POO

### ✨ Abstracción
La lógica de negocio (casos de uso) y el acceso a datos (repositorios) están separados, ocultando la complejidad de implementación y exponiendo solo interfaces necesarias.

### 🔒 Encapsulamiento
Los atributos de las clases son privados y se accede a ellos a través de getters y setters, protegiendo la integridad del estado de los objetos.

### 🧬 Herencia y Polimorfismo
Interfaces o clases abstractas permiten múltiples implementaciones intercambiables, facilitando la extensibilidad.

### 🗂️ Modularidad
La estructura por paquetes según responsabilidad permite un código más limpio, escalable y fácil de mantener.

---

## 💡 Ejemplo Ilustrativo

- `User` encapsula los datos del usuario y representa una entidad del modelo de dominio.
- `ExistingUserException` se lanza cuando un usuario ya está registrado.
- `RegisterUserUseCase` contiene la lógica para registrar usuarios, utilizando repositorios y excepciones.
- `SignupController` gestiona eventos del formulario y delega la ejecución a los casos de uso.

---

## 🧱 Patrones de Diseño Utilizados

### 🧭 Patrón MVC (Modelo–Vista–Controlador)
- **Modelo:** Clases en `ve.edu.ucab.domain.model` que representan datos y reglas del negocio.
- **Vista:** Archivos FXML (por ejemplo, `login.fxml`) que definen la interfaz gráfica.
- **Controlador:** Clases como `SignupController` que manejan la interacción del usuario y coordinan la lógica.

### 🧼 Principios de Código Limpio

- **Responsabilidad Única:** Cada clase tiene una función clara (ej. `SignupController` se enfoca solo en el formulario).
- **Separación de Responsabilidades:** UI, lógica de negocio y acceso a datos están desacoplados.
- **Nombres Descriptivos:** Clases y métodos con nombres claros y significativos.
- **Manejo de Errores:** Uso de excepciones personalizadas como `ExistingUserException`.
- **Inyección de Dependencias:** Los casos de uso reciben sus dependencias externamente, lo cual mejora testabilidad.

### 🔁 Patrón Singleton
`UserRepository` utiliza el método `getInstance()` para asegurar una única instancia compartida.

### 🎯 Patrón Use Case
La lógica de negocio está encapsulada en clases de casos de uso (`RegisterUserUseCase`), lo que simplifica el controlador y mejora el aislamiento.

### 🏭 Patrón Factory
`UserRepository.getInstance()` actúa como una fábrica (Factory + Singleton), centralizando y controlando la creación de su instancia.

---

El diseño del sistema aplica correctamente los principios de la Programación Orientada a Objetos.  
La estructura modular y la implementación de patrones como **MVC**, **Singleton**, **Factory** y **Use Case** permiten una arquitectura clara, robusta y fácilmente escalable.  
Las decisiones de diseño —como el uso de excepciones personalizadas, la inyección de dependencias y la separación de responsabilidades— aseguran un desarrollo sostenible y facilitan las pruebas, el mantenimiento y la evolución del sistema.

---

> _Diseñado con buenas prácticas, pensado para el cambio y preparado para escalar._

### pequeña configuracion 

Agrega esta linea en el Run config, en VM Options:
  VM Option: --module-path /home/badjavi/Documents/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
  Main Class: ve.edu.ucab.app.App
