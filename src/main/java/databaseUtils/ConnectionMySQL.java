package databaseUtils;

import constans.ConfigMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {

    private final Connection connection;


    ConnectionMySQL() throws SQLException {
        String dbURL = ConfigMySQL.DB_NAME_CLOUD;
        String username = ConfigMySQL.USERNAME_CLOUD;
        String password = ConfigMySQL.PASSWORD_CLOUD;
        this.connection = DriverManager.getConnection(dbURL, username, password);;
    }

    Connection getConnection() {
        return this.connection;
    }
}
