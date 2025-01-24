package org.example.Controllers.Membre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class TreasuryManagementController {

    @FXML
    private TextField amountField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button addMoneyButton;

    @FXML
    private Button payCotisationButton;

    @FXML
    private Label cotisationStatusLabel;

    @FXML
    private TableView<FinancialOperation> financialTable;

    @FXML
    private TableColumn<FinancialOperation, String> dateColumn;

    @FXML
    private TableColumn<FinancialOperation, Double> amountColumn;

    @FXML
    private TableColumn<FinancialOperation, String> typeColumn;

    @FXML
    private TableColumn<FinancialOperation, String> descriptionColumn;

    private ObservableList<FinancialOperation> financialData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind TableView columns to FinancialOperation properties
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Set the TableView's items
        financialTable.setItems(financialData);

        // Example: Adding an initial transaction
        financialData.add(new FinancialOperation(LocalDate.now().toString(), 100.0, "Dépôt", "Initial deposit"));
    }

    @FXML
    private void handleAddMoney() {
        String amountText = amountField.getText();
        String description = descriptionField.getText();

        if (!amountText.isEmpty() && !description.isEmpty()) {
            double amount = Double.parseDouble(amountText);

            // Add to the TableView
            financialData.add(new FinancialOperation(LocalDate.now().toString(), amount, "Dépôt", description));

            // Clear fields
            amountField.clear();
            descriptionField.clear();
        }
    }

    @FXML
    private void handlePayCotisation() {
        // Example: Pay cotisation logic
        financialData.add(new FinancialOperation(LocalDate.now().toString(), 50.0, "Cotisation", "Cotisation annuelle"));
        cotisationStatusLabel.setText("Payée");
        cotisationStatusLabel.setStyle("-fx-text-fill: green;");
    }
}
