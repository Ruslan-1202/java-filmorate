-- drop table FILMS;
CREATE TABLE IF NOT EXISTS genres
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS mpa
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS films
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255),
    description  VARCHAR(255),
    mpa_id       INTEGER REFERENCES genres (id),
    release_date DATE,
    duration     INTEGER
);

CREATE TABLE IF NOT EXISTS users
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255),
    login    VARCHAR(255),
    name     VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  INTEGER REFERENCES films (id)  NOT NULL,
    genre_id INTEGER REFERENCES genres (id) NOT NULL,
    constraint friends_pk primary key (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id   INTEGER REFERENCES users (id) NOT NULL,
    friend_id INTEGER REFERENCES users (id) NOT NULL,
    constraint friends_pk primary key (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id INTEGER REFERENCES films (id) NOT NULL,
    user_id INTEGER REFERENCES users (id) NOT NULL,
    constraint likes_pk primary key (FILM_ID, USER_ID)
);