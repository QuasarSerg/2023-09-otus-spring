drop table if exists products cascade;
drop table if exists orders cascade;
drop table if exists users cascade;

create table products (
    id bigserial,
    full_name varchar(255),
    type varchar(255),
    product_size varchar(255),
    price numeric,
    primary key (id)
);

create table orders (
    id bigserial,
    product_id bigint references products (id) on delete cascade,
    amount integer,
    price numeric,
    applique varchar,
    primary key (id)
);

create table users (
    id bigserial,
    username varchar(255) not null,
    password varchar(255) not null,
    authority varchar(255) not null,
    primary key (id)
);