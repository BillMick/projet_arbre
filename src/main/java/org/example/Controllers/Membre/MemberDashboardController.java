package org.example.Controllers.Membre;

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
import org.example.Controllers.Node.AppChosenController;
import org.example.Models.Arbre;
import org.example.Models.Association;
import org.example.java_project.Application;

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MemberDashboardController {

    private Map<String, Object> infos = AppChosenController.infosMembre1;
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
    private Label nbNotificationsLabel;

    @FXML
    private Label nbActivitesLabel;

    @FXML
    private Label anneeLabel;

    @FXML
    private TableView<Map<String, Object>> treesTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> locationColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;

    // private ObservableList<Arbre> treesData = FXCollections.observableArrayList();
    private ObservableList<Map<String, Object>> treesData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // private static final String FILE_PATH = "Storage/trees.json";

    @FXML
    public void initialize() {
        Path yearPath = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, infos.get("association").toString());
        File directory = yearPath.toFile();
        if (directory.exists() && directory.isDirectory()) {
            File[] subdirectories = directory.listFiles(File::isDirectory);
            if (subdirectories == null || subdirectories.length == 0) {
                //
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

        // Set color based on the status
        statusColumn.setCellFactory(param -> {
            return new TableCell<Map<String, Object>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        switch (item) { //.toLowerCase()
                            case "REMARQUABLE":
                                setStyle("-fx-text-fill: #074994;"); // Blue
                                break;
                            case "ABATTU":
                                setStyle("-fx-text-fill: #8B0000;"); // Red
                                break;
                            case "en croissance":
                                setStyle("-fx-text-fill: #32CD32;"); // Green
                                break;
                            case "NON REMARQUABLE":
                            default:
                                setStyle("-fx-text-fill: black;"); // Default to black if no match
                                break;
                        }
                    }
                }
            };
        });

        // Load data into the TableView
        try {
            updateTableData();
        } catch (IOException e) {
            System.err.println("Error updating table data: " + e.getMessage());
        }

        updateNbLabels();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateTableData() throws IOException {
        treesTable.setItems(treesData);

        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "trees.json").toFile();
        if (!file.exists()) {
            System.out.println("No trees data found.");
            return;
        }

        // Parse the JSON file into a list of maps
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        treesData.setAll(data);
    }

//    public static Map<String, Object> readTrees() throws IOException {
//
//        return Map.of();
//    }
    @FXML
    public void onVotesButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("treeVoting.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Votes");

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
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("memberGestionDeTresorerie.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion de la trésorerie");

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
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("MemberActivities.fxml"));
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
    private void onNotificationsButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("MemberNotifications.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Notifications");

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNbLabels() {
        try {
            File jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, infos.get("association").toString(), "notifications.json").toFile();
            List<Map<String, Object>> notificationsData = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            int nb = notificationsData.size();
            nbNotificationsLabel.setText("Notification·s: " + nb);

            File jsonFile1 = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, infos.get("association").toString(), REPERTOIRE_COURANT, "activites.json").toFile();
            List<Map<String, Object>> activitiesData = objectMapper.readValue(jsonFile1, new TypeReference<List<Map<String, Object>>>() {});
            nb = activitiesData.size();
            nbActivitesLabel.setText("Activité·s: " + nb);

            File jsonFile2 = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, infos.get("association").toString(), "infos.json").toFile();
            Map<String, Object> begin = objectMapper.readValue(jsonFile2, new TypeReference<Map<String, Object>>() {});
            nb = activitiesData.size();
            anneeLabel.setText("Année d'exercice en cours: " + Association.dateFormat.format(begin.get("debut")));
        } catch (IOException e) {
            showErrorDialog("Error", "Unable to read data: " + e.getMessage());
        }
    }

    @FXML
    private void onQuitButtonClick() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Vous êtes sur le point de quitter l'association. Vos données seront supprimées et vous ne pourrez plus vous connecter.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            Path folder = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_MEMBRES, REPERTOIRE_PROPRIETAIRE);
            Files.walk(folder)
                    .sorted((path1, path2) -> path2.compareTo(path1))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            System.out.println("Données supprimées: " + path);
                        } catch (IOException e) {
                            System.err.println("Unable to delete: " + path);
                        }
                    });
            File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, infos.get("association").toString(), "members.json").toFile();
            List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
            data.removeIf(m -> m.get("email") != null && m.get("email").equals(infos.get("email").toString()));
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            System.out.println("Bye.");
            System.exit(0);
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
