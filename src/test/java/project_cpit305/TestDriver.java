/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_cpit305;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class TestDriver implements Driver {

    public static final String URL =
            "jdbc:mysql://localhost:3306/project";

    private final Connection connection;
    private SQLException connectionError;

    // Receive the mock connection from the test.
    public TestDriver(Connection connection) {
        this.connection = connection;
    }

    // Use this when testing a connection failure.
    public void failConnection() {
        connectionError = new SQLException("Connection failed");
    }

    @Override
    public Connection connect(String url, Properties info)
            throws SQLException {

        if (!acceptsURL(url)) {
            return null;
        }

        if (connectionError != null) {
            throw connectionError;
        }

        return connection;
    }

    @Override
    public boolean acceptsURL(String url) {
        return URL.equals(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(
            String url, Properties info) {

        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() {
        return Logger.getGlobal();
    }
}