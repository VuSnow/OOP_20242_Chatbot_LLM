drop table if exists "user";
drop table if exists "message";
drop table if exists conversation;

create table if not exists "user"(
	id serial primary key,
	username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    fullname VARCHAR(255),
	phone VARCHAR(255),
    ban BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS "message" (
    id VARCHAR(255) PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    sender VARCHAR(255) NOT NULL,         -- 'user' hoặc 'assistant'
    content TEXT NOT NULL,                -- nội dung tin nhắn
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table if not exists conversation(
	id VARCHAR(255) PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_archived BOOLEAN DEFAULT FALSE
);


