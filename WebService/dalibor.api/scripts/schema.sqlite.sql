CREATE TABLE user (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    password VARCHAR(64) NOT NULL
);

CREATE INDEX "id" ON "user" ("id");