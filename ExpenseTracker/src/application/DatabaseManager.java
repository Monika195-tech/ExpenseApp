package application;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createTables() {
        String categoryTable = "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE)";

        String expenseTable = "CREATE TABLE IF NOT EXISTS expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, " +
                "category_id INTEGER, " +
                "description TEXT, " +
                "date TEXT, " +
                "FOREIGN KEY(category_id) REFERENCES categories(id))";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(categoryTable);
            stmt.execute(expenseTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertExpense(double amount, String category, String description, String date) {
        try (Connection conn = connect()) {
            // Insert category if not exists
            PreparedStatement psCategory = conn.prepareStatement(
                    "INSERT OR IGNORE INTO categories(name) VALUES (?)");
            psCategory.setString(1, category);
            psCategory.executeUpdate();

            // Get category_id
            PreparedStatement psGetId = conn.prepareStatement(
                    "SELECT id FROM categories WHERE name = ?");
            psGetId.setString(1, category);
            ResultSet rs = psGetId.executeQuery();
            int categoryId = rs.next() ? rs.getInt("id") : 0;

            // Insert expense
            PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO expenses(amount, category_id, description, date) VALUES (?, ?, ?, ?)");
            psInsert.setDouble(1, amount);
            psInsert.setInt(2, categoryId);
            psInsert.setString(3, description);
            psInsert.setString(4, date);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}