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

public class MembresController {

    @FXML
    private Label soldeLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstnameField;

    @FXML
    private TableView<Map<String, Object>> membersTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> emailColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> firstnameColumn;

    @FXML
    private ComboBox<String> comboBox;

    private ObservableList<Map<String, Object>> membersData = FXCollections.observableArrayList();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "Storage/members.json"; // Update with your actual JSON file path

    @FXML
    public void initialize() {
        // Set up the TableView columns
        nameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("nom");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });
        
        firstnameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("prenom");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        emailColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("email");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        // Populate the TableView
        try {
            loadmembersData();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Echec du chargement des membres: " + e.getMessage());
        }

        updateSoldeLabel();
        membersTable.setItems(membersData);
    }

    private void deleteMember(int rowIndex) {
        Map<String, Object> donorToDelete = membersTable.getItems().get(rowIndex);
        System.out.println(donorToDelete);
        membersTable.getItems().remove(rowIndex);

        // Remove from the data source (your JSON file)
        try {
            File file = new File(FILE_PATH);
            List<Map<String, Object>> membersData = objectMapper.readValue(file, List.class);

            // Find and remove the donor from the data
            membersData.removeIf(donor -> donor.get("email").equals(donorToDelete.get("email")));

            // Write the updated list back to the JSON file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, membersData);
            System.out.println("Donor " + donorToDelete.get("name") + " deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadmembersData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Aucun donateur enregistré.");
            return;
        }
        // Parse the JSON file into a list of maps
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        membersData.setAll(data);
    }

    @FXML
    private void handleAddMember() {
        String name = nameField.getText();
        String email = emailField.getText();
        String firstname = firstnameField.getText();

        if (!name.isEmpty() && !email.isEmpty() && !firstname.isEmpty()) {
            Map<String, Object> newMember = Map.of(
                    "nom", name,
                    "prenom", firstname,
                    "email", email
            );

            // Add to the TableView
            membersData.add(newMember);

            // Update the JSON file
            saveDonorData();

            // Clear fields
            nameField.clear();
            firstnameField.clear();
            emailField.clear();
        }
    }

//    @FXML
//    private void onAskButtonClick() throws IOException {
//
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Faire une demande");
//
//        VBox dialogVBox = new VBox();
//        dialogVBox.setSpacing(10);
//
//        TextField amountField = new TextField();
//        amountField.setPromptText("Entrer le montant");
//
//        ComboBox<String> donorComboBox = new ComboBox<>();
//        donorComboBox.setPromptText("Choisir un donateur");
//
//        File file = new File(FILE_PATH);
//        if (!file.exists()) {
//            System.out.println("Aucun donateur enregistré.");
//            return ;
//        }
//        List<Map<String, Object>> donors = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
//        for (Map<String, Object> donor : donors) {
//            donorComboBox.getItems().add((String) donor.get("nom"));
//        }
//
//        dialogVBox.getChildren().addAll(amountField, donorComboBox);
//
//        dialog.getDialogPane().setContent(dialogVBox);
//
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == ButtonType.OK) {
//                String selectedDonor = donorComboBox.getValue();
//                double montant = Double.parseDouble(amountField.getText());
//                onAddMoney(montant, selectedDonor);
//                System.out.println("Donateur " + selectedDonor + " with " + montant);
//                //processRequest(montant, selectedDonor);  // Handle the request logic
//            }
//            return null;
//        });
//
//        dialog.showAndWait();
//    }


    @FXML
    private void onAddMoney(double montant, String member) {
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

            Recette r = new Recette(montant, Recette.TypeRecette.COTISATION, member);
            r.modifierStatut(Recette.StatutRecette.NONPERCUE);

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
        File file = Paths.get("Storage/cotisations.json").toFile();
        if (!file.exists()) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, List.of(recipe));
            return;
        }
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        data.add(recipe);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }

    @FXML
//    private void handlePayCotisation() {
//        Map<String, Object> cotisationOperation = Map.of(
//                "date", java.time.LocalDate.now().toString(),
//                "amount", "50.0",
//                "type", "Cotisation",
//                "description", "Cotisation annuelle"
//        );
//
//        membersData.add(cotisationOperation);
//
//        // Update the JSON file
////        saveFinancialData();
//    }

    private void saveDonorData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), membersData);
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

            jsonFile = Paths.get("Storage/cotisations.json").toFile();
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
            soldeLabel.setText("Total des Cotisations: " + totalDon + " €");

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
            stage.setTitle("Interface de Gestion de la Trésorerie");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onVotesButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationVotes.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Visualisation des Votes");

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

    @FXML
    private void onBackButtonClick() {
        Stage stage = (Stage) membersTable.getScene().getWindow();
        stage.close();
    }
}
