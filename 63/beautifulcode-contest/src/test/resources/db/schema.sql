CREATE SCHEMA account;
CREATE TABLE account.transaction
(
    id     UUID PRIMARY KEY,
    amount NUMERIC      NOT NULL,
    date   timestamptz  NOT NULL,
    status VARCHAR(100) NOT NULL
);