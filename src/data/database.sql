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
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
--     is_archived BOOLEAN DEFAULT FALSE
);

-- Create 1 user to test function
INSERT INTO "user" (id, username, password, email, fullname, phone, ban) VALUES
(1, 'vusnow', 'Dung03062002?', 'adcarry1107@gmail.com', 'Vu Minh Dung', '0366032936', false);

-- Create 5 conversations cách nhau 3 ngày
INSERT INTO conversation (id, user_id, title, created_at, last_modified_at) VALUES
('conv-1', 1, 'Conversation One', NOW() - INTERVAL '12 days', NOW() - INTERVAL '12 days'),
('conv-2', 1, 'Conversation Two', NOW() - INTERVAL '9 days', NOW() - INTERVAL '9 days'),
('conv-3', 1, 'Conversation Three', NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days'),
('conv-4', 1, 'Conversation Four', NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days'),
('conv-5', 1, 'Conversation Five', NOW(), NOW());

-- conv-1: cách nhau 20-30s
INSERT INTO message VALUES
('msg-1-1', 'conv-1', 1, 'user', 'Hello, what is AI?', NOW() - INTERVAL '12 days'),
('msg-1-2', 'conv-1', 0, 'assistant', 'AI stands for Artificial Intelligence.', NOW() - INTERVAL '12 days' + INTERVAL '25 seconds'),
('msg-1-3', 'conv-1', 1, 'user', 'Give an example of AI in real life.', NOW() - INTERVAL '12 days' + INTERVAL '50 seconds'),
('msg-1-4', 'conv-1', 0, 'assistant', 'Sure, voice assistants like Siri or Alexa.', NOW() - INTERVAL '12 days' + INTERVAL '75 seconds'),
('msg-1-5', 'conv-1', 1, 'user', 'Got it, thanks!', NOW() - INTERVAL '12 days' + INTERVAL '100 seconds');

-- conv-2
INSERT INTO message VALUES
('msg-2-1', 'conv-2', 1, 'user', 'What is RAG in AI?', NOW() - INTERVAL '9 days'),
('msg-2-2', 'conv-2', 0, 'assistant', 'RAG stands for Retrieval-Augmented Generation.', NOW() - INTERVAL '9 days' + INTERVAL '30 seconds'),
('msg-2-3', 'conv-2', 1, 'user', 'Is it better than pure LLM?', NOW() - INTERVAL '9 days' + INTERVAL '60 seconds'),
('msg-2-4', 'conv-2', 0, 'assistant', 'Yes, when external knowledge is needed.', NOW() - INTERVAL '9 days' + INTERVAL '90 seconds'),
('msg-2-5', 'conv-2', 1, 'user', 'Interesting.', NOW() - INTERVAL '9 days' + INTERVAL '120 seconds');

-- conv-3
INSERT INTO message VALUES
('msg-3-1', 'conv-3', 1, 'user', 'How to implement chat UI in JavaFX?', NOW() - INTERVAL '6 days'),
('msg-3-2', 'conv-3', 0, 'assistant', 'Use VBox and ScrollPane for message flow.', NOW() - INTERVAL '6 days' + INTERVAL '25 seconds'),
('msg-3-3', 'conv-3', 1, 'user', 'And what about input field?', NOW() - INTERVAL '6 days' + INTERVAL '55 seconds'),
('msg-3-4', 'conv-3', 0, 'assistant', 'TextField + Button inside HBox.', NOW() - INTERVAL '6 days' + INTERVAL '85 seconds'),
('msg-3-5', 'conv-3', 1, 'user', 'Thanks for the tip!', NOW() - INTERVAL '6 days' + INTERVAL '115 seconds');

-- conv-4
INSERT INTO message VALUES
('msg-4-1', 'conv-4', 1, 'user', 'Can you explain MVC pattern?', NOW() - INTERVAL '3 days'),
('msg-4-2', 'conv-4', 0, 'assistant', 'Sure. Model-View-Controller separates concerns.', NOW() - INTERVAL '3 days' + INTERVAL '22 seconds'),
('msg-4-3', 'conv-4', 1, 'user', 'Which layer should handle DB?', NOW() - INTERVAL '3 days' + INTERVAL '45 seconds'),
('msg-4-4', 'conv-4', 0, 'assistant', 'DAO layer should interact with DB.', NOW() - INTERVAL '3 days' + INTERVAL '68 seconds'),
('msg-4-5', 'conv-4', 1, 'user', 'Good answer.', NOW() - INTERVAL '3 days' + INTERVAL '90 seconds');

-- conv-5
INSERT INTO message VALUES
('msg-5-1', 'conv-5', 1, 'user', 'Explain OpenAI GPT model.', NOW()),
('msg-5-2', 'conv-5', 0, 'assistant', 'GPT is a generative transformer-based model.', NOW() + INTERVAL '20 seconds'),
('msg-5-3', 'conv-5', 1, 'user', 'How many parameters?', NOW() + INTERVAL '40 seconds'),
('msg-5-4', 'conv-5', 0, 'assistant', 'GPT-3 has 175 billion.', NOW() + INTERVAL '60 seconds'),
('msg-5-5', 'conv-5', 1, 'user', 'Wow, impressive.', NOW() + INTERVAL '80 seconds'),
('msg-5-6', 'conv-5', 0, 'assistant', 'Indeed. That’s why it’s powerful.', NOW() + INTERVAL '100 seconds');
