

CREATE TABLE IF NOT EXISTS users(
                                    id VARCHAR(255) PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
                                    password VARCHAR(255) NOT NULL



);


ALTER TABLE users ADD coin INT;

INSERT INTO users (id, name, password)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'affe', 'okecool');

INSERT INTO users VALUES ('okeeefd', 'nein','hilfe');