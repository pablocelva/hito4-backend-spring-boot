# Ticketera - Core de Dominio Puro

Ticketera es un sistema de venta de entradas para eventos independientes. Este repositorio contiene el **Core de Dominio Puro**, completamente aislado de frameworks, bases de datos o interfaces externas, siguiendo los principios de **Clean Architecture**, **Domain-Driven Design (DDD)** y **Hexagonal Architecture (Ports & Adapters)**.

Repositorio original del **Hito 1** (base de este proyecto): [hito1-ticketera](https://github.com/pablocelva/hito1-ticketera)

## Índice

- [Arquitectura](#arquitectura)
  - [Estructura del directorio](#estructura-del-directorio)
  - [Descripción de archivos](#descripción-de-archivos)
  - [Nota sobre la arquitectura](#nota-sobre-la-arquitectura)
- [Lenguaje Ubicuo](#lenguaje-ubicuo)
- [Contexto Delimitado](#contexto-delimitado)
- [Tecnologías y dependencias](#tecnologías-y-dependencias)
  - [Lenguaje y plataforma](#lenguaje-y-plataforma)
  - [Build](#build)
  - [Dependencias de testing (scope: test)](#dependencias-de-testing-scope-test)
  - [Plugins de Maven](#plugins-de-maven)
- [Testing y Garantía de Calidad](#testing-y-garantía-de-calidad)
  - [Resumen de cobertura por clase](#resumen-de-cobertura-por-clase)
- [Instrucciones de ejecución](#instrucciones-de-ejecución)
  - [Compilar y verificar el proyecto](#compilar-y-verificar-el-proyecto)
  - [Ejecutar la suite de pruebas unitarias](#ejecutar-la-suite-de-pruebas-unitarias)
  - [Generar el reporte de cobertura JaCoCo](#generar-el-reporte-de-cobertura-jacoco)

## Arquitectura

El proyecto está organizado en capas según Clean Architecture, con dependencias apuntando siempre hacia el dominio (DDD):

- **`domain`**: el corazón del sistema. Entidades (Aggregate Roots), Value Objects, excepciones de negocio y contratos (interfaces de repositorio y servicios). Sin dependencias de producción.
- **`application`**: casos de uso que orquestan las reglas del dominio. Dependen únicamente de contratos de `domain`.
- **`infrastructure`**: implementaciones concretas de los contratos (repositorio en memoria, notificación por email). Aislada del dominio y **excluida del reporte de cobertura**.

Las interacciones externas se modelan como interfaces inyectadas por constructor, de modo que la capa de dominio nunca depende de una implementación concreta.

### Estructura del directorio

```
hito3-backend-domain-driven-design/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ticketera/
    │   ├── application/
    │   │   ├── port/
    │   │   │   └── MessageNotifier.java
    │   │   └── usecase/
    │   │       ├── ProcessOrderUseCase.java
    │   │       └── SendBookingConfirmationUseCase.java
    │   ├── domain/
    │   │   ├── entity/
    │   │   │   ├── Customer.java
    │   │   │   ├── Event.java
    │   │   │   └── TicketPool.java
    │   │   ├── exception/
    │   │   │   ├── InvalidEmailException.java
    │   │   │   ├── InvalidOrderException.java
    │   │   │   └── SoldOutException.java
    │   │   ├── repository/
    │   │   │   └── EventRepository.java
    │   │   └── valueobject/
    │   │       ├── Email.java
    │   │       ├── EventId.java
    │   │       ├── Money.java
    │   │       └── TicketQuantity.java
    │   └── infrastructure/
    │       ├── notification/
    │       │   └── EmailNotificationService.java
    │       └── persistence/
    │           └── InMemoryEventRepository.java
    └── test/java/com/ticketera/
        ├── application/usecase/
        │   ├── ProcessOrderUseCaseTest.java
        │   └── SendBookingConfirmationUseCaseTest.java
        ├── domain/
        │   ├── entity/
        │   │   ├── CustomerTest.java
        │   │   ├── EventTest.java
        │   │   └── TicketPoolTest.java
        │   └── valueobject/
        │       ├── EmailTest.java
        │       ├── EventIdTest.java
        │       ├── MoneyTest.java
        │       └── TicketQuantityTest.java
        └── infrastructure/
            └── persistence/
                └── InMemoryEventRepositoryTest.java
```

### Descripción de archivos

**Entidades (Aggregate Roots):**

| Archivo | Responsabilidad |
|---|---|
| `Event.java` | Aggregate Root del contexto Ticketing. Identificado por un `EventId` (Value Object). Contiene nombre, venue, capacidad y delega el control de inventario a su `TicketPool`. Expone `hasAvailability()`, `getAvailableTickets()`, `getTicketSold()` y `reserveTickets(TicketQuantity)` como único punto de entrada para modificar el inventario. |
| `TicketPool.java` | Entidad interna que gestiona el stock de entradas disponibles. Valida que la capacidad sea positiva y que haya stock suficiente antes de reservar, evitando la sobreventa. |
| `Customer.java` | Entidad que representa a la persona que compra entradas, identificada por un `id` único y un email válido (Value Object `Email`). |

**Value Objects:**

| Archivo | Responsabilidad |
|---|---|
| `EventId.java` | Identificador inmutable de un evento. Rechaza `null` y valores en blanco. |
| `TicketQuantity.java` | Cantidad de entradas de una orden. Rechaza valores ≤ 0 (`InvalidOrderException`). |
| `Money.java` | Precio de una entrada. Rechaza valores ≤ 0 (`InvalidOrderException`). |
| `Email.java` | Email normalizado (trim + minúsculas). Rechaza `null`, vacíos o formatos inválidos (`InvalidEmailException`). |

**Casos de uso:**

| Archivo | Responsabilidad |
|---|---|
| `ProcessOrderUseCase.java` | Procesa una orden: construye los Value Objects, busca el evento en el repositorio, reserva las entradas y notifica al administrador. Depende de `EventRepository` y `MessageNotifier` (inyectados por constructor). |
| `SendBookingConfirmationUseCase.java` | Envía una confirmación de reserva al cliente. Depende de `MessageNotifier` (inyectado por constructor). |

**Puertos de Aplicación:**

| Archivo | Responsabilidad |
|---|---|
| `MessageNotifier.java` | Interfaz que define el contrato para envío de notificaciones. Permite cambiar la implementación (SMS, email, push) sin modificar la capa de aplicación. |

**Contratos (interfaces del dominio):**

| Archivo | Responsabilidad |
|---|---|
| `EventRepository.java` | Contrato para acceso a datos de eventos (`Optional<Event> findById(EventId)`, `save`). Permite cambiar la fuente de datos sin modificar el dominio. |

**Excepciones personalizadas:**

| Archivo | Responsabilidad |
|---|---|
| `SoldOutException.java` | Se lanza cuando no hay entradas suficientes para satisfacer una reserva. |
| `InvalidOrderException.java` | Se lanza cuando una orden tiene datos inválidos (cantidad o precio ≤ 0). |
| `InvalidEmailException.java` | Se lanza cuando un email no es válido. |

**Infraestructura (excluida de cobertura):**

| Archivo | Responsabilidad |
|---|---|
| `InMemoryEventRepository.java` | Implementación en memoria de `EventRepository` (HashMap). |
| `EmailNotificationService.java` | Implementación de `MessageNotifier` que imprime el email en consola. |

### Nota sobre la arquitectura

La estructura de este proyecto combina tres patrones complementarios:

- **Clean Architecture**: separación en capas (`domain`, `application`, `infrastructure`) con dependencias apuntando hacia el núcleo.
- **Domain-Driven Design (DDD)**: modelado del negocio con entidades, Value Objects auto-validantes, Aggregate Roots y lenguaje ubicuo.
- **Hexagonal Architecture (Ports & Adapters)**: puertos (`application/port/`) para servicios externos y adaptadores (`infrastructure/`) para implementaciones concretas.

**Diferencias con el proyecto de ejemplo del profesor (neonpulse):**

| Elemento | Profesor (neonpulse) | Este proyecto (ticketera) | Razón |
|---|---|---|---|
| `domain/service/` | `StockManager`, `PurchaseValidator` | No existe | La lógica de validación vive en las entidades (`TicketPool.reserve()`) y Value Objects (`TicketQuantity`), siguiendo el principio DDD de que las entidades protegen sus propios invariantes. |
| `application/port/` | `MessageNotifier`, `SmsNotifier` | `MessageNotifier` | Se incluye `MessageNotifier` como puerto de aplicación para servicios externos. No se incluye `SmsNotifier` porque el dominio no maneja números de teléfono. |
| `application/service/` | `PaymentService`, `PurchaseService`, `ShoppingCart` | No existe | Los casos de uso (`ProcessOrderUseCase`, `SendBookingConfirmationUseCase`) ya orquestan la lógica de aplicación. Agregar servicios adicionales sería redundante. |

**Justificación técnica:**

El proyecto de ejemplo del profesor utiliza un enfoque más "service-oriented" donde la lógica de negocio está en servicios separados (`domain/service/`). Este proyecto utiliza un enfoque más "entity-oriented" (más idiomático DDD) donde las entidades y Value Objects encapsulan su propio comportamiento:

- `TicketPool.reserve()` valida stock → equivalente a `StockManager.checkAvailability()`
- `TicketQuantity` record valida cantidad → equivalente a `PurchaseValidator.processQuantity()`

Ambos enfoques son válidos y cumplen con la rúbrica del Hito 3. La diferencia es de **dónde se pone la lógica**, no de si está desacoplada.

## Lenguaje Ubicuo

Glosario compartido entre el equipo de negocio y el equipo técnico. Cada término de esta lista se usa de forma idéntica en el código, los tests y la documentación.

| Término | Definición |
|---|---|
| `Event` | Reunión pública con una sede y una capacidad definidas. Raíz del agregado del contexto Ticketing. |
| `Ticket` | El derecho a asistir a un `Event`. Una unidad del inventario del `Event`. |
| `TicketPool` | El inventario de entradas disponibles de un `Event`. Evita la sobreventa al respetar la capacidad. |
| `Order` | La solicitud de un cliente de comprar una cantidad de entradas para un `Event`. |
| `Booking` | Una reserva de entradas confirmada, producida al procesar con éxito una `Order`. |
| `Customer` | Persona que compra entradas, identificada por un `id` único y un email válido. |
| `Venue` | El lugar físico donde se realiza un `Event`. |
| `Capacity` | El número máximo de entradas que un `Event` puede vender. |
| `Sold Out` | Estado de un `Event` cuando no quedan entradas disponibles. |
| `Notification` | Un mensaje de salida (email, SMS) enviado a un cliente o al administrador. |

## Contexto Delimitado

**Ticketing** es el único contexto delimitado de este sistema. Su frontera cubre el catálogo de eventos, el inventario de entradas, la solicitud de órdenes y la confirmación de reservas. Conceptos como el procesamiento de pagos (facturación) o el control de acceso físico quedan excluidos de forma intencional y pertenecerían a contextos separados en un sistema de mayor escala.

## Tecnologías y dependencias

### Lenguaje y plataforma
- **Java 17** (compilador source/target 17)

### Build
- **Apache Maven** — Sistema de construcción y gestión de dependencias

### Dependencias de testing (scope: test)

| Dependencia | Versión | Propósito |
|---|---|---|
| `junit-jupiter-api` | 5.11.0 | Anotaciones y asserts de JUnit 5 (`@Test`, `@DisplayName`, `assertEquals`, `assertThrows`, etc.) |
| `junit-jupiter-engine` | 5.11.0 | Motor de ejecución de tests de JUnit 5 |
| `junit-jupiter-params` | 5.10.2 | Soporte para tests parametrizados (`@ParameterizedTest`, `@ValueSource`) |
| `mockito-core` | 5.11.0 | Framework de mocking para crear objetos simulados y verificar interacciones (`mock()`, `verify()`, `when()`) |

### Plugins de Maven

| Plugin | Versión | Propósito |
|---|---|---|
| `maven-surefire-plugin` | 3.2.5 | Ejecuta la suite de tests con soporte para nombres legibles de JUnit 5 |
| `jacoco-maven-plugin` | 0.8.15 | Instrumenta el código y genera reportes de cobertura (instrucciones, ramas, métodos, líneas). Excluye `com/ticketera/infrastructure/**` |
| `jacoco-console-reporter` | 1.3.2 | Imprime un resumen de cobertura directamente en la consola |

> **Nota sobre cobertura:** la capa `infrastructure` (detalles técnicos: repositorio en memoria y notificador por email) y los contratos de `domain/repository` (interfaces puras sin lógica) quedan **excluidos** del reporte de cobertura. Esto se configura con la propiedad `sonar.coverage.exclusions` (usada por el console-reporter) y con `<excludes>` en `jacoco-maven-plugin` (usada por el reporte HTML). La cobertura se mide sobre `domain` (entidades, value objects, excepciones) y `application` (casos de uso).

## Testing y Garantía de Calidad

Este proyecto utiliza **JUnit 5** y **Mockito** para asegurar los más altos estándares de calidad.

- **Patrón AAA Estricto**: Todos los tests están estructurados rigurosamente usando las fases Arrange, Act y Assert.
- **Excepciones de Negocio**: Las excepciones personalizadas se verifican exhaustivamente usando `assertThrows`.
- **Cobertura 100%**: La suite de tests garantiza 100% de cobertura de Instrucciones, Ramas, Métodos y Líneas sobre `domain` y `application` (la capa `infrastructure` está excluida por configurarse como detalles técnicos).

### Resumen de cobertura por clase

| Clase | Tests | Cobertura |
|---|---|---|
| `Event` | 7 | `hasAvailability()` true + false, `reserveTickets` éxito/sold out/cantidad inválida, cálculo de disponibles y vendidas |
| `TicketPool` | 5 | `capacity ≤ 0`, `quantity ≤ 0`, `quantity > available`, éxito, pool vacío |
| `Customer` | 5 | Creación válida, `id` null/blank, `name` null/blank |
| `TicketQuantity` | 4 | Valor válido, `quantity ≤ 0` |
| `Money` | 4 | Valor válido, `price ≤ 0` |
| `Email` | 5 | Normalización, `null`, blank, sin `@`, sin dominio |
| `EventId` | 3 | Trim, `null`, blank |
| `ProcessOrderUseCase` | 5 | `eventId` null/vacío, `quantity ≤ 0`, evento no encontrado, éxito |
| `SendBookingConfirmationUseCase` | 3 | Email null/vacío, éxito |
| `InMemoryEventRepository` | 2 | Evento inexistente, guardar y recuperar |
| **Total** | **43 tests** | **100% de clases, ramas, métodos y líneas** |

## Instrucciones de ejecución

### Compilar y verificar el proyecto

```bash
mvn clean compile
```

### Ejecutar la suite de pruebas unitarias

```bash
mvn test
```

### Generar el reporte de cobertura JaCoCo

Para ejecutar la suite de tests y generar el reporte de cobertura:

```bash
mvn clean test jacoco:report
```

Después de ejecutar el comando, ver la evidencia de cobertura en:
`target/site/jacoco/index.html`
