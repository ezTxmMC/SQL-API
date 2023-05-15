package dev.eztxm.sql;

import java.sql.*;

public class MariaDBConnection {
    private Connection connection;
    private final String url;
    private final String username;
    private final String password;

    public MariaDBConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        connect();
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database. Retrying in 5 seconds...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            connect();
        }
    }

    public void createTable(String tableName, String columns) {
        String sql = "CREATE TABLE " + tableName + " (" + columns + ")";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("Table " + tableName + " created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String tableName, String column, String value, String condition) {
        String sql = "UPDATE " + tableName + " SET " + column + " = '" + value + "' WHERE " + condition;
        try {
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            System.out.println(rowsAffected + " rows updated in table " + tableName + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertInto(String tableName, String values) {
        String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";
        try {
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            System.out.println(rowsAffected + " rows inserted into table " + tableName + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFrom(String tableName, String condition) {
        String sql = "DELETE FROM " + tableName + " WHERE " + condition;
        try {
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            System.out.println(rowsAffected + " rows deleted from table " + tableName + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String tableName, String columns, String condition) {
        String sql = "SELECT " + columns + " FROM " + tableName + " WHERE " + condition;
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
