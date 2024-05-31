package org.voorraadbeheer.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase {
    private static final String URL = "jdbc:sqlite:voorraadbeheer.db";

    // Method to establish connection to the SQLite database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Method to create a new database table for products
    public static void createNewDatabase() {
        try (Connection conn = connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS products (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + " name TEXT NOT NULL,\n"
                        + " quantity INTEGER NOT NULL,\n"
                        + " price REAL\n"
                        + ");";
                stmt.execute(sql);
                System.out.println("Table 'products' has been created or already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating table 'products': " + e.getMessage());
        }
    }
}
