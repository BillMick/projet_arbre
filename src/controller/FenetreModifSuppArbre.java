package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;


import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static model.LectureCSV.arbresList;

public class FenetreModifSuppArbre {

    @FXML
    private TableView<Arbre> tableView;


    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Arbre, String> colIDBase;
    @FXML
    private TableColumn<Arbre, String> colTypeEmp;
    @FXML
    private TableColumn<Arbre, String> colDOM;
    @FXML
    private TableColumn<Arbre, String> colArrd;
    @FXML
    private TableColumn<Arbre, String> colCompAdresse;
    @FXML
    private TableColumn<Arbre, String> colNum;
    @FXML
    private TableColumn<Arbre, String> colLieu;
    @FXML
    private TableColumn<Arbre, String> colIDEmp;
    @FXML
    private TableColumn<Arbre, String> colLibelle;
    @FXML
    private TableColumn<Arbre, String> colGenre;
    @FXML
    private TableColumn<Arbre, String> colEspece;
    @FXML
    private TableColumn<Arbre, String> colVariete;
    @FXML
    private TableColumn<Arbre, String> colCirconference;
    @FXML
    private TableColumn<Arbre, String> colHauteur;
    @FXML
    private TableColumn<Arbre, String> colStadeDev;
    @FXML
    private TableColumn<Arbre, String> colRemarquable;
    @FXML
    private TableColumn<Arbre, String> colGeo2D;

    @FXML
    public void initialize() {
        if (tableView == null) {
            tableView = new TableView<>();
            System.out.println("TableView initialisé manuellement !");
        }
        initializeColumns();
        tableView.setItems(arbresList);

    }

    @FXML
    private void initializeColumns() {
        System.out.println("Début de l'initialisation des colonnes...");
        colIDBase = new TableColumn<Arbre, String>("IDBASE");
        colIDBase.setCellValueFactory(new PropertyValueFactory<Arbre, String>("idBase"));

        colTypeEmp = new TableColumn<Arbre, String>("TYPE EMPLACEMENT");
        colTypeEmp.setCellValueFactory(new PropertyValueFactory<Arbre, String>("typeEmplacement"));

        colDOM = new TableColumn<Arbre, String>("DOMANIALITE");
        colDOM.setCellValueFactory(new PropertyValueFactory<>("domanialite"));

        colArrd = new TableColumn<Arbre, String>("ARRONDISSEMENT");
        colArrd.setCellValueFactory(new PropertyValueFactory<>("arrondissement"));

        colCompAdresse = new TableColumn<Arbre, String>("COMPLEMENT ADRESSE");
        colCompAdresse.setCellValueFactory(new PropertyValueFactory<>("complementAdresse"));

        colNum = new TableColumn<Arbre, String>("NUMERO");
        colNum.setCellValueFactory(new PropertyValueFactory<Arbre, String>("numero"));

        colLieu = new TableColumn<Arbre, String>("LIEU/ADRESSE");
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieuAdresse"));

        colIDEmp = new TableColumn<Arbre, String>("IDEMPLACEMENT");
        colIDEmp.setCellValueFactory(new PropertyValueFactory<>("idEmplacement"));

        colLibelle = new TableColumn<Arbre, String>("LIBELLE FR");
        colLibelle.setCellValueFactory(new PropertyValueFactory<Arbre, String>("libelle"));

        colGenre = new TableColumn<Arbre, String>("GENRE");
        colGenre.setCellValueFactory(new PropertyValueFactory<Arbre, String>("genre"));

        colEspece = new TableColumn<Arbre, String>("ESPECE");
        colEspece.setCellValueFactory(new PropertyValueFactory<Arbre, String>("espece"));

        colVariete = new TableColumn<Arbre, String>("VARIETE OU CULTIVAR");
        colVariete.setCellValueFactory(new PropertyValueFactory<Arbre, String>("varieteOuCultivar"));

        colCirconference = new TableColumn<Arbre, String>("CIRCONFERENCE");
        colCirconference.setCellValueFactory(new PropertyValueFactory<Arbre, String>("circonference"));

        colHauteur = new TableColumn<Arbre, String>("HAUTEUR");
        colHauteur.setCellValueFactory(new PropertyValueFactory<Arbre, String>("hauteur"));

        colStadeDev = new TableColumn<Arbre, String>("STADE DE DEVELOPPEMENT");
        colStadeDev.setCellValueFactory(new PropertyValueFactory<Arbre, String>("stadeDeveloppement"));

        colRemarquable = new TableColumn<Arbre, String>("REMARQUABLE");
        colRemarquable.setCellValueFactory(new PropertyValueFactory<Arbre, String>("remarquable"));

        colGeo2D = new TableColumn<Arbre, String>("GEO_2D");
        colGeo2D.setCellValueFactory(new PropertyValueFactory<Arbre, String>("geo2D"));

        tableView.getColumns().addAll(colIDBase, colTypeEmp, colDOM, colArrd, colCompAdresse, colNum, colLieu, colGenre, colEspece, colVariete, colCirconference, colHauteur, colStadeDev, colRemarquable, colGeo2D);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        System.out.println("Initialisation des colonnes terminée.");
    }


