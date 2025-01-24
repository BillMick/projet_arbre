package org.example.Controllers.Membre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Models.Arbre;

import java.io.IOException;
import java.util.Map;

public class MemberDashboardController {

    @FXML
    private TableView<Arbre> treesTable;

    @FXML
    private TableColumn<Arbre, String> nameColumn;

    @FXML
    private TableColumn<Arbre, String> locationColumn;

    @FXML
    private TableColumn<Arbre, String> statusColumn;

    private ObservableList<Arbre> treesData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind columns to Arbre properties
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data into the TableView
        try {
            updateTableData();
        } catch (IOException e) {
            System.err.println("Error updating table data: " + e.getMessage());
        }
    }

    private void updateTableData() throws IOException {
        // Clear existing data
        treesData.clear();

        // Load new data from the `readTrees` function
        Map<String, Arbre> treesMap = readTrees();

        // Add all Arbre objects to the ObservableList
        treesData.addAll(treesMap.values());

        // Set the TableView's items
        treesTable.setItems(treesData);
    }

    // Existing readTrees() method
    public static Map<String, Arbre> readTrees() throws IOException {
        // Your implementation of readTrees() function here
        return Map.of(); // Replace with actual logic
    }
}
