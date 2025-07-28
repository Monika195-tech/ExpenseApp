package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ExpenseDBHelper.createTables();

        VBox root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(15));

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(
            "Food", "Transport", "Utilities", "Shopping", "Health", "Groceries",
            "Entertainment", "Dining Out", "Education", "Rent", "Subscriptions",
            "Travel", "Fuel", "Savings", "Loan", "EMI", "Investment", "Charity", "Other"
        );
        categoryBox.setPromptText("Category");
        categoryBox.setEditable(true);

        TextField descField = new TextField();
        descField.setPromptText("Description");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date");

        Button saveBtn = new Button("Save");
        Button exportBtn = new Button("📥 Export to CSV");
        Button deleteBtn = new Button("🗑 Delete Selected");

        ComboBox<String> monthFilter = new ComboBox<>();
        monthFilter.getItems().addAll("All", "January", "February", "March", "April", "May",
                                      "June", "July", "August", "September", "October", "November", "December");
        monthFilter.setValue("All");

        VBox form = new VBox(10, amountField, categoryBox, descField, datePicker, saveBtn, exportBtn, deleteBtn);

        TableView<Expense> table = new TableView<>();

        TableColumn<Expense, Double> colAmt = new TableColumn<>("Amount");
        colAmt.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Expense, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Expense, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Expense, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(colAmt, colCat, colDesc, colDate);

        root.getChildren().addAll(form, monthFilter, new Label("📋 Expense History"), table, getCategoryPieChart());

        monthFilter.setOnAction(e -> {
            String selected = monthFilter.getValue();
            List<Expense> all = ExpenseDBHelper.getAllExpenses();
            ObservableList<Expense> filtered = FXCollections.observableArrayList();

            for (Expense ex : all) {
                if (selected.equals("All") || ex.getDate().toLowerCase().contains(selected.toLowerCase())) {
                    filtered.add(ex);
                }
            }
            table.setItems(filtered);
        });

        saveBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryBox.getValue();
                String desc = descField.getText();
                String date = datePicker.getValue().toString();

                ExpenseDBHelper.insertExpense(amount, category, desc, date);

                amountField.clear();
                categoryBox.setValue(null);
                descField.clear();
                datePicker.setValue(null);

                table.setItems(FXCollections.observableArrayList(ExpenseDBHelper.getAllExpenses()));
                root.getChildren().set(4, getCategoryPieChart());
                monthFilter.fireEvent(new javafx.event.ActionEvent());
            } catch (Exception ex) {
                System.out.println("Save Error: " + ex.getMessage());
            }
        });

        exportBtn.setOnAction(e -> {
            try {
                List<Expense> all = ExpenseDBHelper.getAllExpenses();
                StringBuilder sb = new StringBuilder();
                sb.append("Amount,Category,Description,Date\n");
                for (Expense ex : all) {
                    sb.append(ex.getAmount()).append(",")
                      .append(ex.getCategory()).append(",")
                      .append(ex.getDescription()).append(",")
                      .append(ex.getDate()).append("\n");
                }
                Files.write(Paths.get("expenses.csv"), sb.toString().getBytes());
                System.out.println("Exported to expenses.csv");
            } catch (Exception ex) {
                System.out.println("CSV Export Error: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            Expense selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                ExpenseDBHelper.deleteExpense(selected.getId());
                table.setItems(FXCollections.observableArrayList(ExpenseDBHelper.getAllExpenses()));
                root.getChildren().set(4, getCategoryPieChart());
                monthFilter.fireEvent(new javafx.event.ActionEvent());
            } else {
                System.out.println("No row selected to delete.");
            }
        });

        table.setItems(FXCollections.observableArrayList(ExpenseDBHelper.getAllExpenses()));

        Scene scene = new Scene(root, 550, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Expense Tracker PRO");
        primaryStage.show();
    }

    private PieChart getCategoryPieChart() {
        List<Expense> allExpenses = ExpenseDBHelper.getAllExpenses();
        Map<String, Double> totals = new HashMap<>();

        for (Expense ex : allExpenses) {
            totals.put(ex.getCategory(), totals.getOrDefault(ex.getCategory(), 0.0) + ex.getAmount());
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (String category : totals.keySet()) {
            data.add(new PieChart.Data(category, totals.get(category)));
        }

        PieChart pieChart = new PieChart(data);
        pieChart.setTitle("Spending by Category");
        return pieChart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}