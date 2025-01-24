package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Notification;
import controller.NotificationController;
import javafx.application.Application;
import javafx.scene.Parent;

public class NotificationView extends Application {

    private NotificationController notificationController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML pour la fenêtre des notifications
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Notification.fxml"));
        Parent root = loader.load();

        // Accéder au contrôleur de la vue FXML et initialiser la logique
        notificationController = loader.getController();
        notificationController.initialize();

        // Créer la scène et définir la fenêtre principale
        Scene scene = new Scene(root);
        primaryStage.setTitle("Notifications");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode statique pour lancer la fenêtre des notifications
    public static void showNotificationWindow() {
        launch();
    }

    public static void main(String[] args) {
        launch(args); // Lancer l'application
    }
}
