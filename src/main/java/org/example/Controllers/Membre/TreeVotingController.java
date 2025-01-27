package org.example.Controllers.Membre;


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
import org.example.Models.Membre;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TreeVotingController {

    @FXML
    private TableView<Map<String, Object>> treeTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> locationColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> actionColumn;

    private ObservableList<Map<String, Object>> treeData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TREE_FILE_PATH = "Storage/trees.json";
    private static final String VOTES_FILE_PATH = "Storage/votes/votes.json";

    private Membre currentUser;

    @FXML
    public void initialize() {
        // Initialiser l'utilisateur actuel
        // (ici, un utilisateur par défaut est utilisé)
        // Mais je n'ai pas compris comment obtenir l'utilisateur actuel dans ce système

//        currentUser = new Membre("JIA", "Yamin", "jia.yamin@example.com");

        // Configurer la liaison des données pour les colonnes
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


        // Ajouter dynamiquement un bouton de vote à chaque ligne
        actionColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button voteButton = new Button("Voter");
                    voteButton.setTextFill(Color.web("#4CAF50"));
                    voteButton.setOnAction(event -> handleVote(getTableRow().getIndex(), currentUser));
                    setGraphic(new HBox(voteButton));
                }
            }
        });

        // Charger les données des arbres
        try {
            loadTreeData();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Erreur", "Impossible de charger les données des arbres.");
        }

        treeTable.setItems(treeData);
    }

    private void loadTreeData() throws IOException {
        File file = new File(TREE_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Fichier de données des arbres introuvable.");
        }

        List<Map<String, Object>> data = objectMapper.readValue(file, new TypeReference<>() {});
        treeData.setAll(data);
    }

    private void handleVote(int rowIndex, Membre currentUser) {
        Map<String, Object> selectedTree = treeTable.getItems().get(rowIndex);

        try {
            // Lire les enregistrements de vote existants
            File file = new File(VOTES_FILE_PATH);
            List<Map<String, Object>> votes;
            if (file.exists()) {
                votes = objectMapper.readValue(file, new TypeReference<>() {});
            } else {
                votes = FXCollections.observableArrayList();
            }

            // Filtrer les votes de l'utilisateur actuel
            long userVoteCount = votes.stream()
                    .filter(vote -> vote.get("user").equals(currentUser.getEmail()))
                    .count();

            // Vérifier si l'utilisateur a déjà voté pour 5 arbres
            if (userVoteCount >= 5) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Limite de votes atteinte");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez atteint la limite maximale de 5 votes !");
                alert.showAndWait();
                return;
            }

            // Vérifier si l'utilisateur a déjà voté pour cet arbre
            boolean alreadyVoted = votes.stream()
                    .anyMatch(vote -> vote.get("name").equals(selectedTree.get("name")) &&
                            vote.get("user").equals(currentUser.getEmail()));
            if (alreadyVoted) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Vote en double");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez déjà voté pour cet arbre !");
                alert.showAndWait();
                return;
            }

            // Ajouter un nouvel enregistrement de vote
            Map<String, Object> newVote = Map.of(
                    "name", selectedTree.get("name"),
                    "location", selectedTree.get("location"),
                    "status", selectedTree.get("status"),
                    "user", currentUser.getEmail(),
                    "timestamp", System.currentTimeMillis()
            );

            // Mettre à jour la liste des votes
            votes.add(newVote);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, votes);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vote réussi");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez voté avec succès pour l'arbre : " + selectedTree.get("name"));
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Erreur de vote", "Impossible d'enregistrer votre vote, veuillez réessayer plus tard !");
        }
    }

    @FXML
    public void onTreasuryButtonClick() {
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("memberGestionDeTresorerie.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Treasury Interface");

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
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("MemberActivities.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Treasury Interface");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNotificationsButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("MemberNotifications.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Notifications");

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Stage stage = (Stage) treeTable.getScene().getWindow();
        stage.close();
    }
}
