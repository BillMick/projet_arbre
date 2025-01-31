package org.example.Controllers.Node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Controllers.Association.AssociationDashboardController;
import org.example.Controllers.Membre.MemberDashboardController;
import org.example.Models.Association;
import org.example.Models.Donateur;
import org.example.java_project.Application;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AppChosenController {

    public static Map<String, Object> infosAssociation;
    public static Map<String, Object> infosService;
    public static Map<String, Object> infosMembre1;
    public static Map<String, Object> infosMembre2;
    public static Map<String, Object> infosMembre3;
    public static Map<String, Object> infosMembre4;
    public static Map<String, Object> infosMembre5;

    public static final String REPERTOIRE_DE_BASE = "Storage";
    public static final String REPERTOIRE_ASSOC = "Associations";
    public static final String REPERTOIRE_MEMBRES = "Members";
    public static final String REPERTOIRE_SERVICE = "Municipalite";
    // public static final String REPERTOIRE_PROPRIETAIRE = "assoc@mail.com"; // à dynamiser .......
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private TextField associationField;

    @FXML
    private TextField presidentField;
    @FXML
    private TextField memberField;

    @FXML
    public void initialize() {}

    @FXML
    public void handleAssociationLogin() throws IOException {
        String association = associationField.getText();
        String president = presidentField.getText();
        Path path = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, association);
        File dir = new File(path.toString());
        if (!dir.exists()) {
            clearFields();
            showErrorDialog("Erreur de connexion1", "Identifiants incorrects ou inexistants. Veuillez réessayer.");
            return;
        }
        path = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, association, "infos.json");
        dir = new File(path.toString());
        if (!dir.exists()) {
            clearFields();
            showErrorDialog("Erreur de connexion2", "Identifiants incorrects ou inexistants. Veuillez réessayer.");
            return;
        }
        infosAssociation = objectMapper.readValue(dir, new TypeReference<Map<String, Object>>() {});
        if (infosAssociation.get("email").equals(association) && infosAssociation.get("isPresident").equals(president)) {
            try {
                System.out.println(infosAssociation);
                Stage stage = (Stage) associationField.getScene().getWindow();
                stage.close();
                onAssociationButtonClick(infosAssociation);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clearFields();
        showErrorDialog("Erreur de connexion", "Identifiants incorrects ou inexistants. Veuillez réessayer.");
    }

    @FXML
    public void handleMemberLogin() throws IOException {
        String email = memberField.getText();
        Path path = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_MEMBRES, email);
        File dir = new File(path.toString());
        if (!dir.exists()) {
            memberField.clear();
            showErrorDialog("Erreur de connexion1", "Identifiants incorrects ou inexistants. Veuillez réessayer.");
            return;
        }
        path = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_MEMBRES, email, "infos.json");
        dir = new File(path.toString());
        if (!dir.exists()) {
            memberField.clear();
            showErrorDialog("Erreur de connexion2", "Identifiants incorrects ou inexistants. Veuillez réessayer.");
            return;
        }
        infosMembre1 = objectMapper.readValue(dir, new TypeReference<Map<String, Object>>() {});
        if (infosMembre1.get("email").equals(email)) {
            try {
                System.out.println(infosMembre1);
                Stage stage = (Stage) memberField.getScene().getWindow();
                stage.close();
                onMemberButtonClick(infosMembre1);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        memberField.clear();
        showErrorDialog("Erreur de connexion", "Identifiants incorrects ou inexistants. Veuillez réessayer.");
    }

    public void handleAssociationRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("register.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Association | Création de compte");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Error", "Unable to load the registration form: " + e.getMessage());
        }
    }

    private void clearFields() {
        if (associationField.getText() != null) associationField.clear();
        if (presidentField.getText() != null) presidentField.clear();
    }

    public void onMemberButtonClick(Map<String, Object> infos) {
        if (infos == null || infos.isEmpty()) {
            showErrorDialog("Error", "Member data is invalid or missing.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("memberDashboard.fxml"));
            Parent root = loader.load();
            MemberDashboardController controller = loader.getController();
            controller.setInfos(infos);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Association | Dashboard");
            stage.show();
            Stage currentStage = (Stage) ((Node) memberField).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Error", "Unable to load the dashboard view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void onAssociationButtonClick(Map<String, Object> infos) {
        if (infos == null || infos.isEmpty()) {
            showErrorDialog("Error", "Association data is invalid or missing.");
            return;
        }
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationDashboard.fxml"));
            Parent root = loader.load();

            // Set data in the new controller
            AssociationDashboardController controller = loader.getController();
            controller.setInfos(infos);

            // Create and configure the new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Association | Dashboard");
            stage.show();

            // Close the current stage
            if (associationField != null) {
                Stage currentStage = (Stage) associationField.getScene().getWindow();
                currentStage.close();
            }
            if (emailField != null) {
                Stage currentStage = (Stage) emailField.getScene().getWindow();
                currentStage.close();
            }
            if(presidentField != null) {
                Stage currentStage = (Stage) presidentField.getScene().getWindow();
                currentStage.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Error", "Unable to load the dashboard view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField balanceField;

    @FXML
    public void handleRegisterAction() {
        if (emailField.getText().isEmpty() || nameField.getText().isEmpty() || balanceField.getText().isEmpty()) {
            showErrorDialog("Erreurr", "Tous les champs sont obligatoires.");
            return;
        }
        String email = emailField.getText();
        String name = nameField.getText();
        Double balance = Double.parseDouble(balanceField.getText());



        try {
            Path userPath = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, email);
            File userDir = new File(userPath.toString());
            if (userDir.exists()) {
                showErrorDialog("Erreur", "Un compte avec ce mail existe déjà.");
                return;
            }
            userDir.mkdirs();
            File userInfoFile = Paths.get(userPath.toString(), "infos.json").toFile();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("nom", name);
            userInfo.put("email", email);
            userInfo.put("solde", balance instanceof Number ? balance : 0.0);
            userInfo.put("isPresident", "");
            userInfo.put("presidentName", "");
            userInfo.put("souscription", false);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(userInfoFile, userInfo);
            infosAssociation = userInfo;
            showInfoDialog("Success", "Compte créé avec succès! \nIl vous est conseiller de créer au moins un membre et de désigner votre président avant la prochaine déconnexion.");
            onAssociationButtonClick(userInfo);
            closeCurrentStage();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Error", "Unable to create account: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackToLogin() {
        closeCurrentStage();
    }

    private void closeCurrentStage() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }

    private void showInfoDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void onAssociationLoginButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("loginAssociation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Association | Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onMemberButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("loginMembre.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Membre | Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onMunicipalityButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("AppPrincipale.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Service des Espaces verts");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Vous êtes sur le point de quitter l'application.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
