# java-filmorate


Используемая база данных
![Схема базы данных](https://github.com/Vexvl/java-filmorate/raw/main/table.png)


- films - содержит данные о фильме.
- film_genre - содержит данные о жанрах, к которым относится фильм.
- genres - содержит название жанров.
- mpa_rating - содержит названия рейтингов ассоциации кинокомпаний.
- favorite_films - содержит информацию о лайках, поставленных фильму.
- users - содержит данные пользователя.
- user_friends - содержит информацию о дружбе пользователей.

Примеры запросов для основных операций:

1) Запись фильма в таблицу
- INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
- VALUES ('Название фильма', 'Описание фильма', '2022-01-01', 120, 1);

2) Удаление фильма по айди
- DELETE FROM films WHERE film_id = 1;
