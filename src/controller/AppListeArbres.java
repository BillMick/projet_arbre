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
import model.Arbre;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;

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

    private ObservableList<Arbre> arbresList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tableView == null) {
            tableView = new TableView<>();
            System.out.println("TableView initialisé manuellement !");
        }
        initializeColumns();
        loadCSVData("C:\\Users\\yacin\\IdeaProjects\\espaces_verts2\\projet_arbre\\resources\\liste_arbres.csv");
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

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        System.out.println("Initialisation des colonnes terminée.");
    }

    @FXML
    public void loadCSVData(String filePath) {
        try {
            // Configuration du parser pour utiliser ; comme délimiteur
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';') // Définit le délimiteur à ;
                    .build();

            // Construction du lecteur CSV avec le parser
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                    .withCSVParser(parser)
                    .build();

            String[] nextLine;
            boolean isHeader = true;

            while ((nextLine = csvReader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false; // Ignore la première ligne (entête)
                    continue;
                }

                // Vérification pour éviter les erreurs avec des données incorrectes
                if (nextLine.length == 17) {
                    Arbre arbre = new Arbre(
                            nextLine[0], nextLine[1], nextLine[2], nextLine[3],
                            nextLine[4], nextLine[5], nextLine[6], nextLine[7],
                            nextLine[8], nextLine[9], nextLine[10], nextLine[11],
                            nextLine[12], nextLine[13], nextLine[14], nextLine[15], nextLine[16]
                    );
                    arbresList.add(arbre);
                } else {
                    System.err.println("Ligne incorrecte dans le fichier CSV : " + String.join(";", nextLine));
                }
            }
            tableView.setItems(arbresList);

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
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
