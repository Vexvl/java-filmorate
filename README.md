# java-filmorate


Используемая база данных
![Схема базы данных](https://github.com/Vexvl/java-filmorate/raw/main/table.png)


- films - данные о фильме
- users - данные пользователя
- favorite_films - понравившиеся фильмы
- user_friends - информация о дружбе
- film_genre - данные о жанрах
- genres - название жанров
- mpa_rating - рейтинги ассоциации кинокомпаний

Примеры запросов для основных операций:

1) Запись фильма в таблицу
- INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
- VALUES ('Название фильма', 'Описание фильма', '2022-01-01', 120, 1);

2) Удаление фильма по айди
- DELETE FROM films WHERE film_id = 1;
