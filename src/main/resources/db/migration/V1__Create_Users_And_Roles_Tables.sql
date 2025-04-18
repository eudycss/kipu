-- Create Roles Table
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

-- Create Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create User_Roles Junction Table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert Default Roles
INSERT INTO roles (name) VALUES
    ('ROLE_DOCENTE'),
    ('ROLE_RECTOR'),
    ('ROLE_ADMIN');

-- Create Index for faster role lookups
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id); 