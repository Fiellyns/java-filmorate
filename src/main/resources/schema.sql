DROP TABLE IF EXISTS ratings_mpa CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendships CASCADE;

CREATE TABLE IF NOT EXISTS ratings_mpa (
  rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  rating_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  genre_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
  film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL,
  description VARCHAR(200),
  release_DATE DATE NOT NULL,
  duration INTEGER NOT NULL,
  rating_id INTEGER REFERENCES ratings_mpa (rating_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genres (
  film_id INTEGER NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
  genre_id INTEGER NOT NULL REFERENCES genres (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
  user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email VARCHAR NOT NULL,
  login VARCHAR NOT NULL,
  name VARCHAR NOT NULL,
  birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS likes (
  film_id INTEGER NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
  user_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendships (
  user_first_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
  user_second_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);