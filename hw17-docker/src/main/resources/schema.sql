drop table if exists books cascade;
drop table if exists authors cascade;
drop table if exists genres cascade;
drop table if exists comments cascade;
drop table if exists users cascade;

create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table comments (
    id bigserial,
    text varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

create table users (
    id bigserial,
    username varchar(255) not null,
    password varchar(255) not null,
    authority varchar(255) not null,
    primary key (id)
);