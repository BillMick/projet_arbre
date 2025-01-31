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
public class AppGestion {
    @FXML
    public void Gestion(javafx.event.ActionEvent event){
        try {
            // Chargement du fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("AppGestion.fxml"));
            Parent root = loader.load();
            Image backgroundImage = new Image(String.valueOf(Application.class.getResource("images/img2.png")));
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
            );
            if (root instanceof Pane) {
                ((Pane) root).setBackground(new Background(bgImage));
            }
            Stage nouvelleFenetre = new Stage();
            nouvelleFenetre.setTitle("Gestion Arbres");
            nouvelleFenetre.setScene(new Scene(root));

            // Désactiver la croix de fermeture

            nouvelleFenetre.setOnCloseRequest(eventClose -> {
                eventClose.consume();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ne pas utiliser ce bouton");
                alert.setHeaderText("Opération Impossible");
                alert.setContentText("Veuillez utiliser le bouton Quitter à la page d'Accueil ");
                alert.showAndWait();;
            });

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FenetreAjouterArbre.fxml"));
            Parent root = loader.load();

            // Crée une image de fond
            Image backgroundImage = new Image(String.valueOf(getClass().getResource("images/img3.png")));
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
            //Ouvre la fenêtre permettant de modifier ou supprimer des arbres.

    public void modifSuppArbre(javafx.event.ActionEvent event){
        FenetreModifSuppArbre MSA = new FenetreModifSuppArbre();
        MSA.afficheTableau(event);
    }


}
