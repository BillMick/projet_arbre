package controller;/*package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Notification;

public class NotificationController {

    @FXML
    private ListView<String> receivedNotificationsList;  // Liste des notifications reçues
    @FXML
    private ListView<String> sentNotificationsList;      // Liste des notifications envoyées

    private Notification notificationModel;

    public NotificationController() {
        notificationModel = new Notification();
    }

    // Initialisation de la fenêtre avec des notifications
    public void initialize() {
        notificationModel.initializeNotifications();  // Charger les notifications d'exemple

        // Ajouter les notifications existantes aux ListView
        receivedNotificationsList.getItems().addAll(notificationModel.getReceivedNotifications());
        sentNotificationsList.getItems().addAll(notificationModel.getSentNotifications());
    }

    // Méthode pour mettre à jour les notifications envoyées
    public void refreshSentNotifications() {
        sentNotificationsList.getItems().clear();
        sentNotificationsList.getItems().addAll(notificationModel.getSentNotifications());
    }

    // Accéder au modèle de notification pour d'autres contrôleurs
    public Notification getNotificationModel() {
        return notificationModel;
    }
}
*/


import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.io.IOException;

public class NotificationController {

    @FXML
    private ListView<String> receivedNotificationsList;  // Liste des notifications reçues
    @FXML
    private ListView<String> sentNotificationsList;      // Liste des notifications envoyées

    private Notification notificationModel;

    public NotificationController() {
        notificationModel = new Notification();
    }

    // Initialisation de la fenêtre avec des notifications
    public void initialize() {
        // Charger les notifications envoyées depuis le fichier JSON
        loadSentNotifications(); // Charger uniquement les notifications à envoyer depuis le fichier JSON

        // Ajouter les notifications existantes aux ListView
        receivedNotificationsList.getItems().addAll(notificationModel.getReceivedNotifications());
        // La section "Notifications à Envoyer" sera remplie avec les notifications chargées depuis JSON
        sentNotificationsList.getItems().addAll(notificationModel.getSentNotifications());
    }

    // Charger les notifications envoyées depuis le fichier JSON
    private void loadSentNotifications() {
        sentNotificationsList.getItems().clear();  // Vider la liste avant de charger les nouvelles notifications

        try {
            // Charger le fichier JSON avec les notifications
            Gson gson = new Gson();
            FileReader reader = new FileReader("projet_arbre/notifications.json");
            JsonArray notificationsArray = gson.fromJson(reader, JsonArray.class);

            // Pour chaque notification dans le fichier JSON, on l'ajoute à la ListView
            for (int i = 0; i < notificationsArray.size(); i++) {
                JsonObject notificationObj = notificationsArray.get(i).getAsJsonObject();
                String type = notificationObj.get("type").getAsString();
                String message = notificationObj.get("message").getAsString();

                // Ajouter le message formaté à la ListView
                String formattedNotification = type + " : " + message;
                sentNotificationsList.getItems().add(formattedNotification);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour les notifications envoyées
    public void refreshSentNotifications() {
        sentNotificationsList.getItems().clear();
        for (String notification : notificationModel.getSentNotifications()) {
            sentNotificationsList.getItems().add(notification);  // Ajouter à la ListView
        }
    }

    // Accéder au modèle de notification pour d'autres contrôleurs
    public Notification getNotificationModel() {
        return notificationModel;
    }
}
