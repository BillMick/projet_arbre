package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Arbre;
import model.LectureCSV;
import model.Notification;
import model.NotificationType;

public class FenetreAjouterArbre {

    @FXML
    private TextField txtIDBase;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtDomanialite;
    @FXML
    private TextField txtLatitude;
    @FXML
    private TextField txtLongitude;
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

    @FXML
    public void ajouterArbre() {
        try {
            String idBaseSaisi = txtIDBase.getText().trim();
            if (idBaseSaisi.isEmpty()) {
                throw new IllegalArgumentException("L'ID Base est obligatoire !");
            }

            boolean existeDeja = LectureCSV.arbresList.stream()
                    .anyMatch(arbre -> arbre.getIdBase().equals(idBaseSaisi));

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
                LectureCSV.arbresList.add(0, nouvelArbre);

                // Ajouter une notification pour l'arbre ajouté
                String message = "Plantation de l'arbre avec l'ID " + txtIDBase.getText();
                notificationModel.addSentNotification(NotificationType.ABATTAGE, message);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Arbre ajouté");
                alert.setContentText("L'arbre a été ajouté avec succès !");
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

    private void resetFormFields() {
        txtIDBase.clear();
        txtType.clear();
        txtDomanialite.clear();
        txtLatitude.clear();
        txtLongitude.clear();
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
}
