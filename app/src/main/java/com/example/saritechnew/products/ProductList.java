package com.example.saritechnew.products;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductList {
    public static void main(String[] args) {
        // Connection string for the SQLite database file
        String connectionString = "jdbc:sqlite:/path/to/ProductList.db";

        Connection connection = null;
        Statement statement = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(connectionString);

            // Create a statement object
            statement = connection.createStatement();

            // Create the table
            String createTableSql = "CREATE TABLE products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "price REAL NOT NULL," +
                    "photo_path TEXT," +
                    "quantity INTEGER" +
                    ")";
            statement.execute(createTableSql);

            System.out.println("ProductList database created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the statement and connection
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}