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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.Models.Association;
import org.example.Models.Dette;
import org.example.Models.Recette;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreasureManagementController {

    @FXML
    private Label soldeLabel;

    @FXML
    private Button seeDebtsButton;

    @FXML
    private Label debtsLabel;

    @FXML
    private TableView<Map<String, Object>> financialTable;

    @FXML
    private TableColumn<Map<String, Object>, String> dateColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> amountColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> typeColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> debtorColumn;

    private ObservableList<Map<String, Object>> financialData = FXCollections.observableArrayList();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "Storage/cotisations.json"; // il y a deux fichiers à parcourir ici: cotisations, dons...

    @FXML
    public void initialize() {
        // Set up the TableView columns
        dateColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("date");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        amountColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("montant");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        typeColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("type");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        debtorColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("debiteur");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        // Populate the TableView
        try {
            loadFinancialData();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load financial data: " + e.getMessage());
        }

        updateSoldeLabel();
        updateDebtsLabel();
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

    private void saveFinancialData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), financialData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save financial operations: " + e.getMessage());
        }
    }

    @FXML
    private void openDebtsDialog() {
        // Load the debts from the JSON file
        List<Map<String, Object>> allDebts = loadDebtsFromJson();
        List<Map<String, Object>> debts = allDebts.stream()
                .filter(debt -> !"PAYEE".equals(debt.get("statut")))  // Filter condition
                .collect(Collectors.toList());

        // Create a new Stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Détails des Impayés (dettes)");

        // Set up a TableView for displaying debts
        TableView<Map<String, Object>> debtTable = new TableView<>();
        TableColumn<Map<String, Object>, String> nameColumn = new TableColumn<>("Type");
        TableColumn<Map<String, Object>, Double> amountColumn = new TableColumn<>("Montant");

        // Setting up the columns
        nameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("type");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });
        amountColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("montant");
            if (value instanceof Double) {
                return new ReadOnlyObjectWrapper<>((Double) value);
            } else {
                return null;
            }
        });

        TableColumn<Map<String, Object>, Void> payColumn = new TableColumn<>("Payer");
        payColumn.setCellFactory(col -> {
            TableCell<Map<String, Object>, Void> cell = new TableCell<Map<String, Object>, Void>() {
                private final Button payButton = new Button("Payer");
                {
                    // payButton.setOnAction(event -> payDebt(debt, getTableRow().getIndex()));
                    payButton.setOnAction(event -> {
                        Map<String, Object> debt = getTableView().getItems().get(getIndex());
                        try {
                            payDebt(debtTable, getTableRow().getIndex());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(payButton);
                    }
                }
            };
            return cell;
        });

        debtTable.getColumns().add(nameColumn);
        debtTable.getColumns().add(amountColumn);
        debtTable.getColumns().add(payColumn);

        // Add the data to the table
        debtTable.getItems().setAll(debts);

        // Layout for the dialog
        VBox vbox = new VBox(debtTable);
        Scene dialogScene = new Scene(vbox);

        // Set up the stage (dialog window)
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private List<Map<String, Object>> loadDebtsFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("Storage/debts.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void payDebt(TableView<Map<String, Object>> debtTable, int rowIndex) throws IOException {
        // Logic for handling the payment
        File jsonFile = Paths.get("Storage/infos.json").toFile(); // Replace with the actual JSON file path
        Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);
        double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                ? ((Number) accountData.get("solde")).doubleValue()
                : 0.0;
        Map<String, Object> debtToDelete = debtTable.getItems().get(rowIndex);
        System.out.println(debtToDelete);
        debtTable.getItems().remove(rowIndex);
        double newSolde = currentSolde - ((Number) debtToDelete.get("montant")).doubleValue();
        accountData.put("solde", newSolde);
        objectMapper.writeValue(jsonFile, accountData);
        System.out.println("Solde mis à jour avec succès! Nouveau solde: " + newSolde);
        // Mise à jour du statut et réinsertion

        Dette d = new Dette(((Number) debtToDelete.get("montant")).doubleValue(), (Dette.TypeDette) debtToDelete.get("type"), Dette.StatutDette.PAYEE, (String) debtToDelete.get("crediteur"));
        Map<String, Object> newRecipe = Map.of(
                "statut", d.statut(),
                "montant", d.montant(),
                "type", d.type(),
                "crediteur", d.crediteur()
        );



        try {
            jsonFile = Paths.get("Storage/debts.json").toFile();
            List<Map<String, Object>> data = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});

            // Find and remove the donor from the data
            data.removeIf(dt -> dt.get("crediteur").equals(debtToDelete.get("crediteur")));

            // Write the updated list back to the JSON file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, data);
            System.out.println("Debt " + debtToDelete.get("name") + " deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Paying debt: " + debtToDelete.get("type") + " amount: " + debtToDelete.get("montant"));
        // You can add additional logic here to update your list, remove the debt, etc.
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
            soldeLabel.setText("Solde actuel: " + currentSolde + " €");
            System.out.println("Solde actuel: " + currentSolde);

        } catch (IOException e) {
            showErrorDialog("Error", "Unable to read solde: " + e.getMessage());
        }
    }

    private void updateDebtsLabel() {
        try {
            File jsonFile = Paths.get("Storage/infos.json").toFile(); // Replace with the actual JSON file path
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);
            double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                    ? ((Number) accountData.get("solde")).doubleValue()
                    : 0.0;

            jsonFile = Paths.get("Storage/debts.json").toFile();
            if (!jsonFile.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, List.of());
            }
            List<Map<String, Object>> debtsList = objectMapper.readValue(jsonFile, List.class);
            double totalDette = 0.0;
            for (Map<String, Object> dette : debtsList) {
                if (dette.get("montant") instanceof Number) {
                    totalDette += ((Number) dette.get("montant")).doubleValue();
                }
            }
            debtsLabel.setText("Total des Impayés: " + totalDette + " €");
            System.out.println("Total des Impayés: " + totalDette);

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
    public void onMembersButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDesMembres.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Membres");

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
