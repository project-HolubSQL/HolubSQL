package com.holub.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.holub.database.*;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        // Directory where the database is stored
        File dbDirectory = new File("path/to/database_directory");

        // Create users table
        TableFactory factory = new TableFactory();
        Table users = factory.create("users", new String[] { "username", "password" });
        // Add users to the table as necessary
        users.insert(new Object[] { "admin", "admin123" });
        // ... More user initialization ...

        // Export table to the database directory
        try {
            users.export(new CSVExporter(new FileWriter(new File(dbDirectory, "users.csv"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}