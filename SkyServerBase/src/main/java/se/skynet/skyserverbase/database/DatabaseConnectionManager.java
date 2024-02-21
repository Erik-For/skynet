package se.skynet.skyserverbase.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private String host = System.getenv("DB_HOST");
    private String port = "3306";
    private String database = System.getenv("DB_DATABASE");
    private String username = System.getenv("DB_USERNAME");
    private String password = System.getenv("DB_PASSWORD");

    private Connection connection;

    public void connect(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("FATAL: Could not connect to the database");
            System.exit(1);
        }
    }

    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("FATAL: Could not disconnect from the database");
            System.exit(1);
        }
    }
    public Connection getConnection(){
        return connection;
    }

}