package org.example.Controllers.Association;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class MembresController {

    @FXML
    private Label soldeLabel;

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
    private static final String FILE_PATH = "Storage/treasury.json"; // Update with your actual JSON file path

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

        updateSoldeLabel();

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

    // Called when the "Ajouter" button is clicked
    @FXML
    private void onAddMoney() {
        try {
            // Get the amount entered by the user
            String amountText = amountField.getText();
            double amountToAdd = Double.parseDouble(amountText); // Make sure the input is numeric
            String description = descriptionField.getText();

            if (!amountText.isEmpty() && !description.isEmpty()) {
                File jsonFile = Paths.get("Storage/infos.json").toFile(); // Replace with the actual JSON file path

                // Load the current data from the JSON file
                Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);

                // Get the current "solde" from the file
                double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                        ? ((Number) accountData.get("solde")).doubleValue()
                        : 0.0;

                // Update the "solde"
                double newSolde = currentSolde + amountToAdd;
                accountData.put("solde", newSolde);

                // Write the updated data back to the JSON file
                objectMapper.writeValue(jsonFile, accountData);

                System.out.println("Successfully added money! New solde: " + newSolde);
                updateSoldeLabel();

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

            // Optionally, clear the input field
            amountField.clear();

        } catch (IOException e) {
            System.err.println("Error reading or writing JSON file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid amount entered: " + e.getMessage());
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

    private void updateSoldeLabel() {
        try {
            File jsonFile = Paths.get("Storage/infos.json").toFile(); // Replace with the actual JSON file path
            // Read the JSON file
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);

            // Get the current solde or default to 0
            double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                    ? ((Number) accountData.get("solde")).doubleValue()
                    : 0.0;

            // Set the Label text
            soldeLabel.setText("Total des Cotisations: " + currentSolde + " €");

        } catch (IOException e) {
            showErrorDialog("Error", "Unable to read solde: " + e.getMessage());
        }
    }

    @FXML
    public void onDonorsButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDesDonateurs.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion de Trésorerie");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onNotificationsButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationNotifications.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface des Notifications");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onActivitiesButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDesActivites.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Activités");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onTreasuryButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDeTresorerie.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Treasury Interface");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Vous êtes sur le point de quitter l'application.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.exit(0);
        }
    }

}
