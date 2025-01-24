package org.example.Controllers.Membre;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TreasureManagementController {

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
    private TableView<Map<String, Object>> financialTable;

    @FXML
    private TableColumn<Map<String, Object>, String> dateColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> amountColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> typeColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> descriptionColumn;

    private ObservableList<Map<String, Object>> financialData = FXCollections.observableArrayList();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "Storage/treasure.json"; // Update with your actual JSON file path

    @FXML
    public void initialize() {
        // Set up the TableView columns
        dateColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("date");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        amountColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("amount");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        typeColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("type");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        descriptionColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("description");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        // Populate the TableView
        try {
            loadFinancialData();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load financial operations: " + e.getMessage());
        }

        financialTable.setItems(financialData);
    }

    private void loadFinancialData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No financial operations found.");
            return;
        }

        // Parse the JSON file into a list of maps
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        financialData.setAll(data);
    }

    @FXML
    private void handleAddMoney() {
        String amountText = amountField.getText();
        String description = descriptionField.getText();

        if (!amountText.isEmpty() && !description.isEmpty()) {
            Map<String, Object> newOperation = Map.of(
                    "date", java.time.LocalDate.now().toString(),
                    "amount", amountText,
                    "type", "Dépôt",
                    "description", description
            );

            // Add to the TableView
            financialData.add(newOperation);

            // Update the JSON file
            saveFinancialData();

            // Clear fields
            amountField.clear();
            descriptionField.clear();
        }
    }

    @FXML
    private void handlePayCotisation() {
        Map<String, Object> cotisationOperation = Map.of(
                "date", java.time.LocalDate.now().toString(),
                "amount", "50.0",
                "type", "Cotisation",
                "description", "Cotisation annuelle"
        );

        financialData.add(cotisationOperation);
        cotisationStatusLabel.setText("Payée");
        cotisationStatusLabel.setStyle("-fx-text-fill: green;");

        // Update the JSON file
        saveFinancialData();
    }

    private void saveFinancialData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), financialData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save financial operations: " + e.getMessage());
        }
    }
}
