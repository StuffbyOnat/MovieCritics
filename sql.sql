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
leadingActor varchar(255),
supportingActor varchar(255),
about text,
rating tinyint CHECK (rating >= 1 AND rating <= 10),
comments text,
poster varchar(255),
parentalRestriction boolean);

CREATE TABLE Persons
(personID int(10) auto_increment primary key,
firstName varchar(100),
lastName varchar(100),
dateOfBirth date,
nationality varchar(100)
);

CREATE TABLE Users
(userID int(10) auto_increment primary key,
username varchar(100),
password varchar(100),
userType integer(1),
email varchar(255) unique,
isParent boolean
);
insert into users(username,password,userType,email,isParent)
values ('onatu',1234,1,'onat.unlu@yandex.com',true),
('ardat',1234,3,'arda.topalli@cibirnet.com',false),
('şevvald',1234,2,'sevval.demir@gmail.com',true),
('barışu',1234,3,'baris.unlu@cibirnet.com',false),
('edipa',1234,1,'edip.akbayram@anatolia.com',true);

