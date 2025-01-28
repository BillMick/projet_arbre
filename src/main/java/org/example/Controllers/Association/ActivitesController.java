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
import org.example.Models.Activite;
import org.example.Models.Association;
import org.example.Models.Recette;
import org.example.Models.Visite;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ActivitesController {
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
    private Label nbNotificationsLabel;

    @FXML
    private Label nbActivitesLabel;

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
    private TableView<Map<String, Object>> activitiesTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> typeColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> dateColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> costColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> executorColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> actionColumn;

    @FXML
    private Button openDonorsInterfaceButton;

    private ObservableList<Map<String, Object>> activitiesData = FXCollections.observableArrayList();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    // private static final String FILE_PATH = "Storage/activites.json"; // Update with your actual JSON file path

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
            Object value = cellData.getValue().get("nomArbre");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        typeColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("type");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        statusColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("statut");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        executorColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("executeur");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        costColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("cout");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        dateColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("dateDePlanification");
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
                        Button deleteButton = new Button("Révoquer");
                        deleteButton.setTextFill(Color.web("#f44336"));
                        deleteButton.setOnAction(event -> deleteActivity(getTableRow().getIndex()));
                        HBox hBox = new HBox(deleteButton);
                        setGraphic(hBox);
                    }
                }
            };
            return cell;
        });

        // Populate the TableView
        try {
            loadActivitiesData();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Echec du chargement des donateurs: " + e.getMessage());
        }

        updateSoldeLabel();
        updateNbLabels();
        activitiesTable.setItems(activitiesData);
    }

    private void deleteActivity(int rowIndex) {
        Map<String, Object> activityToDelete = activitiesTable.getItems().get(rowIndex);
        System.out.println(activityToDelete);
        activitiesTable.getItems().remove(rowIndex);
        try {
            if (REPERTOIRE_DE_BASE == null || REPERTOIRE_ASSOC == null || REPERTOIRE_PROPRIETAIRE == null || REPERTOIRE_COURANT == "Courant") {
                throw new IllegalArgumentException("Chemin inexistant.");
            }
            File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "activites.json").toFile();
            List<Map<String, Object>> activitiesData = objectMapper.readValue(file, List.class);

            // Find and remove the donor from the data
            activitiesData.removeIf(ac -> {
                boolean a1 = ac.get("dateDePlanification").equals(activityToDelete.get("dateDePlanification"));
                boolean a2 = ac.get("cout").equals(activityToDelete.get("cout"));
                boolean a3 = ac.get("arbre").equals(activityToDelete.get("arbre"));
                boolean a4 = ac.get("type").equals(activityToDelete.get("type"));
                boolean a5 = ac.get("statut").equals("ATTENTE");
                boolean a = a1 && a2 && a3 && a4 && a5;
                System.out.println("ROOOOOOOOOSE");
                System.out.println(a1);
                System.out.println(a2);
                System.out.println(a3);
                System.out.println(a4);
                System.out.println(a5);
                System.out.println(a);
                return a;
            });

            // Write the updated list back to the JSON file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, activitiesData);
            System.out.println("Visite " + activityToDelete.get("arbre") + " deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadActivitiesData() throws IOException {
        if (REPERTOIRE_DE_BASE == null || REPERTOIRE_ASSOC == null || REPERTOIRE_PROPRIETAIRE == null || REPERTOIRE_COURANT == "Courant") {
            throw new IllegalArgumentException("Chemin inexistant.");
        }
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "activites.json").toFile();
        if (!file.exists()) {
            System.out.println("Aucune activité enregistrée.");
            return;
        }
        // Parse the JSON file into a list of maps
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        activitiesData.setAll(data);
    }

    @FXML
    private void onAddActivityButtonClick() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une activité");

        VBox dialogVBox = new VBox();
        dialogVBox.setSpacing(10);

        // TextField for amount (cost)
        TextField amountField = new TextField();
        amountField.setPromptText("Entrer le coût de l'activité");

        // ComboBox for selecting the activity type
        ComboBox<String> activityTypeComboBox = new ComboBox<>();
        activityTypeComboBox.setPromptText("Choisir le type d'activité");

        // Add options for Activite.TypeActivite
        activityTypeComboBox.getItems().addAll(String.valueOf(Activite.TypeActivite.VISITE));

        // ComboBox for selecting an arbre (tree)
        ComboBox<String> activityComboBox = new ComboBox<>();
        activityComboBox.setPromptText("Choisir un arbre");
        // Load trees from the file
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "trees.json").toFile();
        if (!file.exists()) {
            System.out.println("Aucun arbre enregistré.");
            return;
        }
        List<Map<String, Object>> trees = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        for (Map<String, Object> tree : trees) {
            activityComboBox.getItems().add((String) tree.get("name"));
        }

        // DatePicker for selecting the date of planification
        DatePicker dateDePlanificationPicker = new DatePicker();
        dateDePlanificationPicker.setPromptText("Choisir la date de planification");

        // Add all fields to the dialog
        dialogVBox.getChildren().addAll(activityTypeComboBox, activityComboBox, dateDePlanificationPicker, amountField);

        dialog.getDialogPane().setContent(dialogVBox);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                // Gather the data entered by the user
                String selectedActivityType = activityTypeComboBox.getValue();
                String selectedTree = activityComboBox.getValue();
                String costText = amountField.getText();
                String dateDePlanification = dateDePlanificationPicker.getValue() != null ? dateDePlanificationPicker.getValue().toString() : "";

                // You can now handle the collected data (e.g., create a new Activity object, save to file, etc.)
                System.out.println("Type d'Activité: " + selectedActivityType);
                System.out.println("Arbre: " + selectedTree);
                System.out.println("Montant: " + costText);
                System.out.println("Date de planification: " + dateDePlanification);
                try {
                    double cost = Double.parseDouble(costText);
                    if (!selectedActivityType.isEmpty() && !selectedTree.isEmpty() && !costText.isEmpty()) {
                        Map<String, Object> newActivity = Map.of(
                                "dateDePlanification", dateDePlanification,
                                "cout", cost,
                                "statut", Activite.StatutActivte.ATTENTE,
                                "nomArbre", selectedTree,
                                "localisationArbre", selectedTree,
                                "type", selectedActivityType,
                                "rapport", "",
                                "executeur", ""
                        );

                        // Add to the TableView
                        activitiesData.add(newActivity);
                        // Update the JSON file
                        saveActivitiesData();

                        // Clear fields
                        activityTypeComboBox.cancelEdit();
                        activityComboBox.cancelEdit();
                        amountField.clear();
                    }
                    // Continue with your processing logic
                } catch (NumberFormatException e) {
                    System.out.println("Invalid cost input");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void saveActivitiesData() {
        if (REPERTOIRE_DE_BASE == null || REPERTOIRE_ASSOC == null || REPERTOIRE_PROPRIETAIRE == null || REPERTOIRE_COURANT == "Courant") {
            throw new IllegalArgumentException("Chemin inexistant.");
        }
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "activites.json").toFile(), activitiesData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Échec de l'enregistrement: " + e.getMessage());
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

            jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "dons.json").toFile();
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

    private void updateNbLabels() {
        try {
            if (REPERTOIRE_DE_BASE == null || REPERTOIRE_ASSOC == null || REPERTOIRE_PROPRIETAIRE == null || REPERTOIRE_COURANT == "Courant") {
                throw new IllegalArgumentException("Chemin inexistant.");
            }
            File jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "notifications.json").toFile();
            if (!jsonFile.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, List.of());
            }
            List<Map<String, Object>> notificationsData = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            nbNotificationsLabel.setText("Notification·s: " + notificationsData.size());

            File jsonFile1 = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "activites.json").toFile();
            if (!jsonFile1.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile1, List.of());
            }
            List<Map<String, Object>> activitiesData = objectMapper.readValue(jsonFile1, new TypeReference<List<Map<String, Object>>>() {});
            nbActivitesLabel.setText("Activité·s: " + activitiesData.size());

        } catch (IOException e) {
            showErrorDialog("Erreur", "Problème de lecture de données: " + e.getMessage());
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
        Stage stage = (Stage) activitiesTable.getScene().getWindow();
        stage.close();
    }
}
