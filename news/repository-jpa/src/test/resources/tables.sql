CREATE TABLE author (
  id      BIGINT IDENTITY PRIMARY KEY,
  name    VARCHAR(30) NOT NULL,
  surname VARCHAR(30) NOT NULL
);

CREATE TABLE users (
  id       BIGINT IDENTITY PRIMARY KEY,
  name     VARCHAR(20) NOT NULL,
  surname  VARCHAR(20) NOT NULL,
  login    VARCHAR(30) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
  id   BIGINT IDENTITY PRIMARY KEY,
  role_name VARCHAR(30) NOT NULL
);

CREATE TABLE tag (
  id   BIGINT IDENTITY PRIMARY KEY,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE news (
  id                BIGINT IDENTITY PRIMARY KEY,
  title             VARCHAR(30)   NOT NULL,
  short_text        VARCHAR(100)  NOT NULL,
  full_text         VARCHAR(2000) NOT NULL,
  creation_date     DATE          NOT NULL,
  modification_date DATE          NOT NULL
);

CREATE TABLE news_author
(
  news_id   BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  primary key (news_id, author_id),
  constraint fk_news_author_news_id
  foreign key (news_id) references news (id),
  constraint fk_news_author_authors_id
  foreign key (author_id) references author (id)
);

CREATE TABLE news_tag
(
  news_id BIGINT NOT NULL,
  tag_id  BIGINT NOT NULL,
  primary key (news_id, tag_id),
  constraint fk_news_tag_news_id
  foreign key (news_id) references news (id),
  constraint fk_news_tag_tag_id
  foreign key (tag_id) references tag (id)

);

CREATE TABLE user_role (
	user_id BIGINT NOT NULL,
	role_id BIGINT NOT NULL,
	primary key (user_id, role_id),
    constraint fk_user_role_user_id
    foreign key (user_id) references users (id),
    constraint fk_user_role_role_id
    foreign key (role_id) references roles (id)
);
