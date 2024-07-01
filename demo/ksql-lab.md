

../confluent-7.6.1/bin/ksql http://localhost:8088

CREATE STREAM TransactionsStream (
    TransactionID INT,
    TransactionDate STRING,
    Amount DOUBLE,
    Currency STRING,
    Description STRING,
    TransactionType STRING
) WITH (
    KAFKA_TOPIC='transactions_topic',
    VALUE_FORMAT='AVRO'
);

CREATE STREAM DebitTransactions WITH (KAFKA_TOPIC='debit_transactions_topic') AS \
SELECT * FROM TransactionsStream WHERE TransactionType = 'DEBIT';
