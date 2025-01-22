package org.example.finalexam.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class DatabaseConnection {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://aws-0-us-east-2.pooler.supabase.com:6543/postgres?user=postgres.hxzgmnjtldqraspjhbdo&password=x!aUah55x8pqYME&prepareThreshold=0");
        config.setUsername("postgres");
        config.setPassword("x!aUah55x8pqYME");
        config.setMaximumPoolSize(15);
        config.setMinimumIdle(5);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Retrieves a connection from the HikariCP connection pool.
     *
     * @return a {@link Connection} object for interacting with the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Closes the HikariCP connection pool, releasing all database connections.
     *
     * <p>This method should be called when the application is shutting down to ensure proper cleanup of resources.</p>
     */
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    /**
     * Sets a custom HikariDataSource for the application.
     *
     * <p>This method allows overriding the default connection pool configuration, primarily used for testing or specialized use cases.</p>
     *
     * @param dataSource the {@link HikariDataSource} to be set
     */
    public static void setDataSource(HikariDataSource dataSource) {
        DatabaseConnection.dataSource = dataSource;
    }
}
