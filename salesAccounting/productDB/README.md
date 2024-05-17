# productDB

## Описание проекта

`productDB` - это RESTful веб-сервис для управления базой данных продуктов. Сервис позволяет создавать, просматривать, обновлять и удалять информацию о продуктах, а также предоставляет расширенные возможности фильтрации продуктов по различным критериям.

## Технологии и зависимости

Проект разработан с использованием следующих технологий и библиотек:

- Spring Boot 3.2.3
- Spring Data JPA
- Spring Web
- Spring Cloud Config Server
- Spring Cloud Netflix Eureka Client
- H2 Database
- Lombok
- JUnit (для тестирования)

## Структура проекта
опсание структуры проекта
# productDB Project Structure

## Overview
This document describes the structure of the `productDB` project, which includes details about the Java package structure, REST controllers, services, models, repositories, and configurations.

## Source Code Structure

### Main Java Source (`src/main/java/productdb`)
Organized by functionality and type, here's an overview of the primary Java packages and their key classes.

#### Configuration (`config`)
- `SecurityConfig` - Configuration for security and URL access restrictions.

#### Controllers (`controller`)
Handles incoming HTTP requests and returns responses to the client.
- `CounterpartyController`
- `EmployeeController`
   - `authenticateEmployee`
   - `getAllEmployees`
   - `getEmployeeById`
   - `getEmployeesByOrganizationId`
   - `registerEmployee`
- `OrderController`
   - `createOrder`
   - `getAllOrders`
   - `getOrderById`
- `OrganizationController`
   - `authenticateOrganization`
   - `deleteOrganization`
   - `getAllOrganizations`
   - `getById`
   - `getOrganizationName`
   - `registerOrganization`
   - `searchOrganizations`
   - `updateOrganization`
- `ProductController`
   - `createProduct`
   - `deleteProduct`
   - `findProducts`
   - `getAllProducts`
   - `getAvailableProducts`
   - `getOrganizationByProductId`
   - `getProductById`
   - `getProductsAvailableForPurchaseByOrganizationId`
   - `getProductsByName`
   - `getProductsByOrganization`
   - `getProductsByPriceRange`
   - `handleProductNotFound`
   - `updateProduct`

#### Models (`model`)
Entities representing database tables.
- `Counterparty`
- `Employee`
- `Order`
- `OrderItem`
- `Organization`
- `Product`

#### Repositories (`repository`)
Interfaces for CRUD operations on the database.
- `CounterpartyRepository`
- `EmployeeRepository`
- `OrderItemRepository`
- `OrderRepository`
- `OrganizationRepository`
- `ProductRepository`

#### Services (`service`)
Business logic and interactions with the repositories.
- `CounterpartService`
- `EmployeeService`
- `OrderService`
- `OrganizationService`
- `ProductService`

#### Exceptions (`exceptions`)
Custom exceptions for specific error handling.
- `ProductInStockException`
- `ProductNotFoundException`

### Resources (`src/main/resources`)
Configuration files and static resources.
- `application.properties`
- `data.sql`
- `productDB.yml`

### Static Files (`src/main/resources/static`)
CSS, JavaScript, and images used in the web interface.
- `/static/style.css`
- `/static/script.js`

### Templates (`src/main/resources/templates`)
Thymeleaf templates for rendering HTML content.
- Various HTML files for different views and operations.

## Tests (`src/test`)
Unit and integration tests for the application.
- `ProductDbApplicationTests`

## Miscellaneous Files
- `.gitignore` - Specifies intentionally untracked files to ignore.
- `README.md` - Markdown file with project overview and setup instructions.
- `mvnw`, `mvnw.cmd` - Maven wrapper script for building the project.

## Build Configuration
- `pom.xml` - Maven configuration file with project dependencies and build settings.

This structure ensures that all components of the productDB application are organized and easily manageable, supporting both development and maintenance activities.

