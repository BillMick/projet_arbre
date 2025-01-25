package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
public class AppGestion {
    @FXML
    public void Gestion(javafx.event.ActionEvent event){
        try {
            // Chargement du fichier FXML de la nouvelle vue

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AppGestion.fxml"));
            Parent root = loader.load();

            // Crée une image de fond
            Image backgroundImage = new Image(getClass().getResource("/images/img2.png").toExternalForm());
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
            nouvelleFenetre.setTitle("Gestion Arbres");
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
    public void Retours(javafx.event.ActionEvent event){
        AppPrincipale AP = new AppPrincipale();
        AP.Principale(event);
    }
    @FXML
    public void ouvrirFenetreAjouterArbre(javafx.event.ActionEvent event) {
        try {
            // Charger le fichier FXML de la fenêtre de saisie d'arbre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FenetreAjouterArbre.fxml"));
            Parent root = loader.load();

            // Crée une image de fond
            Image backgroundImage = new Image(getClass().getResource("/images/img2.png").toExternalForm());
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

            // Créer une nouvelle scène pour cette fenêtre
            Stage nouvelleFenetre = new Stage();
            nouvelleFenetre.setTitle("Ajouter un Arbre");
            nouvelleFenetre.setScene(new Scene(root));

            // Rendre la fenêtre non redimensionnable
            nouvelleFenetre.setResizable(false);

            // Afficher la fenêtre
            nouvelleFenetre.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modifSuppArbre(javafx.event.ActionEvent event){
        FenetreModifSuppArbre MSA = new FenetreModifSuppArbre();
        MSA.afficheTableau(event);
    }


}
