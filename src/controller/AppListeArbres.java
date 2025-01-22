package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AppListeArbres {

    @FXML
    private TableView<DataRow> tableView;

    @FXML
    private TableColumn<DataRow, String> IDBASE;
    @FXML
    private TableColumn<DataRow, String> TYPEEMPLACEMENT;
    @FXML
    private TableColumn<DataRow, String> DOMANIALITE;
    @FXML
    private TableColumn<DataRow, String> ARRODISSEMENT;
    @FXML
    private TableColumn<DataRow, String> COMPLEMENTADRESSE;
    @FXML
    private TableColumn<DataRow, String> NUMERO;
    @FXML
    private TableColumn<DataRow, String> LIEUADRESSE;
    @FXML
    private TableColumn<DataRow, String> IDEMPLACEMENT;
    @FXML
    private TableColumn<DataRow, String> LIBELLEFR;
    @FXML
    private TableColumn<DataRow, String> GENRE;
    @FXML
    private TableColumn<DataRow, String> ESPECE;
    @FXML
    private TableColumn<DataRow, String> VARIETE;
    @FXML
    private TableColumn<DataRow, String> CIRCONFERENCE;
    @FXML
    private TableColumn<DataRow, String> HAUTEUR;
    @FXML
    private TableColumn<DataRow, String> STADEDEVELOPPEMENT;
    @FXML
    private TableColumn<DataRow, String> REMARQUABLE;
    @FXML
    private TableColumn<DataRow, String> GEO_2D;

    @FXML
    public void initialize() {
        // Lier les colonnes du tableau aux propriétés de DataRow
        IDBASE.setCellValueFactory(new PropertyValueFactory<>("IDBASE"));
        TYPEEMPLACEMENT.setCellValueFactory(new PropertyValueFactory<>("TYPEEMPLACEMENT"));
        DOMANIALITE.setCellValueFactory(new PropertyValueFactory<>("DOMANIALITE"));
        ARRODISSEMENT.setCellValueFactory(new PropertyValueFactory<>("ARRODISSEMENT"));
        COMPLEMENTADRESSE.setCellValueFactory(new PropertyValueFactory<>("COMPLEMENTADRESSE"));
        NUMERO.setCellValueFactory(new PropertyValueFactory<>("NUMERO"));
        LIEUADRESSE.setCellValueFactory(new PropertyValueFactory<>("LIEUADRESSE"));
        IDEMPLACEMENT.setCellValueFactory(new PropertyValueFactory<>("IDEMPLACEMENT"));
        LIBELLEFR.setCellValueFactory(new PropertyValueFactory<>("LIBELLEFR"));
        GENRE.setCellValueFactory(new PropertyValueFactory<>("GENRE"));
        ESPECE.setCellValueFactory(new PropertyValueFactory<>("ESPECE"));
        VARIETE.setCellValueFactory(new PropertyValueFactory<>("VARIETE"));
        CIRCONFERENCE.setCellValueFactory(new PropertyValueFactory<>("CIRCONFERENCE"));
        HAUTEUR.setCellValueFactory(new PropertyValueFactory<>("HAUTEUR"));
        STADEDEVELOPPEMENT.setCellValueFactory(new PropertyValueFactory<>("STADEDEVELOPPEMENT"));
        REMARQUABLE.setCellValueFactory(new PropertyValueFactory<>("REMARQUABLE"));
        GEO_2D.setCellValueFactory(new PropertyValueFactory<>("GEO_2D"));
    }

    @FXML
    public void listeArbres() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadCsvFile(file);
        }
    }

    private void loadCsvFile(File file) {
        ObservableList<DataRow> data = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                // Ignorer la première ligne (en-têtes)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                // Séparer les colonnes avec le délimiteur ";"
                String[] columns = line.split(";");
                if (columns.length >= 17) {
                    data.add(new DataRow(columns));
                }
            }
            tableView.setItems(data);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors du chargement du fichier : " + e.getMessage());
            alert.show();
        }
    }

    public static class DataRow {
        private final String IDBASE;
        private final String TYPEEMPLACEMENT;
        private final String DOMANIALITE;
        private final String ARRODISSEMENT;
        private final String COMPLEMENTADRESSE;
        private final String NUMERO;
        private final String LIEUADRESSE;
        private final String IDEMPLACEMENT;
        private final String LIBELLEFR;
        private final String GENRE;
        private final String ESPECE;
        private final String VARIETE;
        private final String CIRCONFERENCE;
        private final String HAUTEUR;
        private final String STADEDEVELOPPEMENT;
        private final String REMARQUABLE;
        private final String GEO_2D;

        public DataRow(String[] columns) {
            this.IDBASE = columns[0];
            this.TYPEEMPLACEMENT = columns[1];
            this.DOMANIALITE = columns[2];
            this.ARRODISSEMENT = columns[3];
            this.COMPLEMENTADRESSE = columns[4];
            this.NUMERO = columns[5];
            this.LIEUADRESSE = columns[6];
            this.IDEMPLACEMENT = columns[7];
            this.LIBELLEFR = columns[8];
            this.GENRE = columns[9];
            this.ESPECE = columns[10];
            this.VARIETE = columns[11];
            this.CIRCONFERENCE = columns[12];
            this.HAUTEUR = columns[13];
            this.STADEDEVELOPPEMENT = columns[14];
            this.REMARQUABLE = columns[15];
            this.GEO_2D = columns[16];
        }

        // Getters pour toutes les colonnes
        public String getIDBASE() { return IDBASE; }
        public String getTYPEEMPLACEMENT() { return TYPEEMPLACEMENT; }
        public String getDOMANIALITE() { return DOMANIALITE; }
        public String getARRODISSEMENT() { return ARRODISSEMENT; }
        public String getCOMPLEMENTADRESSE() { return COMPLEMENTADRESSE; }
        public String getNUMERO() { return NUMERO; }
        public String getLIEUADRESSE() { return LIEUADRESSE; }
        public String getIDEMPLACEMENT() { return IDEMPLACEMENT; }
        public String getLIBELLEFR() { return LIBELLEFR; }
        public String getGENRE() { return GENRE; }
        public String getESPECE() { return ESPECE; }
        public String getVARIETE() { return VARIETE; }
        public String getCIRCONFERENCE() { return CIRCONFERENCE; }
        public String getHAUTEUR() { return HAUTEUR; }
        public String getSTADEDEVELOPPEMENT() { return STADEDEVELOPPEMENT; }
        public String getREMARQUABLE() { return REMARQUABLE; }
        public String getGEO_2D() { return GEO_2D; }
    }
}
