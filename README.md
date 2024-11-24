# Task Management System

## Описание проекта

Данный проект представляет собой систему управления задачами (Task Management System), разработанную с использованием Java и Spring Boot. 
Проект предоставляет REST API для работы с задачами, а также реализует JWT-аутентификацию и авторизацию. 
Основной функционал включает управление задачами, ролевую модель пользователей и администраторов, комментарии к задачам и фильтрацию данных.

---

## Функциональность

- Регистрация и авторизация пользователей с использованием JWT.
- CRUD-операции для задач (создание, обновление, удаление, просмотр).
- Ролевой доступ:
  - **Администратор**: полный контроль над задачами и пользователями.
  - **Пользователь**: доступ к задачам, назначенным на него.
- Комментарии к задачам (добавление, удаление, просмотр).
- Фильтрация задач по автору, исполнителю, статусу.
- Пагинация списков задач.
- Поддержка Swagger UI для документации API.
- Валидация данных и обработка ошибок.

---

## Запуск проекта локально

### Требования

- **Java 17+**
- **Maven** или **Gradle**
- **Docker** и **Docker Compose**

### Инструкции по запуску

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/your-repo/task-management-system.git
   cd task-management-system
   ```
2. Соберите проект
  ```bash
   ./mvnw clean package
   ```
3. Запустите Docker Compose
  ```bash
  docker-compose up
  ```
4. Swagger UI будет доступен по адресу: http://localhost:8080/swagger-ui/index.html



## Маршруты API
| **HTTP Метод** | **Путь**                      | **Описание**                           | **Доступ**                      |
|----------------|--------------------------------|-----------------------------------------|----------------------------------|
| POST           | `/auth/register`              | Регистрация пользователя               | Публичный                        |
| POST           | `/auth/login`                 | Авторизация пользователя               | Публичный                        |
| GET            | `/users`                      | Получение списка пользователей         | Администратор                   |
| GET            | `/users/{id}`                 | Получение информации о пользователе    | Администратор или текущий пользователь |
| GET            | `/tasks`                      | Получение списка задач                 | Авторизованный пользователь      |
| POST           | `/tasks`                      | Создание задачи                        | Администратор                   |
| PUT            | `/tasks/{id}`                 | Обновление задачи                      | Автор или администратор          |
| DELETE         | `/tasks/{id}`                 | Удаление задачи                        | Администратор                   |
| GET            | `/tasks/{taskId}/comments`    | Получение комментариев для задачи      | Авторизованный пользователь      |
| POST           | `/tasks/{taskId}/comments`    | Добавление комментария к задаче        | Авторизованный пользователь      |
| DELETE         | `/comments/{id}`              | Удаление комментария                   | Автор или администратор          |


## Стек технологий

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Security** для аутентификации и авторизации.
- **PostgreSQL** как база данных.
- **Swagger/OpenAPI** для документирования API.
- **Docker** и **Docker Compose** для контейнеризации.
- **JUnit** и **Mockito** для тестирования.

## Тестирование

- Написаны базовые тесты для проверки функциональности.
- Команда для запуска тестов:

      bash
      /mvnw test

  

