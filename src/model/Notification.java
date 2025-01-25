package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Notification {

    private List<String> receivedNotifications;
    private List<String> sentNotifications;

    public Notification() {
        receivedNotifications = new ArrayList<>();
        sentNotifications = new ArrayList<>();
    }

    // Ajouter une notification reçue
    public void addReceivedNotification(NotificationType type, String message) {
        receivedNotifications.add(type.getDescription() + " : " + message);
    }

    // Ajouter une notification envoyée
    public void addSentNotification(NotificationType type, String message) {
        sentNotifications.add(type.getDescription() + " : " + message);
        // Enregistrer dans un fichier JSON chaque fois qu'une notification est envoyée
        saveToJson(type, message);
    }

    // Sauvegarder la notification dans un fichier JSON
  private void saveToJson(NotificationType type, String message) {
        try {
            File file = new File("projet_arbre/notifications.json");
            JsonArray notificationsArray;

            // Si le fichier existe, on charge les notifications existantes
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                reader.close();

                // Charger le tableau JSON existant
                notificationsArray = new com.google.gson.JsonParser().parse(jsonContent.toString()).getAsJsonArray();
            } else {
                // Sinon, on crée un tableau vide
                notificationsArray = new JsonArray();
            }

            // Créer un objet JSON pour la notification
            JsonObject notificationObject = new JsonObject();
            notificationObject.addProperty("type", type.getDescription());
            notificationObject.addProperty("message", message);

            // Ajouter l'objet JSON au tableau
            notificationsArray.add(notificationObject);

            // Sauvegarder le tableau JSON dans le fichier
            FileWriter writer = new FileWriter(file);
            writer.write(notificationsArray.toString()); // Sauvegarde le JSON formaté
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialiser des notifications d'exemple
    public void initializeNotifications() {
        addReceivedNotification(NotificationType.PLANTATION, "Arbre planté à l'emplacement X");
        addReceivedNotification(NotificationType.CLASSIFICATION, "Classification des arbres terminée");
        addSentNotification(NotificationType.ABATTAGE, "Abattage de l'arbre à l'emplacement Y");
    }

    // Retourner les notifications reçues
    public List<String> getReceivedNotifications() {
        return receivedNotifications;
    }

    // Retourner les notifications envoyées
    public List<String> getSentNotifications() {
        return sentNotifications;
    }
}
