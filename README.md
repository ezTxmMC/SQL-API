
# SQL API

This SQL API is a way for programs to interact with relational databases, such as MariaDB and SQLite, using code. Here are some examples to help you understand how to use the SQL API to query and update data in a database:

Establishing a connection
To establish a connection to a MariaDB database, you can use the following code:

```java
String url = "localhost";
int port = 3306;
String database = "mydatabase";
String username = "myuser";
String password = "mypassword";

MariaDBConnection connection = new MariaDBConnection(url, port, database, username, password);
```

For SQLite, you can use the following code to connect to a database:

```java
String databaseName = "mydatabase.db";
String path = "/path/to/my/database/folder";

SQLiteConnection connection = new SQLiteConnection(path, databaseName);
```
# Querying data
Once you've established a connection, you can use the query() method to retrieve data from the database. Here's an example of how to retrieve all the rows from a table called customers in a MariaDB and SQLite database:

```java
String sqlQuery = "SELECT * FROM customers";

ResultSet results = connection.query(sqlQuery);

while (results.next()) {
    String name = results.getString("name");
    int age = results.getInt("age");
    String email = results.getString("email");
    System.out.println(name + ", " + age + ", " + email);
}
```
# Updating data
You can use the update() method to modify data in the database. Here's an example of how to update the email address for a customer with the ID of 12345 in a MariaDB and SQLite database:

```java
String sqlQuery = "UPDATE customers SET email='newemail@example.com' WHERE id=12345";

int rowsAffected = connection.update(sqlQuery);

System.out.println(rowsAffected + " rows were updated.");
```

Overall, the SQL API provides a powerful way to interact with relational databases using

# Maven

```xml
<repository>
    <id>eztxm-repo</id>
    <url>https://eztxm.dev/repo</url>
</repository>

<dependency>
    <groupId>dev.eztxm.sql</groupId>
    <artifactId>SQL-API</artifactId>
    <version>1.0</version>
</dependency>
```
