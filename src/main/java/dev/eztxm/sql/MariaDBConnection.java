package dev.eztxm.sql;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class MariaDBConnection {
    private Connection connection;
    private Timer timer;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public MariaDBConnection(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
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
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
        }
    }

    public void createTable(String tableName, String columns) {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String tableName, String column, Object value, String condition) {
        String sql = "UPDATE " + tableName + " SET " + column + " = " + value + " WHERE " + condition;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertInto(String tableName, String values) {
        String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFrom(String tableName, String condition) {
        String sql = "DELETE FROM " + tableName + " WHERE " + condition;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
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
