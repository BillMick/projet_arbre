package org.example.Controllers.Association;

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
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Controllers.Node.AppChosenController;
import org.example.Models.Arbre;
import org.example.Models.Association;
import org.example.Models.Dette;
import org.example.java_project.Application;

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AssociationDashboardController {

    private Map<String, Object> infos = AppChosenController.infosAssociation;
    public void setInfos(Map<String, Object> infos) {
        this.infos = infos;
        System.out.println(infos);
    }

    public static final String REPERTOIRE_DE_BASE = "Storage";
    public static final String REPERTOIRE_ASSOC = "Associations";
    public static final String REPERTOIRE_MEMBRES = "Members";
    public static final String REPERTOIRE_SERVICE = "Municipalite";
    private String REPERTOIRE_PROPRIETAIRE = (String) infos.get("email");
    private String REPERTOIRE_COURANT = "Courant";

    @FXML
    private Label nbNotificationsLabel;

    @FXML
    private Label nbActivitesLabel;

    @FXML
    private Label anneeLabel;

    @FXML
    private Label presidentLabel;

    @FXML
    private TableView<Map<String, Object>> treesTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> locationColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;

    private ObservableList<Map<String, Object>> treesData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() throws IOException {
        Path yearPath = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE);
        File directory = yearPath.toFile();
        if (directory.exists() && directory.isDirectory()) {
            File[] subdirectories = directory.listFiles(File::isDirectory);
            if (subdirectories == null || subdirectories.length == 0) {
                onBeginButtonClick();
            } else {
                System.out.println("Il existe des sous dossiers: " + subdirectories.length);
                Arrays.sort(subdirectories, Comparator.comparingLong(File::lastModified).reversed());
                REPERTOIRE_COURANT = subdirectories[0].getName();
            }
        } else {
            System.out.println("Le dossier spécifié n'existe pas.");
        }

        // Set up the TableView columns
        nameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("name");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        locationColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("location");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        statusColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("status");
            return value == null ? null : new ReadOnlyObjectWrapper<>(value.toString());
        });

        // Set color based on the status
        statusColumn.setCellFactory(param -> {
            return new TableCell<Map<String, Object>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        switch (item) { //.toLowerCase()
                            case "REMARQUABLE":
                                setStyle("-fx-text-fill: #074994;"); // Blue
                                break;
                            case "ABATTU":
                                setStyle("-fx-text-fill: #8B0000;"); // Red
                                break;
                            case "en croissance":
                                setStyle("-fx-text-fill: #32CD32;"); // Green
                                break;
                            case "NON REMARQUABLE":
                            default:
                                setStyle("-fx-text-fill: black;"); // Default to black if no match
                                break;
                        }
                    }
                }
            };
        });

        // Load data into the TableView
        try {
            updateTableData();
        } catch (IOException e) {
            System.err.println("Error updating table data: " + e.getMessage());
        }

        updateNbLabels();
    }

    private void updateNbLabels() {
        try {
            if (REPERTOIRE_DE_BASE == null || REPERTOIRE_ASSOC == null || REPERTOIRE_PROPRIETAIRE == null || REPERTOIRE_COURANT == "Courant") {
                throw new IllegalArgumentException("Chemin inexistant.");
            }
            File jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "notifications.json").toFile();
            if (!jsonFile.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, List.of());
            }
            List<Map<String, Object>> notificationsData = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            nbNotificationsLabel.setText("Notification·s: " + notificationsData.size());

            File jsonFile1 = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "activites.json").toFile();
            if (!jsonFile1.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile1, List.of());
            }
            List<Map<String, Object>> activitiesData = objectMapper.readValue(jsonFile1, new TypeReference<List<Map<String, Object>>>() {});
            nbActivitesLabel.setText("Activité·s: " + activitiesData.size());

            File jsonFile2 = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "infos.json").toFile();
            if (!jsonFile2.exists()) {
                showErrorDialog("Erreur", "Quelque chose s'est mal passé.");
                return;
            }
            else {
                Map<String, Object> infos = objectMapper.readValue(jsonFile2, new TypeReference<Map<String, Object>>() {});
                if ((infos.containsKey("debut"))) {
                    anneeLabel.setText("Année d'exercice en cours: " + Association.dateFormat.format(infos.get("debut")));
                }
            }

