package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AppPrincipale {

    @FXML
    public void listeArbres(javafx.event.ActionEvent event){
        try {
            // Chargement du fichier FXML de la nouvelle vue

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/.fxml"));
            Parent root = loader.load();

            // Crée une image de fond
            Image backgroundImage = new Image(getClass().getResource("/images/background1.jpg").toExternalForm());
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
            nouvelleFenetre.setTitle("PACMAN");
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

}
