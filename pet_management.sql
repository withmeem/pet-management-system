CREATE DATABASE IF NOT EXISTS pet_management;
USE pet_management;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE pets (
    pet_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(30) NOT NULL,
    birthdate DATE NOT NULL,  -- Changed from age to birthdate
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES users(user_id)
);