package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDBHelper {
    static final String DB_URL = "jdbc:sqlite:./expenses.db";

    public static void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "amount REAL, category TEXT, description TEXT, date TEXT)";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("DB Create Error: " + e.getMessage());
        }
    }

    public static void insertExpense(double amount, String category, String description, String date) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO expenses (amount, category, description, date) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setString(2, category);
            pstmt.setString(3, description);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    public static List<Expense> getAllExpenses() {
        List<Expense> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM expenses ORDER BY id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new Expense(
                    rs.getInt("id"),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getString("date")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get Error: " + e.getMessage());
        }
        return list;
    }

    public static void deleteExpense(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM expenses WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }
}