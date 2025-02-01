package org.example.Controllers.Service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.Controllers.Node.AppChosenController;
import org.example.Models.Membre;
import org.example.java_project.Application;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TreeFormController {

    private Map<String, Object> infos = AppChosenController.infosMembre1;
    public void setInfos(Map<String, Object> infos) {
        this.infos = infos;
        System.out.println(infos);
    }

    public static final String REPERTOIRE_DE_BASE = "Storage";
    public static final String REPERTOIRE_ASSOC = "Associations";
    public static final String REPERTOIRE_MEMBRES = "Members";
    public static final String REPERTOIRE_SERVICE = "Municipalite";
    // private String REPERTOIRE_PROPRIETAIRE = (String) infos.get("email");
    // private String REPERTOIRE_COURANT = "Courant";



    private static final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private TextField typeEmplacementField;

    @FXML
    public void initialize() {
        File directory = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE).toFile();
        if (directory.exists() && directory.isDirectory()) {
            //
        } else {
            System.out.println("Le dossier spécifié n'existe pas.");
        }
    }

    @FXML
    private void ouvrirFenetreAjouterArbre() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("treeForm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajout d'arbre");

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifSuppArbre(javafx.event.ActionEvent event){
        FenetreModifSuppArbre MSA = new FenetreModifSuppArbre();
        MSA.afficheTableau(event);
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onBackButtonClick() {
        Stage stage = (Stage) typeEmplacementField.getScene().getWindow();
        stage.close();
    }
}
