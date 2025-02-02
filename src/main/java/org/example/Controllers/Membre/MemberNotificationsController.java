package org.example.Controllers.Membre;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.Controllers.Node.AppChosenController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class MemberNotificationsController {

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
    private TableView<Map<String, Object>> notificationsTableView;

    @FXML
    private TableColumn<Map<String, Object>, String> dateColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> timeColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> senderColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;

    private ObservableList<Map<String, Object>> notificationsData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // private static final String NOTIFICATIONS_FILE_PATH = "Storage/notifications.json";

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
        // Configuration des colonnes de la TableView
        dateColumn.setCellValueFactory(cellData -> {
            Long timestampLong = (Long) cellData.getValue().get("timestamps");
            if (timestampLong != null) {
                Date timestamp = new Date(timestampLong);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                return new ReadOnlyObjectWrapper<>(dateFormat.format(timestamp));
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        timeColumn.setCellValueFactory(cellData -> {
            Long timestampLong = (Long) cellData.getValue().get("timestamps");
            if (timestampLong != null) {
                Date timestamp = new Date(timestampLong);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                return new ReadOnlyObjectWrapper<>(dateFormat.format(timestamp));
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        senderColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("sender");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });
        statusColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("status");

            if (value != null && value instanceof Boolean) {
                boolean status = (Boolean) value;
                return new ReadOnlyObjectWrapper<>(status ? "Lu" : "Non lu");
            }
            return new ReadOnlyObjectWrapper<>("Non lu");
        });

        // Charger les notifications
        try {
            loadNotifications();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load notifications: " + e.getMessage());
        }

        notificationsTableView.setItems(notificationsData);
        notificationsTableView.setOnMouseClicked(this::handleNotificationClick);
    }
    @FXML
    private void onBackButtonClick() {
        // Ferme la fenêtre actuelle ou naviguer vers une autre vue
        Stage stage = (Stage) notificationsTableView.getScene().getWindow();
        stage.close(); // Fermer la fenêtre
    }

    private void loadNotifications() throws IOException {
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, infos.get("association").toString(), "notifications.json").toFile();
        if (!file.exists()) {
            System.out.println("No notifications found.");
            return;
        }

        // Lire le fichier JSON dans une liste de cartes
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        notificationsData.setAll(data);
    }

    private void handleNotificationClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Vérifie si l'utilisateur double-clique
            Map<String, Object> selectedNotification = notificationsTableView.getSelectionModel().getSelectedItem();
            if (selectedNotification != null) {
                String message = (String) selectedNotification.get("message");
                String date = (String) selectedNotification.get("date");
                String time = (String) selectedNotification.get("time");
                Boolean status = (boolean) selectedNotification.get("status");

                // Afficher le contenu de la notification dans une alerte
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText(message);
                alert.setContentText("Date: " + date + "\nHeure: " + time );
                alert.showAndWait();

                // Mettre à jour le statut
                if (!status) {
                    selectedNotification.put("status", true);
                    notificationsTableView.refresh();
                    saveNotifications();
                }
            }
        }
    }

    private void saveNotifications() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, infos.get("association").toString(), "notifications.json").toFile(), notificationsData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save notifications: " + e.getMessage());
        }
    }
}