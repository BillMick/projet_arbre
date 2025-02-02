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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.Controllers.Node.AppChosenController;
import org.example.Models.Association;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class NotificationsController {
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
    private Label nbNotificationsLabel;

    @FXML
    private Label nbActivitesLabel;

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

    @FXML
    private Button subscribeButton;

    private ObservableList<Map<String, Object>> notificationsData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // private static final String NOTIFICATIONS_FILE_PATH = "Storage/notifications.json"; // Chemin vers le fichier JSON

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
            // If the value is not a Boolean or null, return a default value
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
        updateNbLabels();
        subscription();
    }

    private void subscription() {
        if ((Boolean) this.infos.get("souscription")){
            subscribeButton.setText("Se désinscrire");
            subscribeButton.setStyle("-fx-background-color: #ea053e; -fx-text-fill: white;");
            return;
        }
        subscribeButton.setText("Souscrire aux notifications");
        subscribeButton.setStyle("-fx-background-color: #0a980a; -fx-text-fill: white;");
    }

    @FXML
    private void toggleSubscription() throws IOException {
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "infos.json").toFile();
        if (!file.exists()) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, Map.class);
        }
        Map<String, Object> data = objectMapper.readValue(file, Map.class);
        if ((Boolean) this.infos.get("souscription")) {
            data.put("souscription", false);
            infos = data;
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            subscribeButton.setText("Souscrire aux notifications");
            subscribeButton.setStyle("-fx-background-color: #0a980a; -fx-text-fill: white;");
            // mentionner l'info dans le fichier de la municipalité
            file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "abonnes.json").toFile();
            if (!file.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, List.of());
            }
            List<Map<String, Object>> abonnes = objectMapper.readValue(file, List.class);
            abonnes.removeIf(ab -> {
                boolean a1 = ab.get("nom").equals(this.infos.get("nom"));
                boolean a2 = ab.get("email").equals(this.infos.get("email"));
                boolean a = a1 && a2;
                System.out.println("Notiiiiiiiiification");
                System.out.println(a1);
                System.out.println(a2);
                System.out.println(a);
                return a;
            });
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, abonnes);
        } else {
            data.put("souscription", true);
            infos = data;
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            subscribeButton.setText("Se désinscrire");
            subscribeButton.setStyle("-fx-background-color: #ea053e; -fx-text-fill: white;");
            // ajouter l'info dans le fichier de la municipalité
            file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "abonnes.json").toFile();
            if (!file.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, List.of());
            }
            Map<String, Object> subscriptionInfos = new HashMap<>();
            subscriptionInfos.put("nom", this.infos.get("nom"));
            subscriptionInfos.put("email", this.infos.get("email"));
            List<Map<String, Object>> abonnes = objectMapper.readValue(file, List.class);
            abonnes.add(subscriptionInfos);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, abonnes);
        }
    }

    @FXML
    private void onBackButtonClick() {
        // Ferme la fenêtre actuelle ou naviguer vers une autre vue
        Stage stage = (Stage) notificationsTableView.getScene().getWindow();
        stage.close(); // Fermer la fenêtre
    }

    private void loadNotifications() throws IOException {
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "notifications.json").toFile();
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                String message = (String) selectedNotification.get("message");
                String date = dateFormat.format(new Date((Long) selectedNotification.get("timestamps")));
                String time = hourFormat.format(new Date((Long) selectedNotification.get("timestamps")));
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
                    updateNbLabels();
                }
            }
        }
    }

    private void saveNotifications() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "notifications.json").toFile(), notificationsData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save notifications: " + e.getMessage());
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
            int notificationNl = (int) notificationsData.stream()
                    .filter(notification -> notification.get("status").equals(false))
                    .count();
            nbNotificationsLabel.setText("Notification·s non lue·s: " + notificationNl);

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
