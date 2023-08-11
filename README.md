
# SQL API

This SQL API is a way for programs to interact with relational databases, such as MariaDB and SQLite, using code. Here are some examples to help you understand how to use the SQL API to query and update data in a database:

Establishing a connection
To establish a connection to a MariaDB database, you can use the following code:

```java
String url = "localhost";
int port = 3306;
String database = "database";
String username = "user";
String password = "password";

MariaDBConnection connection = new MariaDBConnection(url, port, database, username, password);
```

For SQLite, you can use the following code to connect to a database:

```java
String databaseName = "mydatabase.db";
String path = "path/to/the/database/folder";

SQLiteConnection connection = new SQLiteConnection(path, databaseName);
```
# Get data
Once you've established a connection, you can use the select() method to retrieve data from the database. Here's an example of how to retrieve all the rows from a table called customers in a MariaDB and SQLite database:

```java
ResultSet results = connection.select("customers", "*");

while (results.next()) {
    String name = results.getString("name");
    int age = results.getInt("age");
    String email = results.getString("email");
    System.out.println(name + ", " + age + ", " + email);
}
```
# Insert data
You can use the insertInto() method to insert data in the database. Here's an example of how to insert the email address for a customer with the ID of 12345 in a MariaDB and SQLite database:

```java
connection.insertInto("customers", "'mail@example.com', 12345");
```
# Updating data
You can use the update() method to modify data in the database. Here's an example of how to update the email address for a customer with the ID of 12345 in a MariaDB and SQLite database:

```java
connection.update("customers", "email='mail@example.com'", "id=12345");
```
# Deleting data
You can use the deleteFrom() method to delete data from the database. Here's an example of how to delete the email address for a customer with the ID of 12345 in a MariaDB and SQLite database:

```java
connection.deleteFrom("customers", "email='mail@example.com'", "id=12345");
```
# Creating tables
You can use the createTable() method to create a table into the database. Here's an example of how to create a table for customers in a MariaDB and SQLite database:

```java
connection.createTable("customers", "email varchar(255), id bigint(255)");
```

# Maven

```xml
<repository>
    <id>eztxm-repo</id>
    <url>https://maven.eztxm.de/repo</url>
</repository>

<dependency>
    <groupId>dev.eztxm.sql</groupId>
    <artifactId>SQL-API</artifactId>
    <version>VERSION</version>
</dependency>
```
