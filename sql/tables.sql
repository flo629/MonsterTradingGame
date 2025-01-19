
create table users
   (
       id VARCHAR(255) NOT NULL PRIMARY KEY,
       username VARCHAR(255) NOT NULL,
       password VARCHAR(255) NOT NULL,
       coin int NOT NULL,
       elo int,
       wins int DEFAULT 0,
       loses int DEFAULT 0,
       bio VARCHAR(255),
       image VARCHAR(255)
   );


create table cards
(
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    name card_name NOT NULL,
    type card_type NOT NULL,
    element element NOT NULL,
    card_rounds integer default 0 not null,
    card_wins   integer default 0 not null,
    card_damage integer           not null




);





create table ownership
(
    ownership_id serial,
    owner_id     VARCHAR(255)
        references users,
    date_start   timestamp                                                                  not null,
    date_end     timestamp default '2999-12-31 23:59:59.99999'::timestamp without time zone not null,
    card_id      VARCHAR(255)                                                                    not null
        references cards
);

create table battles
(
    battle_id   VARCHAR(255)
        primary key,
    user1_id    VARCHAR(255)   not null
        references users,
    user2_id    VARCHAR(255)   not null
        references users,
    battle_date timestamp not null,
    winner      integer   not null
);



create table battle_rounds
(
    round_id      VARCHAR(255)
        primary key,
    battle_id     VARCHAR(255) not null
        references battles,
    user1_card_id VARCHAR(255) not null
        references cards,
    user2_card_id VARCHAR(255) not null
        references cards,
    winner        integer not null
);


create table packages
(
    package_id  VARCHAR(255)
        primary key,
    card1_id2   varchar(255) not null
        references cards (id),
    card2_id2   varchar(255) not null
        references cards (id),
    card3_id2   varchar(255) not null
        references cards (id),
    card4_id2   varchar(255) not null
        references cards (id),
    card5_id2   varchar(255) not null
        references cards (id),
    is_availabe boolean default true
);

create table decks
(
    deck_id   varchar(255)
        primary key,
    card1_id2 varchar(255) not null
        references cards (id),
    card2_id2 varchar(255) not null
        references cards (id),
    card3_id2 varchar(255) not null
        references cards (id),
    card4_id2 varchar(255) not null
        references cards (id),
    user_id varchar(255) not null
        references users(id)
);


create table queue
(
    id      VARCHAR(255)
        primary key,
    user_id VARCHAR(255) not null
        references users
);


DROP TABLE IF EXISTS queue CASCADE;
DROP TABLE IF EXISTS decks CASCADE;
DROP TABLE IF EXISTS packages CASCADE;
DROP TABLE IF EXISTS battle_rounds CASCADE;
DROP TABLE IF EXISTS battles CASCADE;
DROP TABLE IF EXISTS ownership CASCADE;
DROP TABLE IF EXISTS cards CASCADE;
DROP TABLE IF EXISTS users CASCADE;


ALTER TABLE decks ADD CONSTRAINT unique_user_id UNIQUE (user_id);

ALTER TABLE users ADD COLUMN name VARCHAR(255);



UPDATE cards
SET card_damage = 100
WHERE id IN (
             '845f0dc7-37d0-426e-994e-43fc3ac83c08',
             '99f8f8dc-e25e-4a95-aa2c-782823f36e2a',
             'e85e3976-7c86-4d06-9a80-641c2019a79f',
             '171f6076-4eb5-4a7d-b3f2-2d650cc3d237'
    );


UPDATE users
SET coin = coin + 100
WHERE id = 'd968a8af-3e52-4092-b762-336e17d0a2e7';

