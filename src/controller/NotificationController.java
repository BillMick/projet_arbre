package controller;

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
