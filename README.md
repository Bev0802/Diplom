# <h1>**Дипломная (итоговая) работа**

## по специализации: Разработчик — Веб-разработка на Java

<h1>Теоритическая чать</h1>

[Тема: Разработка микросервисной платформы для управления цепочками поставок 
на базе Spring Boot и Cloud с интеграцией блокчейн технологий](https://docs.google.com/document/d/1FHRa-3CLlUm6XhEfwhpFOczjTOqyjOdp/edit?usp=sharing&ouid=101680787802208176601&rtpof=true&sd=true)

Работа размещена на docs.google.com.

<h1>Практическая часть</h1>

Для демонстрации своих практических навыков полученных в результае обучения мной разработано микропоцессоное веб-приложение [salesAccounting](salesAccounting):

- на языке программирования: _Java_
- с ипользованием фраимворка: _Spring Boot_

## 1 этап: Разработка Сервера API

# [productDB](salesAccounting/productDB)

## Описание проекта

`productDB` - это RESTful веб-сервис для управления базой данных продуктов. Сервис позволяет создавать, просматривать, обновлять и удалять информацию о продуктах, а также предоставляет расширенные возможности фильтрации продуктов по различным критериям.

## Технологии и зависимости

Проект разработан с использованием следующих технологий и библиотек:

- Spring Boot 3.2.3
- Spring Data JPA
- Spring Web
- Spring Cloud Config Server
- Spring Boot Admin
- Spring Cloud Netflix Eureka Client
- H2 Database(на время разработки в дальнейшем подключу postgres)
- Lombok
- JUnit
- Mockito
- Prometheus 
- Grafana
- Docker
- DockerCompous
- Kubernetes (если разберусь)

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

## 2 этап: Разработка web-клинета API

# [wholeSale](salesAccounting/wholeSale)
