CREATE TABLE user (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    password VARCHAR(64) NOT NULL,
    sessionkey VARCHAR(64),
    sessiontime NUMERIC
);

CREATE INDEX "id" ON "user" ("id");

CREATE TABLE direction (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    move VARCHAR(64) NOT NULL,
    message VARCHAR(64),
    movetime NUMERIC
);

CREATE TABLE log (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    lat NUMERIC,
    lon NUMERIC,
    logtime NUMERIC
);

