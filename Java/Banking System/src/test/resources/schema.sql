CREATE TABLE IF NOT EXISTS account (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         balance DECIMAL(10, 2) NOT NULL
);
