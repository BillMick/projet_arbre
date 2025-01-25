package org.example.Controllers.Association;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.Models.Association;
import org.example.Models.Recette;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DonateursController {

    @FXML
    private Label soldeLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button addMoneyButton;

    @FXML
    private Button payCotisationButton;

    @FXML
    private Label cotisationStatusLabel;

    @FXML
    private TableView<Map<String, Object>> donorsTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> emailColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> typeColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> actionColumn;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button openDonorsInterfaceButton;

    private ObservableList<Map<String, Object>> donorsData = FXCollections.observableArrayList();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "Storage/donors.json"; // Update with your actual JSON file path

    @FXML
    public void initialize() {
        // Set up the TableView columns
        nameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("nom");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        emailColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("email");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        comboBox.getItems().addAll("MUNICIPALITE", "ENTREPRISE", "ASSOCIATION", "INDIVIDU", "AUTRE");

        typeColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("type");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        // Add the delete button to each row
        actionColumn.setCellFactory(param -> {
            TableCell<Map<String, Object>, String> cell = new TableCell<Map<String, Object>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Button deleteButton = new Button("Supprimer");
                        deleteButton.setTextFill(Color.web("#f44336"));
                        deleteButton.setOnAction(event -> deleteDonor(getTableRow().getIndex()));
                        HBox hBox = new HBox(deleteButton);
                        setGraphic(hBox);
                    }
                }
            };
            return cell;
        });

        // Populate the TableView
        try {
            loadDonorsData();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Echec du chargement des donateurs: " + e.getMessage());
        }

        updateSoldeLabel();
        donorsTable.setItems(donorsData);
    }

    private void deleteDonor(int rowIndex) {
        Map<String, Object> donorToDelete = donorsTable.getItems().get(rowIndex);
        System.out.println(donorToDelete);
        donorsTable.getItems().remove(rowIndex);

        // Remove from the data source (your JSON file)
        try {
            File file = new File(FILE_PATH);
            List<Map<String, Object>> donorsData = objectMapper.readValue(file, List.class);

            // Find and remove the donor from the data
            donorsData.removeIf(donor -> donor.get("email").equals(donorToDelete.get("email")));

            // Write the updated list back to the JSON file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, donorsData);
            System.out.println("Donor " + donorToDelete.get("name") + " deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDonorsData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Aucun donateur enregistré.");
            return;
        }
        // Parse the JSON file into a list of maps
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        donorsData.setAll(data);
    }

    @FXML
    private void handleAddDonor() {
        String name = nameField.getText();
        String email = emailField.getText();
        String type = comboBox.getValue();

        if (!name.isEmpty() && !email.isEmpty() && !type.isEmpty()) {
            Map<String, Object> newDonor = Map.of(
                    "nom", name,
                    "email", email,
                    "type", type
//                    "description", description
            );

            // Add to the TableView
            donorsData.add(newDonor);

            // Update the JSON file
            saveDonorData();

            // Clear fields
            nameField.clear();
            emailField.clear();
        }
    }

    @FXML
    private void onAskButtonClick() throws IOException {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Faire une demande");

        VBox dialogVBox = new VBox();
        dialogVBox.setSpacing(10);

        TextField amountField = new TextField();
        amountField.setPromptText("Entrer le montant");

        ComboBox<String> donorComboBox = new ComboBox<>();
        donorComboBox.setPromptText("Choisir un donateur");

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Aucun donateur enregistré.");
            return ;
        }
        List<Map<String, Object>> donors = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        for (Map<String, Object> donor : donors) {
            donorComboBox.getItems().add((String) donor.get("nom"));
        }

        dialogVBox.getChildren().addAll(amountField, donorComboBox);

        dialog.getDialogPane().setContent(dialogVBox);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String selectedDonor = donorComboBox.getValue();
                double montant = Double.parseDouble(amountField.getText());
                onAddMoney(montant, selectedDonor);
                System.out.println("Donateur " + selectedDonor + " with " + montant);
                //processRequest(montant, selectedDonor);  // Handle the request logic
            }
            return null;
        });

        dialog.showAndWait();
    }


    @FXML
    private void onAddMoney(double montant, String donor) {
        try {
            File jsonFile = Paths.get("Storage/infos.json").toFile(); // Replace with the actual JSON file path
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);
            double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                    ? ((Number) accountData.get("solde")).doubleValue()
                    : 0.0;
            double newSolde = currentSolde + montant;
            accountData.put("solde", newSolde);
            objectMapper.writeValue(jsonFile, accountData);
            System.out.println("Solde mis à jour avec succès! Nouveau solde: " + newSolde);

            Recette r = new Recette(montant, Recette.TypeRecette.DON, donor);
            r.modifierStatut(Recette.StatutRecette.PERCUE);

            Map<String, Object> newRecipe = Map.of(
                    "date", Association.dateFormat.format(r.date()),
                    "montant", r.montant(),
                    "type", r.typeRecette(),
                    "debiteur", r.debiteur(),
                    "statut", r.statutRecette()
            );
            // Normally, add to the association list ...
            saveRecipe(newRecipe);
            System.out.println("BLAA");
            updateSoldeLabel();

        } catch (IOException e) {
            System.err.println("Error reading or writing JSON file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid amount entered: " + e.getMessage());
        }
    }

    private void saveRecipe(Map<String, Object> recipe) throws IOException {
        File file = Paths.get("Storage/dons.json").toFile();
        if (!file.exists()) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, List.of(recipe));
            return;
        }
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        data.add(recipe);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }

    @FXML
    private void handlePayCotisation() {
        Map<String, Object> cotisationOperation = Map.of(
                "date", java.time.LocalDate.now().toString(),
                "amount", "50.0",
                "type", "Cotisation",
                "description", "Cotisation annuelle"
        );

        donorsData.add(cotisationOperation);
        cotisationStatusLabel.setText("Payée");
        cotisationStatusLabel.setStyle("-fx-text-fill: green;");

        // Update the JSON file
//        saveFinancialData();
    }

    private void saveDonorData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), donorsData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Echec de l'enregistrement: " + e.getMessage());
        }
    }

    private void updateSoldeLabel() {
        try {
            File jsonFile = Paths.get("Storage/infos.json").toFile(); // Replace with the actual JSON file path
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);
            double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                    ? ((Number) accountData.get("solde")).doubleValue()
                    : 0.0;

            jsonFile = Paths.get("Storage/dons.json").toFile();
            if (!jsonFile.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, List.of());
            }
            List<Map<String, Object>> recipeList = objectMapper.readValue(jsonFile, List.class);
            double totalDon = 0.0;
            for (Map<String, Object> don : recipeList) {
                if (don.get("montant") instanceof Number) {
                    totalDon += ((Number) don.get("montant")).doubleValue();
                }
            }
            soldeLabel.setText("Total des Dons: " + totalDon + " €");

        } catch (IOException e) {
            showErrorDialog("Error", "Unable to read solde: " + e.getMessage());
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
    public void onDonorsButtonClick() {
        try {
            // Stage currentStage = (Stage) openDonorsInterfaceButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDesDonateurs.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Donateurs");
            // currentStage.close();
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
