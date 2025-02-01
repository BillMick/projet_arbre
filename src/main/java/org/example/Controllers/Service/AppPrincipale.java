package org.example.Controllers.Service;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.java_project.Application;

import java.io.IOException;

public class AppPrincipale {

    @FXML
    public void initialize() throws IOException {

    }

    @FXML
    public void Principale(javafx.event.ActionEvent event){
        try {
            // Chargement du fichier FXML de la nouvelle vue

            FXMLLoader loader = new FXMLLoader(Application.class.getResource("AppPrincipale.fxml"));
            Parent root = loader.load();


            // Crée une image de fond
            Image backgroundImage = new Image(String.valueOf(Application.class.getResource("images/img.png")));
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
            );

            // Applique l'image de fond à la racine
            if (root instanceof Pane) {
                ((Pane) root).setBackground(new Background(bgImage));
            }


            // Création de la nouvelle fenêtre
            Stage nouvelleFenetre = new Stage();
            nouvelleFenetre.setTitle("Espaces Verts");
            nouvelleFenetre.setScene(new Scene(root));

            // Fermeture de la fenêtre principale
            Stage fenetrePrincipale = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            fenetrePrincipale.close();

            // Affichage de la nouvelle fenêtre
            nouvelleFenetre.setResizable(false);
            nouvelleFenetre.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gestionArbre(javafx.event.ActionEvent event){
        AppListeArbres AL = new AppListeArbres();
        AL.afficheTableau(event);
    }


    public void Fermer(javafx.event.ActionEvent event) {
        // Ferme la fenêtre de l'application
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();  // Ferme la fenêtre
    }


    @FXML
    public void ouvrirNotifications() {
        try {
            // Charger le fichier FXML pour la fenêtre des notifications
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Notification.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre pour afficher les notifications
            Stage stage = new Stage();
            stage.setTitle("Notifications");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onVotesButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("serviceVotes.fxml"));
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

    @FXML
    public void onNotificationsButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("serviceNotifications.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Notifications");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
