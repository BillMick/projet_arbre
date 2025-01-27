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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class MemberNotificationsController {

    @FXML
    private TableView<Map<String, Object>> notificationsTableView;

    @FXML
    private TableColumn<Map<String, Object>, String> dateColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> timeColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;

    private ObservableList<Map<String, Object>> notificationsData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String NOTIFICATIONS_FILE_PATH = "Storage/notifications.json"; // Chemin vers le fichier JSON

    @FXML
    public void initialize() {
        // Configuration des colonnes de la TableView
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().get("date")).asString());
        timeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().get("time")).asString());
        statusColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().get("status")).asString());

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
        File file = new File(NOTIFICATIONS_FILE_PATH);
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
                String status = (String) selectedNotification.get("status");

                // Afficher le contenu de la notification dans une alerte
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText(message);
                alert.setContentText("Date: " + date + "\nHeure: " + time );
                alert.showAndWait();

                // Mettre à jour le statut
                selectedNotification.put("status", "Lu");
                notificationsTableView.refresh();
                saveNotifications(); // Enregistrer les modifications
            }
        }
    }

    private void saveNotifications() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(NOTIFICATIONS_FILE_PATH), notificationsData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save notifications: " + e.getMessage());
        }
    }
}