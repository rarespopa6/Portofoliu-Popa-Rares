CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15),
    password     VARCHAR(255) NOT NULL,
    type         VARCHAR(20)  NOT NULL,
    salary       DECIMAL(10, 2),
    role         VARCHAR(50)
);

CREATE TABLE accounts (
      id SERIAL PRIMARY KEY,
      balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
      creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      type VARCHAR(20) NOT NULL,
      transaction_fee DECIMAL(10, 2),
      interest_rate DECIMAL(5, 2),
      monthly_withdrawal_limit INT
);

CREATE TABLE accountuser (
         account_id INT,
         user_id INT,
         PRIMARY KEY (account_id, user_id),
         FOREIGN KEY (account_id) REFERENCES accounts(id),
         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE coownership_request (
         id SERIAL PRIMARY KEY,
         account_id INT,
         requester_id INT,
         owner_id INT,
         approved BOOLEAN DEFAULT FALSE,
         FOREIGN KEY (account_id) REFERENCES accounts(id),
         FOREIGN KEY (requester_id) REFERENCES users(id),
         FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE account_logs (
          id SERIAL PRIMARY KEY,
          account_id INT NOT NULL,
          message TEXT NOT NULL,
          FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);
