



kafka ui
---------

```bash
mkdir kafka-ui
curl -L https://github.com/provectus/kafka-ui/releases/download/v0.7.2/kafka-ui-api-v0.7.2.jar --output kafka-ui/kafka-ui-api-v0.7.2.jar
```


```properties
kafka:
  clusters:
    - name: local
      bootstrapServers: localhost:9092
```

```bash
touch kafka-ui/application.yml
```

```bash
java -Dspring.config.additional-location=application.yml --add-opens java.rmi/javax.rmi.ssl=ALL-UNNAMED -jar kafka-ui-api-v0.7.2.jar
```