```shell
productDB/
├── .idea/
├── .mvn/
├── .vscode/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── productdb/
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── EmployeeController.java
│   │   │       │   ├── OrderController.java
│   │   │       │   ├── OrganizationController.java
│   │   │       │   └── ProductController.java
│   │   │       ├── exceptions/
│   │   │       │   ├── ProductInStockException.java
│   │   │       │   └── ProductNotFoundException.java
│   │   │       ├── model/
│   │   │       │   ├── Employee.java
│   │   │       │   ├── Order.java
│   │   │       │   ├── OrderItem.java
│   │   │       │   ├── Organization.java
│   │   │       │   └── Product.java
│   │   │       ├── repository/
│   │   │       │   ├── EmployeeRepository.java
│   │   │       │   ├── OrderItemRepository.java
│   │   │       │   ├── OrderRepository.java
│   │   │       │   ├── OrganizationRepository.java
│   │   │       │   └── ProductRepository.java
│   │   │       ├── security/
│   │   │       │   └── SecurityConfig.java
│   │   │       └── service/
│   │   │           ├── EmployeeService.java
│   │   │           ├── OrderService.java
│   │   │           ├── OrganizationService.java
│   │   │           └── ProductService.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       └── static/
│   │           ├── style.css
│   │           └── script.js
│   │
│   └── test/
│       └── java/
│           └── productdb/
│               └── service/
│                   └── ProductDbApplicationTests.java
│
├── target/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── productDB.iml
└── README.md

```


## Запуск проекта

Для запуска проекта необходимо:

1. Убедиться, что на вашем компьютере установлен JDK 17 и Maven.
2. Склонировать репозиторий проекта.
3. Перейти в корневую директорию проекта и выполнить команду:

```shell
mvn spring-boot:run
```
Это запустит веб-сервис на порту 8080 (или на другом порту, если он указан в application.properties).

## Тестирование

## Документация

```shell
mvn javadoc:javadoc
```

Сущность OrderItem играет ключевую роль в управлении заказами в системах электронной коммерции или любых других системах, где требуется обработка заказов. Вот основные цели и преимущества использования OrderItem:

1. Детализация заказа
   OrderItem позволяет подробно описывать каждый элемент в заказе. Это включает в себя такие данные, как товар, его количество и цена. Таким образом, один заказ может содержать множество OrderItem, что отражает покупку разных товаров или разных количеств одного товара в одном заказе.

2. Гибкость управления товарами
   Использование OrderItem позволяет легко управлять различными аспектами товаров в заказе, такими как изменение количества товара, обновление цены или удаление товара из заказа без воздействия на сам заказ или другие товары в этом заказе.

3. Упрощение транзакций
   Каждый OrderItem может быть связан с конкретными транзакционными процессами, такими как биллинг (выставление счетов), учет наличия товаров, или логистика. Это упрощает обработку этих операций на уровне каждого отдельного товара в заказе.

4. Трассировка и отчетность
   OrderItem обеспечивает детализацию данных для анализа продаж, позволяя анализировать популярность товаров, наиболее прибыльные товары или предпочтения клиентов. Это также помогает в учете и налогообложении, предоставляя точную информацию о проданных товарах и их стоимости.

5. Адаптивность к изменениям в ассортименте
   Система с OrderItem может легко адаптироваться к изменениям в ассортименте товаров, так как каждый элемент заказа управляется отдельно. Это позволяет добавлять новые товары или удалять устаревшие без необходимости перестраивать всю структуру заказа.

6. Облегчение комплексных операций
   В случаях, когда нужно применять различные скидки, налоги или тарифы в зависимости от конкретного товара или его количества, OrderItem предоставляет необходимую гранулярность для точного расчета.

Использование OrderItem в системе заказов таким образом повышает её эффективность, точность и удобство в управлении заказами, что важно для бизнеса, стремящегося к оптимизации своих операций и улучшению обслуживания клиентов.