CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS book
(
    id uuid NOT NULL,
    title VARCHAR(256) NOT NULL,
    year SMALLINT NOT NULL,
    genre VARCHAR(32) NOT NULL,
    pages SMALLINT NOT NULL,
    publisher VARCHAR(32) NOT NULL,
    file VARCHAR(256),
    added TIMESTAMP NOT NULL,
    modified TIMESTAMP NOT NULL,
    file_added TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS author
(
    id uuid NOT NULL,
    firstname VARCHAR(45) NOT NULL,
    lastname VARCHAR(45) NOT NULL,
    middlename VARCHAR(45),
    birth_date VARCHAR(16) NOT NULL,
    death_date VARCHAR(16),
    description VARCHAR(2048),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS book_has_author
(
    book_id uuid NOT NULL,
    author_id uuid NOT NULL,
    CONSTRAINT book_fk
        FOREIGN KEY (book_id)
            REFERENCES book(id),
    CONSTRAINT author_fk
        FOREIGN KEY (author_id)
            REFERENCES author(id)
);