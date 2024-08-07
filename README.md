# <h1>**Дипломная (итоговая) работа**

## по специализации: Разработчик — Веб-разработка на Java

<h1>Теоретическая часть</h1>

Тема: [Разработка микросервисной платформы
для торговой площадки организаций на базе Spring Boot и Cloud](https://docs.google.com/document/d/1FHRa-3CLlUm6XhEfwhpFOczjTOqyjOdp/edit?usp=sharing&ouid=101680787802208176601&rtpof=true&sd=true)

Работа размещена на docs.google.com.

<h1>Практическая часть</h1>

Для демонстрации своих практических навыков полученных в результате обучения мной разработано микропоцессоное веб-приложение [salesAccounting](https://github.com/Bev0802/Diplom/tree/master/salesAccounting):

- на языке программирования: _Java_
- с использованием фреймворка: _Spring Boot_

## Описание проекта

SalesAccounting - это система для автоматизации процессов купли-продажи, предназначенная для управления продуктами, заказами и организациями. Проект состоит из двух основных частей: серверной части (productDB) и клиентской части (wholeSale).

## Структура проекта

### 1. productDB

Серверная часть отвечает за управление базой данных, содержащей информацию о продуктах, заказах, документах, сотрудниках и организациях.

#### Основные технологии:

- **Spring Boot:** Фреймворк для создания stand-alone, production-grade Spring приложений.
- **Spring Data JPA:** Инструмент для взаимодействия с базой данных.
- **PostgreSQL:** Используется в качестве основной базы данных.
- **Spring Cloud Netflix Eureka:** Сервис для регистрации и обнаружения микросервисов.
- **Spring Security:** Обеспечивает безопасность приложения.
- **JUnit:** Фреймворк для модульного тестирования.

#### Основные функции:

- Управление продуктами: создание, обновление, удаление и поиск продуктов.
- Управление заказами: создание, обновление, подтверждение, отмена и поиск заказов.
- Управление документами: создание документов на основе заказов, поиск и удаление документов.
- Управление сотрудниками: регистрация, аутентификация, клонирование и удаление сотрудников.
- Управление организациями: регистрация, аутентификация, обновление и удаление организаций.

#### Структура каталогов:

```plaintext
productDB
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── productdb
│   │   │   │   ├── config
│   │   │   │   ├── controller
│   │   │   │   ├── exception
│   │   │   │   ├── model
│   │   │   │   ├── repository
│   │   │   │   ├── service
│   │   │   │   └── specification
│   │   ├── resources
│   └── test
└── pom.xml
```

## 2. wholeSale

Клиентская часть предоставляет веб-интерфейс для взаимодействия с системой.

### Основные технологии:

- **Spring Boot:** Фреймворк для создания stand-alone, production-grade Spring приложений.
- **Thymeleaf:** Шаблонизатор для создания динамических веб-страниц.
- **Spring Cloud Netflix Eureka:** Сервис для регистрации и обнаружения микросервисов.
- **JUnit:** Фреймворк для модульного тестирования.

### Основные функции:

- Аутентификация и регистрация сотрудников и организаций.
- Управление продуктами: создание, обновление, удаление и просмотр продуктов.
- Управление заказами: создание, обновление, подтверждение, отмена и просмотр заказов.
- Управление документами: просмотр документов по покупателю и продавцу.
- Управление сотрудниками: просмотр и редактирование информации о сотрудниках.
- Управление организациями: просмотр и редактирование информации об организациях.

### Структура каталогов:

```plaintext
wholeSale
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── wholesale
│   │   │   │   ├── config
│   │   │   │   ├── controller
│   │   │   │   ├── exception
│   │   │   │   ├── model
│   │   │   │   ├── service
│   │   │   └── WholeSaleApplication.java
│   │   ├── resources
│   │       ├── static
│   │       └── templates
│   └── test
└── pom.xml
```

# Запуск проекта

### Для запуска проекта в Docker Compose выполните следующие шаги:

### Клонируйте репозиторий:

```bash
git clone https://github.com/yourusername/salesAccounting.git
cd salesAccounting
```

### Запустите Docker Compose:

```bash
docker-compose up --build
```

## Приложение будет доступно по следующим адресам:

- **Eureka Server:** [http://localhost:8761](http://localhost:8761)
- **Клиентская часть:** [http://localhost:8081](http://localhost:8081)

## Структура проекта

### Конфигурация (config)

Файлы конфигурации содержат настройки для работы приложения:

- **AppConfig:** Основная конфигурация приложения.
- **WebConfig:** Конфигурация для настройки веб-приложения.
- **BasicAuthInterceptor:** Перехватчик для добавления базовой аутентификации в запросы.

### Контроллеры (controller)

Контроллеры обрабатывают входящие HTTP-запросы и взаимодействуют с сервисами для выполнения бизнес-логики:

- **DocumentController:** Управление документами.
- **EmployeeController:** Управление сотрудниками.
- **ErrorController:** Обработка ошибок.
- **HomeController:** Главная страница.
- **LoginController:** Аутентификация и регистрация.
- **LogoutController:** Завершение сеанса.
- **OrderController:** Управление заказами.
- **OrganizationController:** Управление организациями.
- **ProductController:** Управление продуктами.

### Модели (model)

Модели представляют сущности приложения и содержат бизнес-логику:

- **Document:** Документ, связанный с заказом.
- **DocumentItem:** Позиция в документе.
- **Employee:** Сотрудник.
- **Order:** Заказ.
- **OrderItem:** Позиция в заказе.
- **Organization:** Организация.
- **Product:** Продукт.

### Сервисы (service)

Сервисы выполняют бизнес-логику и взаимодействуют с репозиториями для доступа к данным:

- **DocumentService:** Управление документами.
- **EmployeeService:** Управление сотрудниками.
- **OrderService:** Управление заказами.
- **OrganizationService:** Управление организациями.
- **ProductService:** Управление продуктами.

### Исключения (exception)

Обработка исключений и ошибок в приложении:

- **GlobalExceptionHandler:** Глобальный обработчик исключений.
- **ServiceException:** Кастомное исключение для сервисного слоя.

## Иллюстрации проекта

![main.JPG](salesAccounting/wholeSale/src/main/resources/static/img/main.JPG)
![Market.JPG](salesAccounting/wholeSale/src/main/resources/static/img/Market.JPG)
![ListOrder.JPG](salesAccounting/wholeSale/src/main/resources/static/img/ListDocs.JPG)
![DetalOrder.JPG](salesAccounting/wholeSale/src/main/resources/static/img/DetalOrder.JPG)
![ListDocs.JPG](salesAccounting/wholeSale/src/main/resources/static/img/ListDocs.JPG)
![DetalDoc.JPG](salesAccounting/wholeSale/src/main/resources/static/img/DetalDoc.JPG)
![Reports.JPG](salesAccounting/wholeSale/src/main/resources/static/img/Reports.JPG)
![Diagram.JPG](salesAccounting/wholeSale/src/main/resources/static/img/Diagram.JPG)