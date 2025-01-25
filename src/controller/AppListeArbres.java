package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;

import static model.LectureCSV.arbresList;

public class AppListeArbres {

    @FXML
    private TableView<Arbre> tableView;

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

        tableView.getColumns().addAll(colIDBase, colTypeEmp,colDOM, colArrd,colCompAdresse,colNum,colLieu, colGenre,colEspece,colVariete, colCirconference, colHauteur, colStadeDev, colRemarquable, colGeo2D);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        System.out.println("Initialisation des colonnes terminée.");
    }




    @FXML
    public void afficheTableau(javafx.event.ActionEvent event) {
        try {
            // Chargement du fichier FXML de la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AppListeArbres.fxml"));
            Parent root = loader.load();

            // Création de la nouvelle fenêtre
            Stage nouvelleFenetre = new Stage();
            nouvelleFenetre.setTitle("Liste des Arbres");
            nouvelleFenetre.setScene(new Scene(root));
            initialize();

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
    public void Retours(javafx.event.ActionEvent event){
        AppPrincipale AP = new AppPrincipale();
        AP.Principale(event);
    }
}