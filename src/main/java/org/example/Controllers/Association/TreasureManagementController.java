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
import java.util.*;
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
//            Object value = cellData.getValue().get("montant");
//            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
            Object montant = cellData.getValue().get("montant");
            Object type = cellData.getValue().get("type");
            if (montant != null && ("DEFRAIEMENT".equals(type) || "FACTURE".equals(type))) {
                try {
                    double m = Double.parseDouble(montant.toString());
                    return new ReadOnlyObjectWrapper<>(String.valueOf(m * -1));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return montant == null ? null : new ReadOnlyObjectWrapper<>(montant.toString());
        });

        typeColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("type");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

//        debtorColumn.setCellValueFactory(cellData -> {
//            Object value = cellData.getValue().get("debiteur");
//            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
//        });
        debtorColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("debiteur");
            if (value == null) {
                value = cellData.getValue().get("crediteur");
            }
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
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        file = Paths.get("Storage/dons.json").toFile();
        List<Map<String, Object>> dons = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        file = Paths.get("Storage/debts.json").toFile();
        List<Map<String, Object>> dettes = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        List<Map<String, Object>> dettesPayees = dettes.stream()
                .filter(debt -> "PAYEE".equals(debt.get("statut")))  // Filter condition
                .collect(Collectors.toList());

        List<Map<String, Object>> allFinancialData = new ArrayList<>();
        allFinancialData.addAll(data);
        allFinancialData.addAll(dons);
        allFinancialData.addAll(dettesPayees);
        Set<Map<String, Object>> uniqueFinancialData = new HashSet<>(allFinancialData);

        financialData.setAll(new ArrayList<>(uniqueFinancialData));

        System.out.println("Merged financial data successfully.");
        // financialData.setAll(data);
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
        TableColumn<Map<String, Object>, String> crediteurColumn = new TableColumn<>("Créditeur");

        // Setting up the columns
        nameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("type");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });
        crediteurColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("crediteur");
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
        debtTable.getColumns().add(crediteurColumn);
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
        Dette.TypeDette type = Dette.TypeDette.DEFRAIEMENT;
        if (debtToDelete.get("type") == "FACTURE") {
            type = Dette.TypeDette.FACTURE;
        }
        Dette d = new Dette(((Number) debtToDelete.get("montant")).doubleValue(), type, Dette.StatutDette.PAYEE, (String) debtToDelete.get("crediteur"));
        System.out.println("TEEEEEEEEEEEEEST: ");
        System.out.println(debtToDelete);
        try {
            jsonFile = Paths.get("Storage/debts.json").toFile();
            List<Map<String, Object>> data = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});

            // Find and update the debt status from the data
            data.removeIf(dt -> {
                boolean a1 = dt.get("crediteur").equals(debtToDelete.get("crediteur"));
                boolean a2 = dt.get("type").equals(debtToDelete.get("type"));
                boolean a3 = dt.get("statut").equals(debtToDelete.get("statut"));
                boolean a4 = dt.get("montant").equals(debtToDelete.get("montant"));
                boolean a = a1 && a2 && a3 && a4;
                System.out.println("BLEUUEEEEEEE");
                System.out.println(a1);
                System.out.println(a2);
                System.out.println(a3);
                System.out.println(a4);
                System.out.println(a);
                return a;
            });
            Map<String, Object> updatesDebt = Map.of(
                    "statut", d.statut(),
                    "montant", d.montant(),
                    "type", d.type(),
                    "crediteur", d.crediteur()
            );
            data.add(updatesDebt);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, data);
            updateDebtsLabel(); updateSoldeLabel();
            System.out.println("Debt " + debtToDelete.get("name") + " deleted");
            // Il faudra créditer le compte du bénéficiaire....
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Paying debt: " + debtToDelete.get("type") + " amount: " + debtToDelete.get("montant"));
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
                if (dette.get("montant") instanceof Number && dette.get("statut").equals("IMPAYEE")) {
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
