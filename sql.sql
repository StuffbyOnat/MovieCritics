DROP DATABASE IF EXISTS moviecritics;

CREATE DATABASE moviecritics DEFAULT CHARACTER SET utf8mb4;

USE moviecritics;

CREATE TABLE Movies
(
    movieID integer(10) auto_increment primary key,
    title varchar(255),
    releaseYear integer(10),
    language varchar(100),
    countryOfOrigin varchar(100),
    genre varchar(100),
    directorld varchar(255),
    isWatched boolean,
    leadingActorld varchar(255),
    supportingActorld varchar(255),
    about text,
    rating tinyint CHECK (rating >= 1 AND rating <= 10),
    comments text,
    poster varchar(255),
    parentalRestriction boolean
) AUTO_INCREMENT=1000;

CREATE TABLE Persons
(
    personID int(10) auto_increment primary key,
    firstName varchar(100),
    lastName varchar(100),
    dateOfBirth date,
    nationality varchar(100)
) AUTO_INCREMENT=2000;

CREATE TABLE Users
(
    userID int(10) auto_increment primary key,
    username varchar(100) unique,
    password varchar(100),
    userType integer(1),
    email varchar(255) unique,
    isParent boolean
) AUTO_INCREMENT=1;

CREATE TABLE User_Critics
(
    userID int(10) not null,
    movieID int(10) not null,
    rating int(10) CHECK (rating >= 1 AND rating <= 10),
    comment text,
    PRIMARY KEY (userID, movieID),
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE,
    FOREIGN KEY (movieID) REFERENCES Movies(movieID) ON DELETE CASCADE
);

DELIMITER //
CREATE TRIGGER after_critic_insert
AFTER INSERT ON User_Critics
FOR EACH ROW
BEGIN
    -- Bir filme yeni yorum yapıldığında, o filmin User_Critics tablosundaki ortalama puanını alır ve yuvarlayıp (ROUND) Movies tablosuna yazar
    UPDATE Movies 
    SET rating = (SELECT ROUND(AVG(rating)) FROM User_Critics WHERE movieID = NEW.movieID)
    WHERE movieID = NEW.movieID;
END //
DELIMITER ;

INSERT INTO Users(username, password, userType, email, isParent) VALUES 
('onatu', '1234', 1, 'onat.unlu@yandex.com', true),
('ardat', '1234', 3, 'arda.topalli@cibirnet.com', false),
('şevvald', '1234', 2, 'sevval.demir@gmail.com', true),
('barışu', '1234', 3, 'baris.unlu@cibirnet.com', false),
('edipa', '1234', 1, 'edip.akbayram@anatolia.com', true);

INSERT INTO Persons (firstName, lastName, dateOfBirth, nationality) VALUES 
('Christopher', 'Nolan', '1970-07-30', 'British'),
('Quentin', 'Tarantino', '1963-03-27', 'American'),
('Christian', 'Bale', '1974-01-30', 'British'),
('Brad', 'Pitt', '1963-12-18', 'American'),
('Uma', 'Thurman', '1970-04-29', 'American');

INSERT INTO Movies (title, releaseYear, language, countryOfOrigin, genre, directorld, leadingActorld, supportingActorld, about, poster) VALUES 
('Inception', 2010, 'English', 'USA', 'Sci-Fi', 'Christopher Nolan', 'Leonardo DiCaprio', 'Joseph Gordon-Levitt', 'Theft within dreams.','/Posters/Inception.jpg'),
('Pulp Fiction', 1994, 'English', 'USA', 'Crime', 'Quentin Tarantino', 'John Travolta', 'Samuel L. Jackson', 'Intersecting mob stories.','/Posters/PulpFiction.jpg'),
('The Dark Knight', 2008, 'English', 'USA', 'Action', 'Christopher Nolan', 'Christian Bale', 'Heath Ledger', 'The battle between Batman and the Joker.','/Posters/DarkKnight.jpg'),
('Fight Club', 1999, 'English', 'USA', 'Drama', 'David Fincher', 'Brad Pitt', 'Edward Norton', 'The story of an insomniac man.','/Posters/FightClub.jpg'),
('Kill Bill', 2003, 'English', 'USA', 'Action', 'Quentin Tarantino', 'Uma Thurman', 'Lucy Liu', 'The Bride seeks revenge.','/Posters/KillBill.jpg');

-- TAMAMLANAN KISIM: User_Critics tablosuna test verileri ekleniyor
INSERT INTO User_Critics (userID, movieID, rating, comment) VALUES 
(1, 1000, 10, 'Harika bir film, rüya sahneleri muazzam!'),
(1, 1002, 9, 'Batman ve Jokerin savaşı efsaneydi.'),
(2, 1004, 8, 'Aksiyon sahneleri çok iyi ama biraz kanlı.'),
(3, 1001, 9, 'Tarantino diyalogları yine şaşırtmadı.'),
(5, 1003, 10, 'Dövüş Kulübünün ilk kuralı...');

SELECT * FROM Movies;
SELECT * FROM Persons;
SELECT * FROM Users;
SELECT * FROM User_Critics;