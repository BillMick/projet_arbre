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
import org.example.Controllers.Node.AppChosenController;
import org.example.Models.Association;
import org.example.Models.Recette;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MembresController {
    private Map<String, Object> infos = AppChosenController.infosAssociation;
    public void setInfos(Map<String, Object> infos) {
        this.infos = infos;
        System.out.println(infos);
    }

    public static final String REPERTOIRE_DE_BASE = "Storage";
    public static final String REPERTOIRE_ASSOC = "Associations";
    public static final String REPERTOIRE_MEMBRES = "Members";
    public static final String REPERTOIRE_SERVICE = "Municipalite";
    private String REPERTOIRE_PROPRIETAIRE = (String) infos.get("email");
    private String REPERTOIRE_COURANT = "Courant";

    @FXML
    private Label soldeLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstnameField;

    @FXML
    private CheckBox presidentCheckBox;

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
    // private static final String FILE_PATH = "Storage/members.json"; // Update with your actual JSON file path

    @FXML
    public void initialize() {

        Path yearPath = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE);
        File directory = yearPath.toFile();
        if (directory.exists() && directory.isDirectory()) {
            File[] subdirectories = directory.listFiles(File::isDirectory);
            if (subdirectories == null || subdirectories.length == 0) {
            } else {
                System.out.println("Il existe des sous dossiers: " + subdirectories.length);
                Arrays.sort(subdirectories, Comparator.comparingLong(File::lastModified).reversed());
                REPERTOIRE_COURANT = subdirectories[0].getName();
            }
        } else {
            System.out.println("Le dossier spécifié n'existe pas.");
        }

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
        Map<String, Object> memberToDelete = membersTable.getItems().get(rowIndex);
        System.out.println(memberToDelete);
        membersTable.getItems().remove(rowIndex);
        // Remove from the data source (your JSON file)
        try {
            File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "members.json").toFile();
            List<Map<String, Object>> membersData = objectMapper.readValue(file, List.class);
            membersData.removeIf(m -> m.get("email").equals(memberToDelete.get("email")));

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, membersData);
            System.out.println("Member " + memberToDelete.get("name") + " deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadmembersData() throws IOException {
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "members.json").toFile();
        if (!file.exists()) {
            System.out.println("Aucun membre enregistré.");
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
        boolean isPresident = presidentCheckBox.isSelected();

        if (!name.isEmpty() && !email.isEmpty() && !firstname.isEmpty()) {
            Map<String, Object> newMember = new HashMap<>();
            newMember.put("nom", name);
            newMember.put("prenom", firstname);
            newMember.put("email", email);
            membersData.add(newMember);
            saveMembersData();
            if (isPresident) {
                savePresident(newMember);
            }
            nameField.clear();
            firstnameField.clear();
            emailField.clear();
            presidentCheckBox.setSelected(false);
            newMember.put("solde", 0.0);
            newMember.put("association", infos.get("email").toString());

            createMemberSpace(newMember);
        }
    }

    private void createMemberSpace(Map<String, Object> member) {
        try {
            File userDir = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_MEMBRES, (String) member.get("email")).toFile();
            if (userDir.exists()) {
                showErrorDialog("Erreur", "Un compte avec ce mail existe déjà.");
                return;
            }
            userDir.mkdirs();
            File userInfoFile = Paths.get(userDir.toString(), "infos.json").toFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(userInfoFile, member);
            System.out.println("Espace membre créé avec succès!");
        } catch (IOException e) {
            System.err.println("Error reading or writing JSON file for member space: " + e.getMessage());
        }
    }

    @FXML
    private void savePresident(Map<String, Object> member) {
        try {
            File jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "infos.json").toFile();
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);
            if (!jsonFile.exists()) {
                return;
            }
            accountData.put("isPresident", member.get("email"));
            accountData.put("presidentName", member.get("nom") + " " + member.get("prenom"));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, accountData);
            System.out.println("Président mis à jour avec succès!");
        } catch (IOException e) {
            System.err.println("Error reading or writing JSON file for setting president: " + e.getMessage());
        }
    }

    private void saveMembersData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "members.json").toFile(), membersData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Echec de l'enregistrement: " + e.getMessage());
        }
    }

    private void updateSoldeLabel() {
        if (REPERTOIRE_DE_BASE == null || REPERTOIRE_ASSOC == null || REPERTOIRE_PROPRIETAIRE == null || REPERTOIRE_COURANT == "Courant") {
            throw new IllegalArgumentException("Chemin inexistant.");
        }
        try {
            File jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "infos.json").toFile();
            Map<String, Object> accountData = objectMapper.readValue(jsonFile, Map.class);
            double currentSolde = accountData.getOrDefault("solde", 0.0) instanceof Number
                    ? ((Number) accountData.get("solde")).doubleValue()
                    : 0.0;

            jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "cotisations.json").toFile();
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
