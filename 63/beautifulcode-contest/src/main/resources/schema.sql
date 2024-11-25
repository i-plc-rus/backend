CREATE SCHEMA IF NOT EXISTS account;
CREATE TABLE IF NOT EXISTS account.transaction
(
    id     UUID PRIMARY KEY,
    amount NUMERIC      NOT NULL,
    date   timestamptz  NOT NULL,
    status VARCHAR(100) NOT NULL
);