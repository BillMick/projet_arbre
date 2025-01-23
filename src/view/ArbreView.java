/*package view;

import controller.ArbreController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Arbre;

import java.util.List;

public class ArbreView extends Application {
    private ArbreController controller;

    @Override
    public void start(Stage primaryStage) {
        // Initialiser le contrôleur
        controller = new ArbreController();

        // Charger les données depuis le fichier CSV
        controller.chargerArbres("src/les-arbres (4).csv");

        // Créer une TableView
        TableView<Arbre> tableView = new TableView<>();

        // Ajouter des colonnes
        TableColumn<Arbre, String> idCol = new TableColumn<>("IDBASE");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idBase"));

        TableColumn<Arbre, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Arbre, String> especeCol = new TableColumn<>("Espèce");
        especeCol.setCellValueFactory(new PropertyValueFactory<>("espece"));

        TableColumn<Arbre, String> nomCol = new TableColumn<>("Nom Commun");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nomCommun"));

        TableColumn<Arbre, Double> hauteurCol = new TableColumn<>("Hauteur (m)");
        hauteurCol.setCellValueFactory(new PropertyValueFactory<>("hauteur"));

        TableColumn<Arbre, Double> circonferenceCol = new TableColumn<>("Circonférence (cm)");
        circonferenceCol.setCellValueFactory(new PropertyValueFactory<>("circonference"));

        TableColumn<Arbre, Boolean> remarquableCol = new TableColumn<>("Remarquable");
        remarquableCol.setCellValueFactory(new PropertyValueFactory<>("remarquable"));

        tableView.getColumns().addAll(idCol, genreCol, especeCol, nomCol, hauteurCol, circonferenceCol, remarquableCol);

        // Charger les données dans la TableView
        List<Arbre> arbres = controller.getArbres();
        ObservableList<Arbre> data = FXCollections.observableArrayList(arbres);
        tableView.setItems(data);

        // Afficher l'interface
        VBox vbox = new VBox(tableView);
        Scene scene = new Scene(vbox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des Arbres");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/