package org.example.Controllers.Membre;

import javafx.stage.Stage;
import org.example.Models.Membre;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TreasureManagementController {

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

    private Membre membre; // Instance de Membre
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "Storage/treasury.json"; // Chemin vers le fichier JSON
    private static final String MEMBER_FILE = "Storage/infos.json"; // Chemin vers le fichier membre

    @FXML
    public void initialize() {
        this.membre = new Membre("Nom", "Prénom", "email@example.com");
        this.membre.ajoutersolde(100.0); // Initialiser le solde pour les tests

        // Configuration des colonnes de la TableView
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

        // Charger les données financières
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

        // Analyser le fichier JSON dans une liste de cartes
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

            // Ajouter à la TableView
            financialData.add(newOperation);

            // Mettre à jour le fichier JSON
            saveFinancialData();

            // Effacer les champs
            amountField.clear();
            descriptionField.clear();
        }
    }

    @FXML
    private void onAddMoney() {
        try {
            String amountText = amountField.getText();
            double amountToAdd = Double.parseDouble(amountText); // Assurez-vous que l'entrée est numérique
            String description = descriptionField.getText();

            if (!amountText.isEmpty() && !description.isEmpty()) {
                File jsonFile = Paths.get(MEMBER_FILE).toFile(); // Chemin vers le fichier JSON

                // Charger les données actuelles du fichier JSON
                Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);

                // Obtenir le "solde" actuel du fichier
                double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                        ? ((Number) accountData.get("solde")).doubleValue()
                        : 0.0;

                // Mettre à jour le "solde"
                double newSolde = currentSolde + amountToAdd;
                accountData.put("solde", newSolde);

                // Écrire les données mises à jour dans le fichier JSON
                objectMapper.writeValue(jsonFile, accountData);

                System.out.println("Successfully added money! New solde: " + newSolde);
                updateSoldeLabel();

                Map<String, Object> newOperation = Map.of(
                        "date", java.time.LocalDate.now().toString(),
                        "amount", amountText,
                        "type", "Dépôt",
                        "description", description
                );

                // Ajouter à la TableView
                financialData.add(newOperation);

                // Mettre à jour le fichier JSON
                saveFinancialData();

                // Effacer les champs
                amountField.clear();
                descriptionField.clear();
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing JSON file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid amount entered: " + e.getMessage());
        }
    }

    @FXML
    private void onPayCotisation() {
        double montantCotisation = 50.0; // Montant de la cotisation

        try {
            // Charger les données actuelles du fichier JSON
            File jsonFile = Paths.get(MEMBER_FILE).toFile();
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);

            // Obtenir le "solde" actuel
            double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                    ? ((Number) accountData.get("solde")).doubleValue()
                    : 0.0;

            // Vérifier si les fonds sont suffisants pour payer la cotisation
            if (currentSolde >= montantCotisation) {
                // Mettre à jour le solde
                double newSolde = currentSolde - montantCotisation;
                accountData.put("solde", newSolde);
                objectMapper.writeValue(jsonFile, accountData); // Écrire les données mises à jour dans le fichier JSON

                // La cotisation a été payée avec succès
                Map<String, Object> cotisationOperation = Map.of(
                        "date", java.time.LocalDate.now().toString(),
                        "amount", String.valueOf(montantCotisation),
                        "type", "Cotisation",
                        "description", "Cotisation annuelle"
                );

                // Ajouter à la TableView
                financialData.add(cotisationOperation);
                saveFinancialData(); // Sauvegarder les opérations financières
                updateSoldeLabel(); // Mettre à jour l'affichage du solde

                // Mettre à jour le statut de la cotisation
                cotisationStatusLabel.setText("Payée");
                cotisationStatusLabel.setStyle("-fx-text-fill: green;");

                // Changer le style du bouton et le désactiver
                payCotisationButton.setStyle("-fx-background-color: #A9A9A9; -fx-text-fill: white;"); // Change to gray
                payCotisationButton.setDisable(true);
            } else {
                showErrorDialog("Erreur", "Fonds insuffisants pour payer la cotisation.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Erreur", "Impossible de traiter le paiement de la cotisation.");
        }
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
            File jsonFile = Paths.get(MEMBER_FILE).toFile(); // Chemin vers le fichier JSON
            // Lire le fichier JSON
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);

            // Obtenir le solde actuel ou défaut à 0
            double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                    ? ((Number) accountData.get("solde")).doubleValue()
                    : 0.0;

            // Mettre à jour le texte du label
            soldeLabel.setText("Current Solde: " + currentSolde + " €");

        } catch (IOException e) {
            showErrorDialog("Error", "Unable to read solde: " + e.getMessage());
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

    @FXML
    private void onBackButtonClick() {
        // Ferme la fenêtre actuelle ou naviguer vers une autre vue
        Stage stage = (Stage) financialTable.getScene().getWindow();
        stage.close(); // Fermer la fenêtre
    }
}