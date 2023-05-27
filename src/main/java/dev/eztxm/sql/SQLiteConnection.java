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
        put(sql);
    }

    public void update(String tableName, String column, Object value, String condition) {
        String sql = "UPDATE " + tableName + " SET " + column + " = " + value + " WHERE " + condition;
        put(sql);
    }

    public void insertInto(String tableName, String values) {
        String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";
        put(sql);
    }

    public void deleteFrom(String tableName, String condition) {
        String sql = "DELETE FROM " + tableName + " WHERE " + condition;
        put(sql);
    }

    public ResultSet select(String tableName, String columns, String condition) {
        String sql = "SELECT " + columns + " FROM " + tableName + " WHERE " + condition;
        try (ResultSet resultSet = query(sql)) {
            return resultSet;
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

    public ResultSet query(String sql, Object... objects) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            setArguments(objects, preparedStatement);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String sql, Object... objects) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            setArguments(objects, preparedStatement);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setArguments(Object[] objects, PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object instanceof String) {
                preparedStatement.setString(i + 1, (String)object);
                return;
            }
            if (object instanceof Integer) {
                preparedStatement.setInt(i + 1, (Integer) object);
                return;
            }
            if (object instanceof Date) {
                preparedStatement.setDate(i + 1, (Date)object);
                return;
            }
            if (object instanceof Timestamp) {
                preparedStatement.setTimestamp(i + 1, (Timestamp)object);
                return;
            }
            if (object instanceof Boolean) {
                preparedStatement.setBoolean(i + 1, (Boolean) object);
                return;
            }
            if (object instanceof Float) {
                preparedStatement.setFloat(i + 1, (Float) object);
                return;
            }
            if (object instanceof Double) {
                preparedStatement.setDouble(i + 1, (Double) object);
                return;
            }
            if (object instanceof Long) {
                preparedStatement.setLong(i + 1, (Long) object);
                return;
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
