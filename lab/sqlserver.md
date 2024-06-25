
https://learn.microsoft.com/en-us/sql/linux/quickstart-install-connect-ubuntu?view=sql-server-ver16&tabs=ubuntu2204


/opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P 'YourStrong@Passw0rd'
    
```sql
USE master;
GO
CREATE DATABASE TestDB;
GO
USE TestDB;
GO
EXEC sys.sp_cdc_enable_db;
GO

-- Create a table and enable CDC on it
CREATE TABLE dbo.Employees (
    ID INT PRIMARY KEY,
    Name NVARCHAR(50),
    Position NVARCHAR(50)
);
GO
EXEC sys.sp_cdc_enable_table
    @source_schema = N'dbo',
    @source_name = N'Employees',
    @role_name = NULL,
    @capture_instance = N'dbo_Employees_New';
GO
```



# insert data
```sql
INSERT INTO dbo.Employees (ID, Name, Position) VALUES (1, 'John Doe', 'Senior Software Engineer');
INSERT INTO dbo.Employees (ID, Name, Position) VALUES (2, 'Jane Doe', 'Database Administrator');
INSERT INTO dbo.Employees (ID, Name, Position) VALUES (3, 'Alice Doe', 'Software Engineer');
GO
```

# select data
```sql
SELECT * FROM dbo.Employees;
GO
```

# update data
```sql
UPDATE dbo.Employees SET Position = 'Manager' WHERE ID = 1;
GO
```

# delete data
```sql
DELETE FROM dbo.Employees WHERE ID = 2;
GO
```



<!-- ----------------------------------------------------------- -->

```sql
USE TestDB;
GO

-- Check if CDC is enabled on the database
SELECT name, is_cdc_enabled
FROM sys.databases
WHERE name = 'TestDB';
GO

-- Check if CDC is enabled on the table
SELECT name, is_tracked_by_cdc
FROM sys.tables
WHERE name = 'Employees';
GO
```