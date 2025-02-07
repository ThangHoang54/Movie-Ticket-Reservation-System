package org.example.finalexam.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/movie_ticket_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "6Hs@9GM88t";
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
    }
//    private static HikariDataSource dataSource;
//
//    static {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:6543/postgres?user=postgres.oedvqryybzgfoaapesbk&password=x!aUah55x8pqYME&prepareThreshold=0");
//        config.setUsername("postgres");
//        config.setPassword("x!aUah55x8pqYME");
//
//        config.setMaximumPoolSize(15);
//        config.setMinimumIdle(5);
//
//        dataSource = new HikariDataSource(config);
//    }

//    /**
//     * Retrieves a connection from the HikariCP connection pool.
//     *
//     * @return a {@link Connection} object for interacting with the database
//     * @throws SQLException if a database access error occurs
//     */
//    public static Connection getConnection() throws SQLException {
//        return dataSource.getConnection();
//    }

    /**
     * Retrieves a connection from the HikariCP connection pool.
     *
     * @return a {@link Connection} object for interacting with the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Closes the HikariCP connection pool, releasing all database connections.
     *
     * <p>This method should be called when the application is shutting down to ensure proper cleanup of resources.</p>
     */
//    public static void closeDataSource() {
//        if (dataSource != null) {
//            dataSource.close();
//        }
//    }
    public static void closeDataSource() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close MySQL connection", e);
            }
        }
    }

//    /**
//     * Sets a custom HikariDataSource for the application.
//     *
//     * <p>This method allows overriding the default connection pool configuration, primarily used for testing or specialized use cases.</p>
//     *
//     * @param dataSource the {@link HikariDataSource} to be set
//     */
//    public static void setDataSource(HikariDataSource dataSource) {
//        DatabaseConnection.dataSource = dataSource;
//    }
}

