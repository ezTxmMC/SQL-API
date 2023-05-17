package dev.eztxm.sql;

import java.io.File;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class SQLiteConnection {
    private Connection connection;
    private Timer timer;
    private final String path;
    private final String fileName;

    public SQLiteConnection(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
        if (!new File(path).exists()) new File(path).mkdirs();
        connect();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkConnection();
            }
        }, 1000, 1000);
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path + "/" + fileName);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
        }
    }

    public void createTable(String tableName, String columns) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("Table " + tableName + " created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String tableName, String column, Object value, String condition) {
        String sql = "UPDATE " + tableName + " SET " + column + " = " + value + " WHERE " + condition;
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
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkConnection() {
        try {
            if (!connection.isValid(1)) {
                System.out.println("Connection lost. Reconnecting...");
                reconnect();
            }
        } catch (SQLException e) {
            System.out.println("Connection lost. Reconnecting...");
            reconnect();
        }
    }

    private void reconnect() {
        try {
            connection.close();
        } catch (SQLException ignored) {}
        connect();
    }

    public void stopChecking() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connection checking stopped.");
    }
}
