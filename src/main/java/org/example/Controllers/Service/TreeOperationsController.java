package org.example.Controllers.Service;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class TreeOperationsController {

    @FXML
    public void initialize() {

    }


    @FXML
    public void onTreeOperationsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("treeForm.fxml"));
            Parent root = loader.load();
            Stage nouvelleFenetre = new Stage();
            nouvelleFenetre.setTitle("Gestion des Arbres");
            nouvelleFenetre.setScene(new Scene(root));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("treeForm.fxml"));
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
