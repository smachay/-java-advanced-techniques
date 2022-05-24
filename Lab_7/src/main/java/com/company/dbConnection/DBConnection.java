package com.company.dbConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static String DB_URL;
    private static String DB_PASSWORD;
    private static String DB_USERNAME;
    private static Properties props = new Properties();
    private static Connection connection = null;


    public DBConnection(){
        try {
            Class.forName("org.postgresql.Driver");
            props.load(new FileInputStream("src/main/resources/database.properties"));
            DB_URL = props.getProperty("db.url");
            DB_USERNAME = props.getProperty("db.username");
            DB_PASSWORD = props.getProperty("db.password");
            connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

        } catch (IOException | SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        } catch (ClassNotFoundException  e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() {
        return connection;
    }
}
