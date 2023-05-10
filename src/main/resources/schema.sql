DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    user_id  INTEGER AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(100),
    login    VARCHAR(100),
    name     VARCHAR(100),
    birthday DATE
);

DROP TABLE IF EXISTS user_friends CASCADE;
CREATE TABLE user_friends
(
    user_id             INTEGER,
    friend_id           INTEGER,
    confirmation_status BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_user_friends PRIMARY KEY (user_id, friend_id)
);

DROP TABLE IF EXISTS films CASCADE;
CREATE TABLE films
(
    film_id       INTEGER AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100),
    description   VARCHAR(200),
    release_date  DATE,
    duration      INTEGER,
    mpa_rating_id INTEGER
);

DROP TABLE IF EXISTS mpa_rating CASCADE;
CREATE TABLE mpa_rating
(
    id   int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40)
);

DROP TABLE IF EXISTS film_genre CASCADE;
CREATE TABLE film_genre
(
    film_id  INTEGER,
    genre_id INTEGER,
    CONSTRAINT pk_film_genre PRIMARY KEY (film_id, genre_id)
);

DROP TABLE IF EXISTS genres CASCADE;
CREATE TABLE genres
(
    id   INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40)
);

DROP TABLE IF EXISTS favorite_films CASCADE;
CREATE TABLE favorite_films
(
    film_id INTEGER,
    user_id INTEGER,
    CONSTRAINT pk_favorite_films PRIMARY KEY (film_id, user_id)
);

ALTER TABLE favorite_films
    ADD CONSTRAINT fk_favorite_films_film_id FOREIGN KEY (film_id)
        REFERENCES films (film_id);

ALTER TABLE favorite_films
    ADD CONSTRAINT fk_favorite_films_user_id FOREIGN KEY (user_id)
        REFERENCES users (user_id);

ALTER TABLE user_friends
    ADD CONSTRAINT fk_user_friends_user_id FOREIGN KEY (user_id)
        REFERENCES users (user_id);

ALTER TABLE user_friends
    ADD CONSTRAINT fk_user_friends_friend_id FOREIGN KEY (friend_id)
        REFERENCES users (user_id);

ALTER TABLE films
    ADD CONSTRAINT fk_film_mpa_rating_id FOREIGN KEY (mpa_rating_id)
        REFERENCES mpa_rating (id);

ALTER TABLE film_genre
    ADD CONSTRAINT fk_film_genre_film_id FOREIGN KEY (film_id)
        REFERENCES films (film_id);

ALTER TABLE film_genre
    ADD CONSTRAINT fk_film_genre_genre_id FOREIGN KEY (genre_id)
        REFERENCES genres (id);