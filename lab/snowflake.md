
# Create private and public keys

```bash
openssl genrsa -out rsa_key.pem 2048
openssl rsa -in rsa_key.pem -pubout -out rsa_key.pub
```

# Configure the public key in Snowflake

```sql
alter user {User_name} set rsa_public_key='{Put the Public key content here}';
desc user {User_name};
```
