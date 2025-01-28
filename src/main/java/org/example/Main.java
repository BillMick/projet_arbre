package org.example;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

import static org.example.Models.LectureCSV.loadCSVData;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        try {
            // Charge le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AppPrincipale.fxml"));
            Parent root = loader.load();
            // Désactiver la croix de fermeture
            primaryStage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ne pas utiliser ce bouton");
                alert.setHeaderText("Opération Impossible");
                alert.setContentText("Veuillez utiliser le bouton Quitter ");
                alert.showAndWait();
                event.consume();

            });
            loadCSVData("projet_arbre/resources/liste_arbres.csv");

            // Crée une image de fond
            Image backgroundImage = new Image(getClass().getResource("resources/images/img.png").toExternalForm());
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

            Scene scene = new Scene(root);
            primaryStage.setTitle("Espace Verts");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}