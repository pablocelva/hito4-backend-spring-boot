# Ticketera - Microservicio de Venta de Entradas (Hito 4)

Ticketera es un sistema de venta de entradas para eventos independientes. Este repositorio evoluciona el **Core de Dominio Puro** construido en el Hito 3 hacia un **microservicio con Spring Boot, PostgreSQL y Docker**, manteniendo el núcleo (`domain` y `application`) completamente aislado de frameworks, siguiendo los principios de **Clean Architecture**, **Domain-Driven Design (DDD)** y **Hexagonal Architecture (Ports & Adapters)**.

**Estado del Hito 4:** migración a Spring Boot y adaptador de persistencia JPA/PostgreSQL completados. Próximas fases: configuración por perfiles, capa web REST con manejo global de errores, Swagger/OpenAPI y Docker Compose.

Repositorios que sirven de base a este proyecto:

- **Hito 1** (núcleo inicial de la ticketera): [hito1-ticketera](https://github.com/pablocelva/hito1-ticketera)
- **Hito 3** (refactor DDD / Clean-Hexagonal): [hito3-backend-domain-driven-design](https://github.com/pablocelva/hito3-backend-domain-driven-design)

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
  - [Dependencias principales](#dependencias-principales)
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
- **`infrastructure`**: adaptadores que implementan los contratos del dominio (persistencia JPA/PostgreSQL y notificación por email). Aislada del dominio y **excluida del reporte de cobertura**.

Las interacciones externas se modelan como interfaces inyectadas por constructor, de modo que la capa de dominio nunca depende de una implementación concreta.

### Estructura del directorio

```
hito4-backend-spring-boot/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/ticketera/
    │   ├── TicketeraApplication.java
    │   ├── application/
    │   │   ├── port/
    │   │   │   └── MessageNotifier.java
    │   │   └── usecase/
    │   │       ├── CreateEventUseCase.java
    │   │       ├── GetEventDetailsUseCase.java
    │   │       ├── GetEventsUseCase.java
    │   │       ├── OrderResult.java
    │   │       ├── ProcessOrderUseCase.java
    │   │       └── SendBookingConfirmationUseCase.java
    │   ├── domain/
    │   │   ├── entity/
    │   │   │   ├── Customer.java
    │   │   │   ├── Event.java
    │   │   │   └── TicketPool.java
    │   │   ├── exception/
    │   │   │   ├── EventNotFoundException.java
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
    │           ├── EventEntity.java
    │           ├── EventJpaRepository.java
    │           └── JpaEventRepository.java
    └── test/java/com/ticketera/
        ├── application/usecase/
        │   ├── CreateEventUseCaseTest.java
        │   ├── GetEventDetailsUseCaseTest.java
        │   ├── GetEventsUseCaseTest.java
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
```

### Descripción de archivos

**Arranque del microservicio:**

| Archivo | Responsabilidad |
|---|---|
| `TicketeraApplication.java` | Clase principal `@SpringBootApplication` ubicada en la raíz `com.ticketera`. Punto de entrada del microservicio; su escaneo de componentes cubre todas las capas. Excluida de la medición de cobertura por ser código de bootstrap sin lógica de negocio. |

**Entidades (Aggregate Roots):**

| Archivo | Responsabilidad |
|---|---|
| `Event.java` | Aggregate Root del contexto Ticketing. Identificado por un `EventId` (Value Object). Contiene nombre, venue, capacidad y delega el control de inventario a su `TicketPool`. Expone `hasAvailability()`, `getAvailableTickets()`, `getTicketSold()` y `reserveTickets(TicketQuantity)` como único punto de entrada para modificar el inventario. Incluye la fábrica estática `reconstitute(...)` para reconstruir el agregado desde la base de datos preservando las entradas disponibles. |
| `TicketPool.java` | Entidad interna que gestiona el stock de entradas disponibles. Valida que la capacidad sea positiva y que haya stock suficiente antes de reservar, evitando la sobreventa. Su constructor de reconstitución `(capacidad, disponibles)` valida que las disponibles estén entre 0 y la capacidad. |
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
| `ProcessOrderUseCase.java` | Procesa una orden: construye los Value Objects, busca el evento (`EventNotFoundException` si no existe), reserva las entradas, **persiste el cambio con `save()`**, notifica al administrador y retorna un `OrderResult` con el detalle de la compra. Depende de `EventRepository` y `MessageNotifier` (inyectados por constructor). |
| `CreateEventUseCase.java` | Crea un nuevo evento generando su identificador (`UUID`), delegando las validaciones al dominio y persistiéndolo. Depende de `EventRepository`. |
| `GetEventsUseCase.java` | Consulta la cartelera completa delegando en `EventRepository.findAll()`. |
| `GetEventDetailsUseCase.java` | Consulta un evento por identificador y lanza `EventNotFoundException` cuando no existe. |
| `SendBookingConfirmationUseCase.java` | Envía una confirmación de reserva al cliente. Depende de `MessageNotifier` (inyectado por constructor). |
| `OrderResult.java` | Record de aplicación que transporta el resultado de una orden (evento, nombre, cantidad comprada y restante) hacia la capa de presentación sin exponer entidades del dominio. |

**Puertos de Aplicación:**

| Archivo | Responsabilidad |
|---|---|
| `MessageNotifier.java` | Interfaz que define el contrato para envío de notificaciones. Permite cambiar la implementación (SMS, email, push) sin modificar la capa de aplicación. |

**Contratos (interfaces del dominio):**

| Archivo | Responsabilidad |
|---|---|
| `EventRepository.java` | Contrato para acceso a datos de eventos (`Optional<Event> findById(EventId)`, `List<Event> findAll()`, `void save(Event)`). Permite cambiar la fuente de datos sin modificar el dominio. |

**Excepciones personalizadas:**

| Archivo | Responsabilidad |
|---|---|
| `SoldOutException.java` | Se lanza cuando no hay entradas suficientes para satisfacer una reserva. |
| `EventNotFoundException.java` | Se lanza cuando no existe el evento solicitado (se mapeará a HTTP 404 desde la capa web). |
| `InvalidOrderException.java` | Se lanza cuando una orden tiene datos inválidos (cantidad o precio ≤ 0). |
| `InvalidEmailException.java` | Se lanza cuando un email no es válido. |

**Infraestructura (excluida de cobertura):**

| Archivo | Responsabilidad |
|---|---|
| `EventEntity.java` | Modelo de persistencia JPA (`@Entity`, tabla `events`) con columnas `id`, `name`, `venue`, `capacity` y `available_tickets`. Mapea desde/hacia el agregado `Event` mediante `fromDomain()`/`toDomain()`, manteniendo el dominio libre de anotaciones de persistencia. |
| `EventJpaRepository.java` | Interfaz que hereda de `JpaRepository<EventEntity, String>` (Spring Data). Genera las operaciones CRUD de forma automática. |
| `JpaEventRepository.java` | Adaptador `@Repository` que implementa el puerto del dominio `EventRepository`, delegando en `EventJpaRepository` y traduciendo entidad ↔ dominio. Reemplaza al antiguo repositorio en memoria del Hito 3. |
| `EmailNotificationService.java` | Implementación `@Component` de `MessageNotifier` que imprime el email en consola. |

### Nota sobre la arquitectura

La estructura de este proyecto combina tres patrones complementarios:

- **Clean Architecture**: separación en capas (`domain`, `application`, `infrastructure`) con dependencias apuntando hacia el núcleo.
- **Domain-Driven Design (DDD)**: modelado del negocio con entidades, Value Objects auto-validantes, Aggregate Roots y lenguaje ubicuo.
- **Hexagonal Architecture (Ports & Adapters)**: puertos (`application/port/` y `domain/repository/`) para servicios externos y adaptadores (`infrastructure/persistence/`, `infrastructure/notification/`) para implementaciones concretas.

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
- **Java 17** sobre **Spring Boot 3.5.7** (`spring-boot-starter-parent`)
- **Hibernate** como proveedor JPA (incluido en `spring-boot-starter-data-jpa`)

### Build
- **Apache Maven** — Sistema de construcción y gestión de dependencias
- **spring-boot-maven-plugin** — Empaqueta el jar ejecutable y permite arrancar con `mvn spring-boot:run`

### Dependencias principales

| Dependencia | Versión | Propósito |
|---|---|---|
| `spring-boot-starter-web` | gestionada por Spring Boot | API REST con Spring Web MVC y Tomcat embebido |
| `spring-boot-starter-validation` | gestionada por Spring Boot | Validación declarativa con Jakarta Bean Validation (`@Valid`, `@NotBlank`, etc.) |
| `spring-boot-starter-data-jpa` | gestionada por Spring Boot | Persistencia con Spring Data JPA e Hibernate |
| `postgresql` | gestionada por Spring Boot | Driver JDBC de PostgreSQL (scope `runtime`) |
| `springdoc-openapi-starter-webmvc-ui` | 2.8.9 | Especificación OpenAPI 3 y Swagger UI interactiva |

### Dependencias de testing (scope: test)

| Dependencia | Versión | Propósito |
|---|---|---|
| `spring-boot-starter-test` | gestionada por Spring Boot | Incluye JUnit 5 (API, engine y params), Mockito, AssertJ y MockMvc para las siguientes fases |

### Plugins de Maven

| Plugin | Versión | Propósito |
|---|---|---|
| `spring-boot-maven-plugin` | 3.5.7 | Genera el jar ejecutable y habilita `mvn spring-boot:run` |
| `maven-surefire-plugin` | gestionada por Spring Boot | Ejecuta la suite de tests con soporte para nombres legibles de JUnit 5 |
| `jacoco-maven-plugin` | 0.8.15 | Instrumenta el código y genera reportes de cobertura (instrucciones, ramas, métodos, líneas). Excluye `com/ticketera/infrastructure/**` y la clase bootstrap `TicketeraApplication` |
| `jacoco-console-reporter` | 1.3.2 | Imprime un resumen de cobertura directamente en la consola |

> **Nota sobre cobertura:** la capa `infrastructure` (detalles técnicos: adaptador de persistencia JPA y notificador por email), los contratos de `domain/repository` (interfaces puras sin lógica) y la clase bootstrap `TicketeraApplication` quedan **excluidos** del reporte de cobertura. Esto se configura con la propiedad `sonar.coverage.exclusions` (usada por el console-reporter) y con `<excludes>` en `jacoco-maven-plugin` (usada por el reporte HTML). La cobertura se mide sobre `domain` (entidades, value objects, excepciones) y `application` (casos de uso).

## Testing y Garantía de Calidad

Este proyecto utiliza **JUnit 5** y **Mockito** (gestionados por el BOM de Spring Boot) para asegurar los más altos estándares de calidad. La suite se mantiene como **tests unitarios puros**: no levanta contexto de Spring ni requiere base de datos, lo que la hace rápida y determinista; las pruebas de integración (controladores, persistencia real) se incorporan en las siguientes fases del hito.

- **Patrón AAA Estricto**: Todos los tests están estructurados rigurosamente usando las fases Arrange, Act y Assert.
- **Excepciones de Negocio**: Las excepciones personalizadas se verifican exhaustivamente usando `assertThrows`.
- **Cobertura 100%**: La suite garantiza 100% de cobertura de Líneas, Ramas y Métodos sobre las 17 clases concretas de `domain` y `application`. La capa `infrastructure` está excluida por configurarse como detalle técnico, igual que los contratos sin lógica (`domain/repository`).

### Resumen de cobertura por clase

| Clase | Tests | Cobertura |
|---|---|---|
| `Event` | 8 | `hasAvailability()` true + false, `reserveTickets` éxito/sold out/cantidad inválida, cálculo de disponibles/vendidas y reconstitución desde persistencia |
| `TicketPool` | 9 | `capacity ≤ 0`, `quantity ≤ 0`, `quantity > available`, éxito, pool vacío, reconstitución válida e inválida (disponibles fuera de rango, capacidad no positiva) |
| `Customer` | 5 | Creación válida, `id` null/blank, `name` null/blank |
| `TicketQuantity` | 4 | Valor válido, `quantity ≤ 0` |
| `Money` | 4 | Valor válido, `price ≤ 0` |
| `Email` | 5 | Normalización, `null`, blank, sin `@`, sin dominio |
| `EventId` | 3 | Trim, `null`, blank |
| `ProcessOrderUseCase` | 5 | `eventId` null/vacío, `quantity ≤ 0`, evento no encontrado, éxito con persistencia y retorno de `OrderResult` |
| `SendBookingConfirmationUseCase` | 3 | Email null/vacío, éxito |
| `CreateEventUseCase` | 2 | Creación válida (id generado + persistencia), validación delegada al dominio |
| `GetEventDetailsUseCase` | 2 | Evento encontrado, `EventNotFoundException` cuando no existe |
| `GetEventsUseCase` | 1 | Retorna la cartelera completa desde el repositorio |
| **Total** | **51 tests** | **100% de líneas, ramas y métodos** sobre las 17 clases concretas |

¹ El contador de clases del console-reporter reporta 17/18 porque incluye la interfaz `MessageNotifier` (contrato sin código ejecutable); los contratos de `domain/repository` quedan excluidos por configuración.

## Instrucciones de ejecución

### Compilar y verificar el proyecto

```bash
mvn clean compile
```

> El arranque del microservicio (`mvn spring-boot:run`) requiere la configuración por perfiles, Docker Compose y PostgreSQL que se incorporan en las siguientes fases; por ahora el entregable verificable es la suite de tests.

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
