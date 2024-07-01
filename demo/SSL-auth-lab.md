

-----------------------------------------------------
Security : encryption
-----------------------------------------------------


Steps:

1. Generate CA
2. Create Truststore
3. Create Keystore
4. Create certificate signing request (CSR)
5. Sign the CSR
6. Import the CA into Keystore
7. Import the signed certificate from step 5 into Keystore

-----------------------------------------------------


1. Generate CA
openssl req -new -x509 -keyout ca-key -out ca-cert -days 365


-----------------------------------------------------
generate keystore and truststore for kafka broker-1
-----------------------------------------------------


2. Create Truststore
keytool -keystore kafka.broker-1.truststore.jks -alias ca-cert -import -file ca-cert

3. Create Keystore
keytool -keystore kafka.broker-1.keystore.jks -alias broker-1 -validity 3650 -genkey -keyalg RSA -ext SAN=dns:localhost

4. Create certificate signing request (CSR)
keytool -keystore kafka.broker-1.keystore.jks -alias broker-1 -certreq -file ca-request-broker-1

5. Sign the CSR
openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-broker-1 -out ca-signed-broker-1 -days 3650 -CAcreateserial

6. Import the CA into Keystore
keytool -keystore kafka.broker-1.keystore.jks -alias ca-cert -import -file ca-cert

7. Import the signed certificate from step 5 into Keystore
keytool -keystore kafka.broker-1.keystore.jks -alias broker-1 -import -file ca-signed-broker-1


----------------------------------------------------------
generate keystore and truststore for kafka client  
----------------------------------------------------------

2. Create Truststore
keytool -keystore kafka.client.truststore.jks -alias ca-cert -import -file ca-cert

3. Create Keystore
keytool -keystore kafka.client.keystore.jks -alias client -validity 3650 -genkey -keyalg RSA -ext SAN=dns:localhost

4. Create certificate signing request (CSR)
keytool -keystore kafka.client.keystore.jks -alias client -certreq -file ca-request-client

5. Sign the CSR
openssl x509 -req -CA ca-cert -CAkey ca-key -in ca-request-client -out ca-signed-client -days 3650 -CAcreateserial

6. Import the CA into Keystore
keytool -keystore kafka.client.keystore.jks -alias ca-cert -import -file ca-cert

7. Import the signed certificate from step 5 into Keystore
keytool -keystore kafka.client.keystore.jks -alias client -import -file ca-signed-client



-----------------------------------------------------

broker-1 server.properties

inter broker communication over plaintext & client communication over ssl
with listener.security.protocol.map

```properties
listeners=PLAINTEXT://localhost:9092,SSL://localhost:9093
advertised.listeners=PLAINTEXT://localhost:9092,SSL://localhost:9093
listener.security.protocol.map=PLAINTEXT:PLAINTEXT,SSL:SSL
ssl.keystore.location=/home/centos/kafka_2.13-3.7.0/kafka.broker-1.keystore.jks
ssl.keystore.password=broker-1
ssl.key.password=broker-1
```


kafka-client properties

```properties
security.protocol=SSL
ssl.truststore.location=/home/centos/kafka_2.13-3.7.0/kafka.client.truststore.jks
ssl.truststore.password=broker-1
```


-----------------------------------------------------
Security : mutual authentication (mTLS) 
-----------------------------------------------------


broker-1 server.properties

inter broker communication over plaintext & client communication over ssl
with listener.security.protocol.map

```properties
listeners=PLAINTEXT://localhost:9092,SSL://localhost:9093
advertised.listeners=PLAINTEXT://localhost:9092,SSL://localhost:9093
listener.security.protocol.map=PLAINTEXT:PLAINTEXT,SSL:SSL
ssl.keystore.location=/home/centos/kafka_2.13-3.7.0/kafka.broker-1.keystore.jks
ssl.keystore.password=broker-1
ssl.key.password=broker-1

ssl.client.auth=required
ssl.truststore.location=/home/centos/kafka_2.13-3.7.0/kafka.broker-1.truststore.jks
ssl.truststore.password=broker-1

```

kafka-client properties

```properties
security.protocol=SSL
ssl.keystore.location=/home/centos/kafka_2.13-3.7.0/kafka.client.keystore.jks
ssl.keystore.password=broker-1
ssl.truststore.location=/home/centos/kafka_2.13-3.7.0/kafka.client.truststore.jks
ssl.truststore.password=broker-1
```

-----------------------------------------------------