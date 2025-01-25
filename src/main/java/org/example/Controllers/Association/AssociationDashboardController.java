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
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Models.Arbre;
import org.example.java_project.Application;

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class AssociationDashboardController {

    @FXML
    private Label nbNotificationsLabel;

    @FXML
    private Label nbActivitesLabel;

    @FXML
    private TableView<Map<String, Object>> treesTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> locationColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;

    private ObservableList<Map<String, Object>> treesData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "Storage/trees.json"; // Update with your actual JSON file path


    @FXML
    public void initialize() {
        // Bind columns to Arbre properties
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
//        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up the TableView columns
        nameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("name");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        locationColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("location");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        statusColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("status");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        // Load data into the TableView
        try {
            updateTableData();
        } catch (IOException e) {
            System.err.println("Error updating table data: " + e.getMessage());
        }

        updateNbLabels();
    }

    private void updateNbLabels() {
        try {
            File jsonFile = Paths.get("Storage/notifications.json").toFile(); // Replace with the actual JSON file path
            List<Map<String, Object>> notificationsData = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            int nb = notificationsData.size();
            nbNotificationsLabel.setText("Notification·s: " + nb);

            File jsonFile1 = Paths.get("Storage/activites.json").toFile(); // Replace with the actual JSON file path
            List<Map<String, Object>> activitiesData = objectMapper.readValue(jsonFile1, new TypeReference<List<Map<String, Object>>>() {});
            nb = activitiesData.size();
            nbActivitesLabel.setText("Activité·s: " + nb);
        } catch (IOException e) {
            showErrorDialog("Error", "Unable to read data: " + e.getMessage());
        }
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateTableData() throws IOException {
//        // Clear existing data
//        treesData.clear();
//
//        // Load new data from the `readTrees` function
//        Map<String, Object> treesMap = readTrees();
//
//        // Add all Arbre objects to the ObservableList
//        treesData.addAll(treesMap.values());

        // Set the TableView's items
        treesTable.setItems(treesData);

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No trees data found.");
            return;
        }

        // Parse the JSON file into a list of maps
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        treesData.setAll(data);
    }

    // Existing readTrees() method
    public static Map<String, Object> readTrees() throws IOException {
        // Your implementation of readTrees() function here
        return Map.of(); // Replace with actual logic
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
