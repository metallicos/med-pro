USE medical_appointment_db;

CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    date_naissance DATE,
    adresse VARCHAR(255),
    telephone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS medecins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    specialite VARCHAR(255),
    numero_licence VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS rendez_vous (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    medecin_id INT NOT NULL,
    date_heure DATETIME NOT NULL,
    motif TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (medecin_id) REFERENCES medecins(id) ON DELETE CASCADE
);

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
