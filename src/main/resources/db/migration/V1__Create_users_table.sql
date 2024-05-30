CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       version BIGINT,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
