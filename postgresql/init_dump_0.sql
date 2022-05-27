create table users(
    id serial,
    user_id varchar(20) not null,
    pwd varchar(20) not null,
    name varchar(20) not null,
    created_at timestamp default now()
);