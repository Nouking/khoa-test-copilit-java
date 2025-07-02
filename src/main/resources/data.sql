-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Insert some sample data
INSERT INTO users (username, name, email) VALUES 
('john_doe', 'John Doe', 'john@example.com'),
('jane_smith', 'Jane Smith', 'jane@example.com');