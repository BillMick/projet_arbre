package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppListeArbres {

    @FXML
    private TableView<ObservableList<String>> tableView;

    @FXML
    public void initialize() {
        if (tableView == null) {
            System.err.println("Erreur : TableView non initialisé !");
            return;
        }

        // Charger les données CSV
        List<String[]> data = readCSV("liste_arbres.csv");

        if (!data.isEmpty()) {
            // Créer dynamiquement les colonnes à partir de l'en-tête
            createColumns(data.get(0));

            // Ajouter les lignes au TableView
            populateTableView(data);
        } else {
            System.err.println("Erreur : Fichier CSV vide ou non valide.");
        }
    }

    /**
     * Lire le fichier CSV et stocker les valeurs dans une liste.
     */
    private List<String[]> readCSV(String filePath) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(";")); // Séparer les valeurs avec le délimiteur ';'
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * Créer dynamiquement les colonnes en fonction de l'en-tête (première ligne du CSV).
     */
    private void createColumns(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(headers[i]);
            column.setCellValueFactory(param ->
                    new javafx.beans.property.SimpleStringProperty(param.getValue().get(colIndex))
            );
            tableView.getColumns().add(column);
        }
    }

    /**
     * Ajouter les lignes de données au TableView.
     */
    private void populateTableView(List<String[]> data) {
        ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

        // Parcourir les données (sauter la première ligne qui est l'en-tête)
        for (int i = 1; i < data.size(); i++) {
            ObservableList<String> row = FXCollections.observableArrayList(data.get(i));
            rows.add(row);
        }

        tableView.setItems(rows);
    }
}

