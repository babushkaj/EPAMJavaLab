CREATE DATABASE news_db
    WITH 
    OWNER = com.epam.lab
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
	
CREATE TABLE author (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(30) NOT NULL,
	surname VARCHAR(30) NOT NULL);
	
CREATE TABLE users (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(20) NOT NULL,
	surname VARCHAR(20) NOT NULL,
	login VARCHAR(30) NOT NULL,
	password VARCHAR(30) NOT NULL);
	
CREATE TABLE roles (
	user_id BIGINT NOT NULL,
	role_name VARCHAR(30) NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users (id));
	
CREATE TABLE tag (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(30) NOT NULL);
	
CREATE TABLE news (
	id BIGSERIAL PRIMARY KEY,
	title VARCHAR(30) NOT NULL,
	short_text VARCHAR(100) NOT NULL,
	full_text VARCHAR(2000) NOT NULL,
	creation_date DATE NOT NULL,
	modification_date DATE NOT NULL);
		
CREATE TABLE news_tag (
	news_id BIGINT NOT NULL,
	tag_id BIGINT NOT NULL,
	FOREIGN KEY (news_id) REFERENCES news (id),
	FOREIGN KEY (tag_id) REFERENCES tag (id));
	
CREATE TABLE news_author (
	news_id BIGINT NOT NULL,
	author_id BIGINT NOT NULL,
	FOREIGN KEY (news_id) REFERENCES news (id),
	FOREIGN KEY (author_id) REFERENCES author (id));
