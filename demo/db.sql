
CREATE DATABASE TransactionsDB;

USE TransactionsDB;

CREATE TABLE Transactions (
    TransactionID INT PRIMARY KEY,
    TransactionDate DATETIME,
    Amount DECIMAL(10, 2),
    Currency VARCHAR(3),
    TransactionType VARCHAR(10),
    Description VARCHAR(255),
);
