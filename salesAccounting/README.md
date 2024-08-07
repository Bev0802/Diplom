# SalesAccounting

SalesAccounting - это система для автоматизации процессов купли-продажи, предназначенная для управления продуктами, заказами и организациями. Проект состоит из двух основных частей: серверной части (productDB) и клиентской части (wholeSale).



## Структура проекта

### 1. productDB

Серверная часть отвечает за управление базой данных, содержащей информацию о продуктах, заказах, документах, сотрудниках и организациях.

#### Основные технологии:

- **Spring Boot:** Фреймворк для создания stand-alone, production-grade Spring приложений.
- **Spring Data JPA:** Инструмент для взаимодействия с базой данных.
- **H2 Database Engine:** Используется в качестве базы данных на период разработки.
- **PostgreSQL:** Используется в качестве основной базы данных.
- **Spring Cloud Netflix Eureka:** Сервис для регистрации и обнаружения микросервисов.
- **Spring Security:** Обеспечивает безопасность приложения.
- **JUnit:** Фреймворк для модульного тестирования.
- **Lombok:** Аннотации для упрощения работы с кодом.
- **Docker:** Поддержка контейнеризации приложений.
- **Docker Compose:** Манипуляции с контейнерами.
- **Git:** Система контроля версий.
- **GitHub:** Репозиторий.

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
- **Lombok:** Аннотации для упрощения работы с кодом.
- **Docker:** Поддержка контейнеризации приложений.

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
![main.JPG](wholeSale%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2Fmain.JPG)
![Market.JPG](wholeSale%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2FMarket.JPG)
![ListOrder.JPG](wholeSale%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2FListOrder.JPG)
![DetalOrder.JPG](wholeSale%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2FDetalOrder.JPG)
![ListDocs.JPG](wholeSale%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2FListDocs.JPG)
![DetalDoc.JPG](wholeSale%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fimg%2FDetalDoc.JPG)
![Reports.JPG](wholeSale/src/main/resources/static/img/Reports.JPG)
![Diagram.JPG](wholeSale/src/main/resources/static/img/Diagram.JPG)
