package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Arbre;
import model.LectureCSV;
import model.Notification;

import java.io.IOException;

import static model.LectureCSV.arbresList;

public class FenetreModifierArbre {

    @FXML
    private TextField txtIDBase ;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtDomanialite;

    @FXML
    private TextField txtArrondissement;
    @FXML
    private TextField txtComplementAdresse;
    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtLieuAdresse;
    @FXML
    private TextField txtIdEmplacement;
    @FXML
    private TextField txtLibelleFrancais;
    @FXML
    private TextField txtGenre;
    @FXML
    private TextField txtEspece;
    @FXML
    private TextField txtVarieteCultivar;
    @FXML
    private TextField txtCirconference;
    @FXML
    private TextField txtHauteur;
    @FXML
    private TextField txtStadeDeveloppement;
    @FXML
    private TextField txtRemarquable;
    @FXML
    private TextField txtGeo;

    private Notification notificationModel = new Notification();

    private Arbre arbreAModifier;
    @FXML
    public void FenetreModif(ActionEvent event, Arbre arbreAModifier) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FenetreModifierArbre.fxml"));
            Parent root = loader.load();

            // Crée une image de fond
            Image backgroundImage = new Image(getClass().getResource("/images/img3.png").toExternalForm());
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

            // Récupérer le contrôleur associé au fichier FXML
            FenetreModifierArbre controller = loader.getController();

            // Transmettre l'arbre à modifier au contrôleur
            controller.setArbreAModifier(arbreAModifier);

            // Configurer et afficher la fenêtre
            Stage nouvelleFenetre = new Stage();
            nouvelleFenetre.setTitle("Modifier l'Arbre");
            nouvelleFenetre.setScene(new Scene(root));
            nouvelleFenetre.setResizable(false);
            nouvelleFenetre.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void modifierArbre(javafx.event.ActionEvent event) {
        try {

            String idBaseSaisi = txtIDBase.getText().trim();
            if (idBaseSaisi.isEmpty()) {
                throw new IllegalArgumentException("L'ID Base est obligatoire !");
            }

            // Vérifier si l'ID existe déjà
            boolean existeDeja = LectureCSV.arbresList.stream()
                    .anyMatch(arbre -> arbre.getIdBase().equals(idBaseSaisi));

            if (idBaseSaisi.equals(arbreAModifier.getIdBase())) {
                existeDeja = false;
            }

            if (existeDeja) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur d'ajout");
                alert.setHeaderText("L'arbre existe déjà");
                alert.setContentText("Un arbre avec cet ID Base existe déjà dans la liste.");
                alert.showAndWait();

            } else {
                Arbre nouvelArbre = new Arbre(
                        txtIDBase.getText(),
                        txtType.getText(),
                        txtDomanialite.getText(),
                        txtArrondissement.getText(),
                        txtComplementAdresse.getText(),
                        txtNumero.getText(),
                        txtLieuAdresse.getText(),
                        txtIdEmplacement.getText(),
                        txtLibelleFrancais.getText(),
                        txtGenre.getText(),
                        txtEspece.getText(),
                        txtVarieteCultivar.getText(),
                        txtCirconference.getText(),
                        txtHauteur.getText(),
                        txtStadeDeveloppement.getText(),
                        txtRemarquable.getText(),
                        txtGeo.getText()
                );

                arbresList.remove(arbreAModifier);
                LectureCSV.arbresList.add(0, nouvelArbre);

                // Afficher une alerte de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Arbre modifié");
                alert.setContentText("L'arbre a été modifié avec succès !");
                alert.showAndWait();

                resetFormFields();
            }

        } catch (IllegalArgumentException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Données invalides");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void setArbreAModifier(Arbre arbreAModifier) {
        this.arbreAModifier = arbreAModifier;

        // Remplir les champs avec les données de l'arbre
        txtIDBase.setText(arbreAModifier.getIdBase());
        txtType.setText(arbreAModifier.getTypeEmplacement());
        txtDomanialite.setText(arbreAModifier.getDomanialite());
        txtArrondissement.setText(arbreAModifier.getArrondissement());
        txtComplementAdresse.setText(arbreAModifier.getComplementAdresse());
        txtNumero.setText(arbreAModifier.getNumero());
        txtLieuAdresse.setText(arbreAModifier.getLieuAdresse());
        txtIdEmplacement.setText(arbreAModifier.getIdEmplacement());
        txtLibelleFrancais.setText(arbreAModifier.getLibelleFr());
        txtGenre.setText(arbreAModifier.getGenre());
        txtEspece.setText(arbreAModifier.getEspece());
        txtVarieteCultivar.setText(arbreAModifier.getVarieteOuCultivar());
        txtCirconference.setText(arbreAModifier.getCirconference());
        txtHauteur.setText(arbreAModifier.getHauteur());
        txtStadeDeveloppement.setText(arbreAModifier.getStadeDeveloppement());
        txtRemarquable.setText(arbreAModifier.getRemarquable());
        txtGeo.setText(arbreAModifier.getGeo2D());
    }


    // Méthode pour réinitialiser les champs du formulaire
    private void resetFormFields() {
        txtIDBase.clear();
        txtType.clear();
        txtDomanialite.clear();
        txtArrondissement.clear();
        txtComplementAdresse.clear();
        txtNumero.clear();
        txtLieuAdresse.clear();
        txtIdEmplacement.clear();
        txtLibelleFrancais.clear();
        txtGenre.clear();
        txtEspece.clear();
        txtVarieteCultivar.clear();
        txtCirconference.clear();
        txtHauteur.clear();
        txtStadeDeveloppement.clear();
        txtRemarquable.clear();
        txtGeo.clear();
    }

    @FXML
    public void Retours(javafx.event.ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
