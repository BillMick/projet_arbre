package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import model.Notification;
import model.NotificationType;

public class NotificationController {

    @FXML
    private ListView<String> receivedNotificationsList;  // Liste des notifications reçues
    @FXML
    private ListView<String> sentNotificationsList;      // Liste des notifications envoyées
    @FXML
    private Button plantNotificationButton;
    @FXML
    private Button classifyNotificationButton;
    @FXML
    private Button cutNotificationButton;

    private Notification notificationModel;

    public NotificationController() {
        notificationModel = new Notification();
    }

    // Initialisation de la fenêtre avec des notifications d'exemple
    public void initialize() {
        notificationModel.initializeNotifications();  // Remplir les listes de notifications

        // Ajouter les notifications à afficher dans les ListView
        receivedNotificationsList.getItems().addAll(notificationModel.getReceivedNotifications());
        sentNotificationsList.getItems().addAll(notificationModel.getSentNotifications());

        // Ajout des événements pour les boutons
        plantNotificationButton.setOnAction(event -> sendNotification(NotificationType.PLANTATION, "Arbre planté à l'emplacement Z"));
        classifyNotificationButton.setOnAction(event -> sendNotification(NotificationType.CLASSIFICATION, "Classification des arbres terminée"));
        cutNotificationButton.setOnAction(event -> sendNotification(NotificationType.ABATTAGE, "Abattage de l'arbre à l'emplacement Z"));
    }

    // Envoi d'une notification
    private void sendNotification(NotificationType type, String message) {
        notificationModel.addSentNotification(type, message);
        // Rafraîchir la liste des notifications envoyées
        sentNotificationsList.getItems().clear();
        sentNotificationsList.getItems().addAll(notificationModel.getSentNotifications());
    }
}
