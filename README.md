# 📝 Task Management API

REST API приложение для управления задачами с авторизацией через JWT.

---

## 🚀 Технологии

- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- Swagger / OpenAPI
- Lombok
- Maven

---

## 🔐 Функциональность

### Аутентификация:
- Регистрация пользователя (`/auth/register`)
- Авторизация пользователя (`/auth/login`)
- Получение access и refresh токенов
- Защищённые эндпоинты требуют JWT токен

### Задачи:
- Создание задачи
- Получение всех задач текущего пользователя
- Получение задачи по ID
- Обновление задачи
- Удаление задачи
- Фильтрация по статусу (`TODO`, `IN_PROGRESS`, `DONE`)

