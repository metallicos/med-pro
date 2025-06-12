-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT chk_role CHECK (role IN ('ADMIN', 'DOCTOR', 'STAFF'))
);

-- Create index for username lookups
CREATE INDEX idx_username ON users(username);

-- Insert default admin user (password: admin123)
-- Note: In production, use a proper password hashing mechanism
INSERT INTO users (username, password_hash, role) 
VALUES ('admin', 'admin123', 'ADMIN');
