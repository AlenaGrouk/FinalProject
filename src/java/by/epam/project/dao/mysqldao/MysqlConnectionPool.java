/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.dao.mysqldao;

import by.epam.project.exception.DaoConnectException;
import by.epam.project.manager.ConfigurationManager;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
class MysqlConnectionPool {
    private static final Logger LOGGER = Logger.getLogger(MysqlConnectionPool.class);
    private static final String DB_USER_NAME;
    private static final  String DB_PASSWORD ;
    private static final  String DB_URL;
    private static final  String DB_DRIVER;
    private static final  Integer DB_MAX_CONNECTIONS;
    
    static {
        DB_USER_NAME = ConfigurationManager.getProperty("db.username");
        DB_PASSWORD = ConfigurationManager.getProperty("db.password");
        DB_URL = ConfigurationManager.getProperty("db.url");
        DB_DRIVER = ConfigurationManager.getProperty("db.driver");
        DB_MAX_CONNECTIONS = Integer.decode(ConfigurationManager.getProperty("db.maxconnect"));
    }
    
    private static MysqlConnectionPool pool;
    private static BlockingQueue<ProxyConnection> queue;
    private static final Lock LOCK = new ReentrantLock();

    static void destroy() {
        while (! queue.isEmpty()) {
            try {
                ProxyConnection conn = queue.take();
                if (conn != null && conn.isValid(1111)) {
                    conn.reallyClose();
                }
            } catch (InterruptedException | SQLException ex) {
                LOGGER.error(ex);
            }
        }
    }
    
    private MysqlConnectionPool() throws DaoConnectException{
        if (DB_MAX_CONNECTIONS == null || DB_MAX_CONNECTIONS <= 0) {
            throw new DaoConnectException();
        }
        
        queue = new ArrayBlockingQueue<>(DB_MAX_CONNECTIONS);
        for (int i = 0; i < DB_MAX_CONNECTIONS; i++) {
            queue.offer(createNewConnection());
        }
    }

       
    private ProxyConnection createNewConnection() throws DaoConnectException{
        ProxyConnection conn = null;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = (Connection) DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
            connection.setAutoCommit(false);
            conn = new ProxyConnection(connection);
        } catch (SQLException | ClassNotFoundException ex) {
            throw new DaoConnectException(ex);
        }
        return conn;
    }
    
    private static void checkPool() throws DaoConnectException {
        if (pool == null) {
            LOCK.lock();
            try {
                if (pool == null) {
                    pool = new MysqlConnectionPool();
                }
            } finally {
                LOCK.unlock();
            }
        }
    }
    
    static Connection getConnection() throws DaoConnectException {
        checkPool();
        
        Connection conn = null;
        try {
            conn = queue.take();
        } catch (InterruptedException ex) {
            throw new DaoConnectException(ex);
        }
       
        return conn;
    }
    
    static void returnConnection(Connection conn) throws DaoConnectException {
        try {
            if (conn == null || !conn.isValid(10000)) {
                throw new DaoConnectException();
            }
        } catch (SQLException ex) {
            throw new DaoConnectException(ex);
        }
        
        try {
            queue.offer((ProxyConnection) conn);
        } catch (Exception ex) {
            throw new DaoConnectException(ex);
        }
    }

    private static class ProxyConnection implements Connection {
    
    private final Connection connection;
    
    ProxyConnection(Connection connection) { // только в пакете
        this.connection = connection;
    }

    private void reallyClose() throws SQLException{
        connection.close();
    }
    
    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Override
    public void close() throws SQLException {
        throw new SQLException("ERROR");
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareCall(sql);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback();
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return connection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return connection.isWrapperFor(iface);
    }
    }

}

