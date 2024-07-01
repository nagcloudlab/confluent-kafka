import pandas as pd
from faker import Faker
import pyodbc

# Initialize Faker
fake = Faker()

# Define possible transaction types
transaction_types = ['DEBIT', 'CREDIT']

# Generate a list of random transactions
def generate_transactions(num_transactions):
    transactions = []
    for _ in range(num_transactions):
        transaction = {
            'TransactionID': fake.unique.random_int(min=1, max=100000),
            'TransactionDate': fake.date_time_this_year().strftime('%Y-%m-%d %H:%M:%S'),
            'Amount': round(fake.random_number(digits=4) / 100, 2),
            'Currency': fake.currency_code(),
            'Description': fake.sentence(nb_words=6),
            'TransactionType': fake.random_element(transaction_types)
        }
        transactions.append(transaction)
    return transactions

# Create a DataFrame
num_transactions = 100
transactions = generate_transactions(num_transactions)
df = pd.DataFrame(transactions)

# Print a sample of the generated data
print(df.head())

# SQL Server connection
conn = pyodbc.connect('DRIVER={ODBC Driver 17 for SQL Server};'
                      'SERVER=localhost;'
                      'DATABASE=TransactionsDB;'
                      'UID=sa;'
                      'PWD=YourStrong@Passw0rd')

cursor = conn.cursor()

# Insert transactions into the database
for index, row in df.iterrows():
    cursor.execute("""
                   INSERT INTO Transactions (TransactionID, TransactionDate, Amount, Currency, Description, TransactionType)
                   VALUES (?, ?, ?, ?, ?, ?)
                   """,
                   row['TransactionID'], row['TransactionDate'], row['Amount'], row['Currency'], row['Description'], row['TransactionType'])

# Commit the transaction
conn.commit()

# Close the connection
cursor.close()
conn.close()

print("Transactions inserted into SQL Server successfully.")
