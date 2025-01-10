

CREATE TABLE IF NOT EXISTS users(
                                    id VARCHAR(255) NOT NULL,
                                    username VARCHAR(255) PRIMARY KEY,
                                    password VARCHAR(255) NOT NULL,
                                    coin int NOT NULL



);


create table Session (
                         username varchar(255) NOT NULL,
                         token varchar(255) NOT NULL,
                         FOREIGN KEY (username) references users(username)
);



DROP TABLE users;
DROP TABLE Session;
ALTER TABLE users ADD coin INT;
ALTER TABLE users ADD bio VARCHAR(255);
ALTER TABLE users ADD image VARCHAR(255);
ALTER TABLE users ADD name VARCHAR(255);

INSERT INTO users (id, username, password)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'affe', 'okecool');

INSERT INTO users VALUES ('okeeefd', 'nein','hilfe');