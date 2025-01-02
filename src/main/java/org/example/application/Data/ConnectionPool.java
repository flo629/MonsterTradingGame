package org.example.application.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {


    private final static String URL = "jdbc:postgresql://localhost:5432/swen1";
    private final static String USER = "swen1";
    private final static String PASSWORD = "swen1";


    public  Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
