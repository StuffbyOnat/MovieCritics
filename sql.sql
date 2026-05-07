DROP DATABASE IF EXISTS moviecritics;

CREATE DATABASE moviecritics DEFAULT CHARACTER SET utf8mb4;

USE moviecritics;

CREATE TABLE Movies
(movieID integer(10) auto_increment primary key,
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
parentalRestriction boolean default false)AUTO_INCREMENT=1000;

CREATE TABLE Persons
(personID int(10) auto_increment primary key,
firstName varchar(100),
lastName varchar(100),
dateOfBirth date,
nationality varchar(100)
)AUTO_INCREMENT=2000;

CREATE TABLE Users
(userID int(10) auto_increment primary key,
username varchar(100),
password varchar(100),
userType integer(1),
email varchar(255) unique,
isParent boolean
)AUTO_INCREMENT=1;
insert into users(username,password,userType,email,isParent)
values ('onatu',1234,1,'onat.unlu@yandex.com',true),
('ardat',1234,3,'arda.topalli@cibirnet.com',false),
('şevvald',1234,2,'sevval.demir@gmail.com',true),
('barışu',1234,3,'baris.unlu@cibirnet.com',false),
('edipa',1234,1,'edip.akbayram@anatolia.com',true);

INSERT INTO Persons (firstName, lastName, dateOfBirth, nationality) VALUES 
('Christopher', 'Nolan', '1970-07-30', 'British'),
('Quentin', 'Tarantino', '1963-03-27', 'American'),
('Christian', 'Bale', '1974-01-30', 'British'),
('Brad', 'Pitt', '1963-12-18', 'American'),
('Uma', 'Thurman', '1970-04-29', 'American');

INSERT INTO Movies (title, releaseYear, language, countryOfOrigin, genre, directorld, leadingActor, about) VALUES 
('Inception', 2010, 'English', 'USA', 'Sci-Fi', 'Christopher Nolan', 'Leonardo DiCaprio', 'Rüyalar içinde hırsızlık.'),
('Pulp Fiction', 1994, 'English', 'USA', 'Crime', 'Quentin Tarantino', 'John Travolta', 'Kesişen mafya hikayeleri.'),
('The Dark Knight', 2008, 'English', 'USA', 'Action', 'Christopher Nolan', 'Christian Bale', 'Batman ve Jokerin savaşı.'),
('Fight Club', 1999, 'English', 'USA', 'Drama', 'David Fincher', 'Brad Pitt', 'Uykusuzluk çeken bir adamın hikayesi.'),
('Kill Bill', 2003, 'English', 'USA', 'Action', 'Quentin Tarantino', 'Uma Thurman', 'Gelin intikam arıyor.');

