package ru.vladushik.vkusvillstoremanagement.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost/test", "postgres", "root");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