    @FXML
    public void afficheTableau(javafx.event.ActionEvent event) {
        try {
            // Chargement du fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FenetreModifSuppArbre.fxml"));
            Parent root = loader.load();

            // Création de la nouvelle fenêtre
            Stage nouvelleFenetre = new Stage();
            nouvelleFenetre.setTitle("Liste des Arbres");
            nouvelleFenetre.setScene(new Scene(root));
            initialize();
            // Désactiver la croix (bouton de fermeture)
            nouvelleFenetre.setOnCloseRequest(closeEvent -> {
                closeEvent.consume(); // Empêche la fermeture
                System.out.println("Fermeture désactivée.");
            });
            // Fermeture de la fenêtre principale
            Stage fenetrePrincipale = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            fenetrePrincipale.close();

            // Affichage de la nouvelle fenêtre
            nouvelleFenetre.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Retours(javafx.event.ActionEvent event) {
        AppPrincipale AP = new AppPrincipale();
        AP.Principale(event);
    }

    @FXML
    public void activerRecherche(javafx.event.ActionEvent event) {

        // Création de la FilteredList à partir de la liste observable existante
        FilteredList<Arbre> filteredList = new FilteredList<>(arbresList, p -> true);

        // Lier le champ de recherche à la FilteredList
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(arbre -> {
                // Si le champ de recherche est vide, afficher tous les éléments
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convertir le texte de recherche en minuscules pour comparaison
                String lowerCaseFilter = newValue.toLowerCase();

                // Comparer chaque propriété pertinente de l'objet Arbre
                return arbre.getIdBase().toLowerCase().contains(lowerCaseFilter) ||
                        arbre.getDomanialite().toLowerCase().contains(lowerCaseFilter) ||
                        arbre.getArrondissement().toLowerCase().contains(lowerCaseFilter) ||
                        arbre.getComplementAdresse().toLowerCase().contains(lowerCaseFilter) ||
                        arbre.getLibelleFr().toLowerCase().contains(lowerCaseFilter) ||
                        arbre.getGenre().toLowerCase().contains(lowerCaseFilter) ||
                        arbre.getEspece().toLowerCase().contains(lowerCaseFilter) ||
                        arbre.getVarieteOuCultivar().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Appliquer les données filtrées au TableView
        tableView.setItems(filteredList);

    }

    @FXML
    public void Modifier(javafx.event.ActionEvent event) {
        // Get the selected tree from the table
        Arbre selectedArbre = tableView.getSelectionModel().getSelectedItem();

        // Check if a tree is selected
        if (selectedArbre == null) {
            // Show an alert if no tree is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Modification");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un arbre à modifier.");
            alert.showAndWait();
            return;
        }

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de Modification");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir modifier cet arbre ?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FenetreModifierArbre Mod = new FenetreModifierArbre();
            Mod.FenetreModif(event, selectedArbre);
        }
    }


    @FXML
    public void Supprimer(javafx.event.ActionEvent event) {
        // Get the selected tree from the table
        Arbre selectedArbre = tableView.getSelectionModel().getSelectedItem();

        // Check if a tree is selected
        if (selectedArbre == null) {
            // Show an alert if no tree is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Suppression");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un arbre à supprimer.");
            alert.showAndWait();
            return;
        }

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet arbre ?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove the selected tree from the list
            arbresList.remove(selectedArbre);

            // Ajouter une notification d'abattage
            Notification notificationModel = new Notification();
            String notificationMessage = "Abattage de l'arbre de l'ID : " + selectedArbre.getIdBase();
            notificationModel.addSentNotification(NotificationType.ABATTAGE, notificationMessage);

            // Show success message
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Suppression");
            successAlert.setHeaderText(null);
            successAlert.setContentText("L'arbre a été supprimé avec succès. Une notification d'abattage a été ajoutée.");
            successAlert.showAndWait();
        }
    }

}
