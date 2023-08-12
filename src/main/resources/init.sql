CREATE TABLE IF NOT EXISTS items(
    id serial PRIMARY KEY NOT NULL,
    name VARCHAR NOT NULL,
    price NUMERIC(21, 2) NOT NULL
);
-- 创建用户表
CREATE TABLE users (
    userid SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- 这应该是哈希过的密码
    email VARCHAR(255) UNIQUE NOT NULL,
    avatar VARCHAR(1024), -- 假设这是一个 URL，你可以根据需要调整长度
    description TEXT,
    register_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    login_time TIMESTAMP WITH TIME ZONE
);

-- 创建推文表
CREATE TABLE posts (
    postid SERIAL PRIMARY KEY,
    userid INTEGER REFERENCES users(userid) ON DELETE CASCADE,
    content TEXT NOT NULL,
    create_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    edit_time TIMESTAMP WITH TIME ZONE
);
