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
import org.example.Models.Arbre;
import org.example.Models.LectureCSV;
import org.example.Models.Membre;
import org.example.Models.NotificationType;
import org.example.java_project.Application;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @FXML private TextField idField;
    @FXML private TextField typeEmplacementField;
    @FXML private TextField idEmplacementField;
    @FXML private TextField domanialiteField;
    @FXML private TextField arrondissementField;
    @FXML private TextField complementAdresseField;
    @FXML private TextField numeroField;
    @FXML private TextField lieuAdresseField;
    @FXML private TextField libelleFrancaisField;
    @FXML private TextField genreField;
    @FXML private TextField especeField;
    @FXML private TextField varieteCultivarField;
    @FXML private TextField circonferenceField;
    @FXML private TextField hauteurField;
    @FXML private TextField stadeDeveloppementField;
    @FXML private TextField geoPoint2DField;
    @FXML private CheckBox remarquableCheckbox;

    @FXML
    public void initialize() {
        File directory = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE).toFile();
        if (directory.exists() && directory.isDirectory()) {
            generateUniqueId();
        } else {
            System.out.println("Le dossier spécifié n'existe pas.");
        }
    }

    private void generateUniqueId() {
        idField.setText(UUID.randomUUID().toString());
    }

    @FXML
    public void addTree() {
        try {
            String idBaseSaisi = idField.getText().trim();
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
                boolean isRemarquable = remarquableCheckbox.isSelected();
                Arbre nouvelArbre = new Arbre(
                        idField.getText(),
                        typeEmplacementField.getText(),
                        domanialiteField.getText(),
                        arrondissementField.getText(),
                        complementAdresseField.getText(),
                        numeroField.getText(),
                        lieuAdresseField.getText(),
                        idEmplacementField.getText(),
                        libelleFrancaisField.getText(),
                        genreField.getText(),
                        especeField.getText(),
                        varieteCultivarField.getText(),
                        circonferenceField.getText(),
                        hauteurField.getText(),
                        stadeDeveloppementField.getText(),
                        isRemarquable ? String.valueOf(Arbre.StatutArbre.REMARQUABLE) : String.valueOf(Arbre.StatutArbre.NONREMARQUABLE),
                        geoPoint2DField.getText()
                );
                LectureCSV.arbresList.add(0, nouvelArbre);
                saveArbresListToCSV(nouvelArbre, String.valueOf(Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "liste_arbres.csv").toFile()));

                // Ajouter une notification pour l'arbre ajouté
                String s = "Type: " + NotificationType.PLANTATION + "\nArbre: " + nouvelArbre;
                handleNewNotification(String.valueOf(NotificationType.PLANTATION), null, s);
//                String message = "Plantation de l'arbre avec l'ID " + txtIDBase.getText();
//                notificationModel.addSentNotification(NotificationType.PLANTATION, message);

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

    public static void saveArbresListToCSV(Arbre arbre, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(LectureCSV.arbresList.get(-1))); ///
            writer.newLine();
            for (int i=0; i < LectureCSV.arbresList.size(); i++) {
                writer.write(arbreToCSVLine(LectureCSV.arbresList.get(i)));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String arbreToCSVLine(Arbre arbre) {
        return String.join(";",
                arbre.getIdBase(),
                arbre.getTypeEmplacement(),
                arbre.getDomanialite(),
                arbre.getArrondissement(),
                arbre.getComplementAdresse(),
                arbre.getNumero(),
                arbre.getLieuAdresse(),
                arbre.getIdEmplacement(),
                arbre.getLibelleFr(),
                arbre.getGenre(),
                arbre.getEspece(),
                arbre.getVarieteOuCultivar(),
                arbre.getCirconference(),
                arbre.getHauteur(),
                arbre.getStadeDeveloppement(),
                arbre.getStatut(),
                arbre.getGeo2D()
        );
    }

    public void handleNewNotification(String selectedType, String selectedTree, String message) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("timestamps", new Date());
        if(selectedTree != null) {
            notification.put("message", "Type: " + selectedType + "\nArbre existant concerné: " + selectedTree + "\nContenu: " + message);
        }
        else {
            notification.put("message", "Type: " + selectedType + "\nContenu: " + message);
        }
        notification.put("sender", "Service des Espaces verts");
        notification.put("status", false);
        File aFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "abonnes.json").toFile();
        if(!aFile.exists()) {
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(aFile, List.of());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            List<Map<String, Object>> abonnes = objectMapper.readValue(aFile, new TypeReference<List<Map<String, Object>>>() {});
            if(abonnes.size() > 0) {
                for (Map<String, Object> ab : abonnes) {
                    File abNotifFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, ab.get("email").toString(), "notifications.json").toFile();
                    if (!abNotifFile.exists()) {
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(abNotifFile, List.of());
                    }
                    List<Map<String, Object>> notifications = objectMapper.readValue(abNotifFile, new TypeReference<List<Map<String, Object>>>() {});
                    notifications.add(notification);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(abNotifFile, notifications);
                }
            }
            File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "notifications.json").toFile();
            if(!file.exists()) { objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, List.of()); }
            List<Map<String, Object>> notificationsS = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
            notificationsS.add(notification);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, notificationsS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetFormFields() {
        idField.clear();
        typeEmplacementField.clear();
        domanialiteField.clear();
        arrondissementField.clear();
        complementAdresseField.clear();
        numeroField.clear();
        lieuAdresseField.clear();
        complementAdresseField.clear();
        idEmplacementField.clear();
        libelleFrancaisField.clear();
        genreField.clear();
        especeField.clear();
        varieteCultivarField.clear();
        circonferenceField.clear();
        hauteurField.clear();
        stadeDeveloppementField.clear();
        geoPoint2DField.clear();
        remarquableCheckbox.setSelected(false);
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
