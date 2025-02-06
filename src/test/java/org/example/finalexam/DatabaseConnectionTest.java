package org.example.finalexam;

import org.example.finalexam.utils.DatabaseConnection;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class DatabaseConnectionTest {
    @Mock
    private Connection mockConnection;

    @Mock
    private HikariDataSource mockDataSource;

    // AutoCloseable resource for initializing and closing mocks
    // InjectMocks not used because static methods are being tested
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    /**
     * Tests `DatabaseConnection.getConnection()` method
     * Mocks `DriverManager.getConnection()` method to return a mock `Connection`
     *
     * @throws SQLException if database access error occurs during test
     */
    @Test
    public void testGetConnection_Success() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        //DatabaseConnection.setDataSource(mockDataSource);
        Connection conn = DatabaseConnection.getConnection();
        Assertions.assertNotNull(conn);
        Assertions.assertSame(mockConnection, conn);
    }

    /**
     * Tests `DatabaseConnection.closeConnection()` method and verify
     * `close()` method is called only once
     */
    @Test
    public void testCloseDataSource_Success() {
        //DatabaseConnection.setDataSource(mockDataSource);
        DatabaseConnection.closeDataSource();
        verify(mockDataSource, times(1)).close();
    }

    /**
     * Tests `DatabaseConnection.closeConnection()` method when `null` is passed
     * and verify no `close()` method is called on null connection
     */
    @Test
    public void testCloseDataSource_NullDataSource() {
        //DatabaseConnection.setDataSource(null);
        Assertions.assertDoesNotThrow(DatabaseConnection::closeDataSource);
        verify(mockDataSource, never()).close();
    }
}
