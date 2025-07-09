CREATE SCHEMA if not test;
USE test;

drop table if EXISTS UserServiceDB;
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    role_id INTEGER NOT NULL REFERENCES roles(role_id)
);

INSERT INTO users(name, role_id) VALUES ("Akex", 1);
INSERt INTO roles(role_id, role_name) VALUES (1, "admin");