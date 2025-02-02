package org.example.Controllers.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.Controllers.Node.AppChosenController;
import org.example.Models.Arbre;
import org.example.Models.LectureCSV;
import org.example.Models.Notification;
import org.example.Models.NotificationType;
import org.example.java_project.Application;

import java.io.File;
import java.io.FileReader;
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
    public static final String REPERTOIRE_SERVICE = "Municipalite";

    @FXML
    private Label nbAbonnesLabel;

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

    @FXML
    public void initialize() {
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
        updateNbLabels();
    }

    private void loadNotifications() throws IOException {
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "notifications.json").toFile();
        if (!file.exists()) {
            System.out.println("No notifications found.");
            return;
        }
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        notificationsData.setAll(data);
    }

    private void handleNotificationClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
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
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "notifications.json").toFile(), notificationsData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save notifications: " + e.getMessage());
        }
    }

    @FXML
    private void onSendButtonClick() throws IOException {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Envoyer une notification");

        VBox dialogVBox = new VBox();
        dialogVBox.setSpacing(10);

        ComboBox<String> typesComboBox = new ComboBox<>();
        typesComboBox.setPromptText("Choisir une catégorie");
        typesComboBox.getItems().add(String.valueOf(NotificationType.ABATTAGE));
        typesComboBox.getItems().add(String.valueOf(NotificationType.PLANTATION));
        typesComboBox.getItems().add(String.valueOf(NotificationType.CLASSIFICATION));

        ComboBox<String> treesComboBox = new ComboBox<>();
        treesComboBox.setPromptText("Choisir un arbre");
        for (Arbre arbre : LectureCSV.arbresList) {
            treesComboBox.getItems().add(arbre.getLibelleFr() + " | " + arbre.getLieuAdresse());
        }

        TextArea messageField = new TextArea();
        messageField.setPromptText("Entrer un message");

        dialogVBox.getChildren().addAll(typesComboBox, treesComboBox, messageField);

        dialog.getDialogPane().setContent(dialogVBox);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String selectedType = typesComboBox.getValue();
                if (selectedType == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Contraintes");
                    alert.setHeaderText("Champ vide non autorisé");
                    alert.setContentText("Vous devez choisir le type de la notification.");
                    alert.showAndWait();
                }
                String selectedTree = treesComboBox.getValue();
                String message = messageField.getText();
                System.out.println("selectedType: " + selectedType + " selectedTree: " + selectedTree + " Message: " + message);
                // Handle the request logic
                Map<String, Object> notification = new HashMap<>();
                notification.put("timestamps", new Date());
                if(selectedTree != null) {
                    notification.put("message", "Type: " + selectedType + "\nArbre existant concerné: " + selectedTree + "\nContenu: " + message);
                }
                else {
                    notification.put("message", "Type: " + selectedType + "\nContenu: " + message);
                }
                notification.put("sender", "Service des Espaces verts");
                notification.put("status", false);
                File aFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "abonnes.json").toFile();
                if(!aFile.exists()) {
                    try {
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(aFile, List.of());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    List<Map<String, Object>> abonnes = objectMapper.readValue(aFile, new TypeReference<List<Map<String, Object>>>() {});
                    if(abonnes.size() > 0) {
                        for (Map<String, Object> ab : abonnes) {
                            File abNotifFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, ab.get("email").toString(), "notifications.json").toFile();
                            if (!abNotifFile.exists()) {
                                objectMapper.writerWithDefaultPrettyPrinter().writeValue(abNotifFile, List.of());
                            }
                            List<Map<String, Object>> notifications = objectMapper.readValue(abNotifFile, new TypeReference<List<Map<String, Object>>>() {});
                            notifications.add(notification);
                            objectMapper.writerWithDefaultPrettyPrinter().writeValue(abNotifFile, notifications);
                        }
                    }
                    notificationsData.add(notification);
                    saveNotifications();
//                    File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "notifications.json").toFile();
//                    if(!file.exists()) { objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, List.of()); }
//                    List<Map<String, Object>> notificationsS = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
//                    notificationsS.add(notification);
//                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, notificationsS);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void updateNbLabels() {
        try {
            if (REPERTOIRE_DE_BASE == null || REPERTOIRE_SERVICE == null) {
                throw new IllegalArgumentException("Chemin inexistant.");
            }
            File jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "abonnes.json").toFile();
            if (!jsonFile.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, List.of());
            }
            List<Map<String, Object>> abonnesData = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            nbAbonnesLabel.setText(abonnesData.size() + " Abonnée·s");

        } catch (IOException e) {
            showErrorDialog("Erreur", "Problème de lecture de données: " + e.getMessage());
        }
    }

    @FXML
    public void gestionArbre(javafx.event.ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("AppListeArbres.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Arbres");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Fermer(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
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
