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
    parentalRestriction boolean default false
) AUTO_INCREMENT=1000;

CREATE TABLE Persons
(
    personID int(10) auto_increment primary key,
    firstName varchar(100),
    lastName varchar(100),
    fullName varchar(100),
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

CREATE TABLE watchlists
(
    userID int(10) not null,
    movieID int(10) not null,
    PRIMARY KEY (userID, movieID),
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE,
    FOREIGN KEY (movieID) REFERENCES Movies(movieID) ON DELETE CASCADE
);

DELIMITER //
CREATE TRIGGER after_critic_insert
AFTER INSERT ON User_Critics
FOR EACH ROW
BEGIN
    UPDATE Movies 
    SET rating = (SELECT ROUND(AVG(rating)) FROM User_Critics WHERE movieID = NEW.movieID)
    WHERE movieID = NEW.movieID;
END //



INSERT INTO Users(username, password, userType, email, isParent) VALUES 
('onatu', '1234', 1, 'onat.unlu@yandex.com', true),
('ardat', '1234', 3, 'arda.topalli@cibirnet.com', false),
('sevvald', '1234', 2, 'sevval.demir@gmail.com', true),
('barisu', '1234', 3, 'baris.unlu@cibirnet.com', false),
('edipa', '1234', 1, 'edip.akbayram@anatolia.com', true);

INSERT INTO Persons (firstName, lastName, fullName, dateOfBirth, nationality) VALUES 
('Christopher', 'Nolan', 'Christopher Nolan', '1970-07-30', 'British'),
('Quentin', 'Tarantino', 'Quentin Tarantino', '1963-03-27', 'American'),
('Christian', 'Bale', 'Christian Bale', '1974-01-30', 'British'),
('Brad', 'Pitt', 'Brad Pitt', '1963-12-18', 'American'),
('Uma', 'Thurman', 'Uma Thurman', '1970-04-29', 'American');

INSERT INTO Movies (title, releaseYear, language, countryOfOrigin, genre, directorld, leadingActorld, supportingActorld, about, poster, parentalRestriction) VALUES 
('Inception', 2010, 'English', 'USA', 'Sci-Fi', 'Christopher Nolan', 'Leonardo DiCaprio', 'Joseph Gordon-Levitt', 'Theft within dreams.','/Posters/Inception.jpg', false),
('Pulp Fiction', 1994, 'English', 'USA', 'Crime', 'Quentin Tarantino', 'John Travolta', 'Samuel L. Jackson', 'Intersecting mob stories.','/Posters/PulpFiction.jpg', true),
('The Dark Knight', 2008, 'English', 'USA', 'Action', 'Christopher Nolan', 'Christian Bale', 'Heath Ledger', 'The battle between Batman and the Joker.','/Posters/DarkKnight.jpg', false),
('Fight Club', 1999, 'English', 'USA', 'Drama', 'David Fincher', 'Brad Pitt', 'Edward Norton', 'The story of an insomniac man.','/Posters/FightClub.jpg', true),
('Kill Bill', 2003, 'English', 'USA', 'Action', 'Quentin Tarantino', 'Uma Thurman', 'Lucy Liu', 'The Bride seeks revenge.','/Posters/KillBill.jpg', true);

INSERT INTO User_Critics (userID, movieID, rating, comment) VALUES 
(1, 1000, 10, 'Great movie, the dream sequences are amazing!'),
(1, 1002, 9, 'The battle between Batman and the Joker was legendary.'),
(2, 1004, 8, 'Great action scenes but a bit bloody.'),
(3, 1001, 9, 'Tarantino dialogues did not disappoint again.'),
(5, 1003, 10, 'The first rule of Fight Club...');

INSERT INTO watchlists (userID, movieID) VALUES 
(1, 1002),
(2, 1000),
(3, 1003),
(4, 1002),
(5, 1001);

CREATE TRIGGER after_movie_insert
AFTER INSERT ON Movies
FOR EACH ROW
BEGIN
    IF NEW.directorld IS NOT NULL AND NEW.directorld != '' THEN
        IF NOT EXISTS (SELECT 1 FROM Persons WHERE fullName = NEW.directorld) THEN
            INSERT INTO Persons(fullName) VALUES (NEW.directorld);
        END IF;
    END IF;

    IF NEW.leadingActorld IS NOT NULL AND NEW.leadingActorld != '' THEN
        IF NOT EXISTS (SELECT 1 FROM Persons WHERE fullName = NEW.leadingActorld) THEN
            INSERT INTO Persons(fullName) VALUES (NEW.leadingActorld);
        END IF;
    END IF;

    IF NEW.supportingActorld IS NOT NULL AND NEW.supportingActorld != '' THEN
        IF NOT EXISTS (SELECT 1 FROM Persons WHERE fullName = NEW.supportingActorld) THEN
            INSERT INTO Persons(fullName) VALUES (NEW.supportingActorld);
        END IF;
    END IF;
END //
DELIMITER ;

SELECT * FROM Movies;
SELECT * FROM Persons;
SELECT * FROM Users;
SELECT * FROM User_Critics;
SELECT * FROM watchlists;