//            File jsonFile3 = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "infos.json").toFile();
//            if (!jsonFile3.exists()) {
//                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile3, Map.of());
//            }
//            Map<String, Object> president = objectMapper.readValue(jsonFile3, new TypeReference<Map<String, Object>>() {});
            presidentLabel.setText("Président: " + infos.get("presidentName"));
        } catch (IOException e) {
            showErrorDialog("Erreur", "Problème de lecture de données: " + e.getMessage());
        }
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateTableData() throws IOException {
        treesTable.setItems(treesData);
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "trees.json").toFile();
        if (!file.exists()) {
            System.out.println("No trees data found.");
            return;
        }
        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        treesData.setAll(data);
    }

    @FXML
    public void onBeginButtonClick() throws IOException {
        File jsonFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "infos.json").toFile();
        if (!jsonFile.exists()) {
            showErrorDialog("Erreur", "Quelque chose s'est mal passé.");
            return;
        }
        Map<String, Object> infos = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Object>>() {});

        if (infos.containsKey("debut")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("L'année d'exercice actuellement en cours (" + Association.dateFormat.format(infos.get("debut")) +  ") n'est pas encore clôturée.");
            alert.showAndWait();
        } else {
            Date beginning = new Date();
            while (true) {
                Path yearPath = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, Association.dateFormat.format(beginning));
                File yearDir = new File(yearPath.toString());
                if (!yearDir.exists()) {
                    yearDir.mkdirs();
                    infos.put("debut", beginning);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, infos);
                    REPERTOIRE_COURANT = Association.dateFormat.format(beginning);
                    // Création de dettes
                    Path path = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "debts.json");
                    File dir = new File(path.toString());
                    Map<String, Object> dette = new HashMap<>();
                    dette.put("statut", Dette.StatutDette.IMPAYEE);
                    dette.put("type", Dette.TypeDette.FACTURE);
                    dette.put("crediteur", "Fournisseur");
                    dette.put("montant", 150.0);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(dir, dette);
                    Map<String, Object> dette1 = new HashMap<>();
                    dette1.put("statut", Dette.StatutDette.IMPAYEE);
                    dette1.put("type", Dette.TypeDette.FACTURE);
                    dette1.put("crediteur", "Etat");
                    dette1.put("montant", 35.0);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(dir, dette1);
                    break;
                }
                beginning = addDays(beginning, 1);
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, infos);
            updateNbLabels();
        }
    }

    public Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    @FXML
    public void onEndButtonClick() throws IOException {
        if (REPERTOIRE_DE_BASE == null || REPERTOIRE_ASSOC == null || REPERTOIRE_PROPRIETAIRE == null || REPERTOIRE_COURANT == "Courant") {
            throw new IllegalArgumentException("Chemin inexistant.");
        }
        // Nombre de donateurs
        File file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "donors.json").toFile();
        List<Map<String, Object>> donors = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        // Nombre de membres
        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "members.json").toFile();
        List<Map<String, Object>> members = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        // Nom du président
        File infoFile = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, "infos.json").toFile();
        Map<String, Object> infos = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
        // Dettes
        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "debts.json").toFile();
        List<Map<String, Object>> debts = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        // Dons
        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "dons.json").toFile();
        List<Map<String, Object>> dons = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        // Cotisations
        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "cotisations.json").toFile();
        List<Map<String, Object>> cotisations = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        // Activités
        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "activites.json").toFile();
        List<Map<String, Object>> activities = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        // Scrutin
        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "votes", "votes.json").toFile();
        List<Map<String, Object>> votes = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});

        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "periode.json").toFile();
        if (file.exists()) {
            showErrorDialog("Erreur", "Quelque chose s'est mal passé.");
            return;
        }
        Map<String, Object> periode = new HashMap<>();
        periode.put("debut", infos.get("debut"));
        periode.put("fin", new Date());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(infoFile, periode);
        infos.remove("debut");

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, infos);

        file = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_ASSOC, REPERTOIRE_PROPRIETAIRE, REPERTOIRE_COURANT, "rapportFinal.json").toFile();
        Map<String, Object> rapport = new HashMap<>();
        double sommeC = cotisations.stream()
                .filter(cotisation -> cotisation.containsKey("montant"))
                .mapToDouble(cotisation -> Double.parseDouble(cotisation.get("montant").toString()))
                .sum();
        double sommeD = dons.stream()
                .filter(cotisation -> cotisation.containsKey("montant"))
                .mapToDouble(cotisation -> Double.parseDouble(cotisation.get("montant").toString()))
                .sum();
        double sommeDe = debts.stream()
                .filter(cotisation -> cotisation.containsKey("montant"))
                .mapToDouble(cotisation -> Double.parseDouble(cotisation.get("montant").toString()))
                .sum();
        rapport.put("debut", periode.get("debut"));
        rapport.put("fin", periode.get("fin"));
        rapport.put("activites", activities);
        rapport.put("cotisations", cotisations);
        rapport.put("SommeCotisations", sommeC);
        rapport.put("dons", sommeD);
        rapport.put("SommeDons", debts);
        rapport.put("recetteTotale", dons);
        rapport.put("dettes", debts);
        rapport.put("SommeDettes", sommeDe);
        rapport.put("soldeFinal", infos.get("solde"));
        rapport.put("classificationProposee", votes);
        rapport.put("nombreDeMembres", members.size());
        rapport.put("nombreDeDonateurs", donors.size());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, rapport);
    }

    @FXML
    public void onDonorsButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDesDonateurs.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion de Trésorerie");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onMembersButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDesMembres.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Membres");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onNotificationsButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationNotifications.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface des Notifications");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onActivitiesButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDesActivites.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Activités");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onTreasuryButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDeTresorerie.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion de Trésorerie");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onVotesButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationVotes.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Visualisation des Votes");

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
}
