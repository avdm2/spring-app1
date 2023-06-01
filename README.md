# Дмитриев Артём Вадимович, БПИ216, НИУ ВШЭ. Курс "Конструирование программного обеспечения", ИДЗ 4. 
### [Условие](https://docs.google.com/document/d/1M_AQEhjyq7GtiQJ4TWt3bQsYcq1x1pIvxVepMY8S0Ps/edit#%5D).

## Стек
- Язык: `Java`
- Фреймворки: `Spring Boot`, `Lombok`, `JWT`, `Apache Commons Validator`
- СУБД: `PostgreSQL`
- API-requests: `Postman`
## API

---

### Регистрация
Method - **POST**.

Route - **"/api/auth/register"**. 

Запрос:
```json
{
    "username": "user",
    "email": "mail@gmail.com",
    "password": "password",
    "userRole": "customer"
}
```

---

### Вход
Method - **POST**.

Route - **"/api/auth/login"**.

Запрос:
```json
{
    "email": "mail@gmail.com",
    "password": "password"
}
```

---

### Получение информации о пользователе
Method - **POST**.

Route - **"/api/auth/get_info"**.

Запрос:
```json
{
    "username": "user"
}
```

---

### Получение меню
Method - **GET**.

Route - **"/api/dishes/menu"**.

---

### Создание заказа
Method - **POST**.

Route - **"/api/orders/create"**.

Запрос:
```json
{
    "username": "user",
    "specialRequests": "some comments",
    "status": "pending"
}
```

---

### Получение информации о заказе
Method - **POST**.

Route - **"/api/orders/get"**.

Запрос:
```json
{
    "id": 1
}
```

---

### Смена роли зарегистрированного пользователя [CHEF]
Method - **POST**.

Route - **"/api/roles/change"**.

Запрос:
```json
{
    "userRole": "manager",
    "username": "user"
}
```

---

### Получение информации о блюде
Method - **GET**.

Route - **"/api/dishes/{id}"**.

---

### Создание блюда [MANAGER]
Method - **POST**.

Route - **"/api/dishes/create"**.

Запрос:
```json
{
    "name": "Dish",
    "description": "some comments",
    "price": 10000,
    "quantity": 10
}
```

---

### Обновление информации о блюде [MANAGER]
Method - **PUT**.

Route - **"/api/dishes/{id}"**.

Запрос:
```json
{
    "name": "UpdatedDish",
    "description": "updated comments",
    "price": 999.76,
    "quantity": 10
}
```

---

### Удаление блюда [MANAGER]
Method - **DELETE**.

Route - **"/api/dishes/{id}"**.

---

## Особенности реализации:
- По обращению к **"/api/orders/get"** по номеру заказа пользователю выведется информация о 
заказе, однако в нем не будет списка блюд. Так сделано потому что в SQL-запросе из ТЗ для создания
таблицы заказов не было указано поле, хранящее в себе список блюд в заказе. В любом случае, реализовать это нетрудно.
Аналогичная ситуация и для **"/api/orders/create"** - создать заказ можно без учета блюд. 
- Для доступа у API помеченным меткой **[MANAGER]** или **[CHEF]** требуется указать в header области запроса JWT токен пользователя 
с соответствующей ролью.