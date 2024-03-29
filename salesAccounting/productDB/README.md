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
```shell
productDB/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bev0802/
│   │   │           └── salesAccounting/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               ├── service/
│   │   │               └── controller/
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           └── (static resources)
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── bev0802/
│                   └── salesAccounting/
│
└── pom.xml
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