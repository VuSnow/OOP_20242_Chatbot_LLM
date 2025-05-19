create table if not exists "user"(
	id serial primary key,
	username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    fullname VARCHAR(255),
    ban BOOLEAN DEFAULT FALSE
);