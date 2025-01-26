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
import org.example.Models.Arbre;
import org.example.java_project.Application;

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class VotesController {

    @FXML
    private TableView<Map<String, Object>> votesTable;

    @FXML
    private TableColumn<Map<String, Object>, String> nameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> locationColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> countColumn;

    private ObservableList<Map<String, Object>> votesData = FXCollections.observableArrayList();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "Storage/trees.json"; // Update with your actual JSON file path


    @FXML
    public void initialize() {
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
        countColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("count");
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
        try {
            updateTableData();
        } catch (IOException e) {
            System.err.println("Error updating table data: " + e.getMessage());
        }
        votesTable.setItems(votesData);
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateTableData() throws IOException {
        File folder = new File("Storage/votes/");
        List<Map<String, Object>> votes = new ArrayList<>();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                if (file.exists()) {
                    try {
                        List<Map<String, Object>> fileVotes = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {
                        });
                        votes.addAll(fileVotes);
                    } catch (IOException e) {
                        System.err.println("Error reading " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("No JSON files found in the folder.");
        }

        // Step 1: Group by "name", "location", and "status", and count occurrences
        Map<String, Map<String, Map<String, Long>>> groupedOccurrences = votes.stream()
                .filter(vote -> vote.containsKey("name") && vote.containsKey("location") && vote.containsKey("status"))  // Ensure all keys exist
                .map(vote -> {
                    String name = (String) vote.get("name");
                    String location = (String) vote.get("location");
                    String status = (String) vote.get("status");
                    return new AbstractMap.SimpleEntry<>(name, new AbstractMap.SimpleEntry<>(location, status));  // Group by name, location, status
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,  // First level of grouping by name
                        Collectors.groupingBy(entry -> entry.getValue().getKey(),  // Second level by location
                                Collectors.groupingBy(entry -> entry.getValue().getValue(), Collectors.counting()))  // Third level by status
                ));

        // Step 2: Flatten the grouped results
        // Now we flatten the map structure correctly
        List<Map<String, Object>> flattenedResults = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map<String, Long>>> nameEntry : groupedOccurrences.entrySet()) {
            String name = nameEntry.getKey();

            // Process each location for this name
            for (Map.Entry<String, Map<String, Long>> locationEntry : nameEntry.getValue().entrySet()) {
                String location = locationEntry.getKey();

                // Process each status for this location
                for (Map.Entry<String, Long> statusEntry : locationEntry.getValue().entrySet()) {
                    String status = statusEntry.getKey();
                    Long count = statusEntry.getValue();

                    // Add the result to the list
                    Map<String, Object> result = new HashMap<>();
                    result.put("name", name);
                    result.put("location", location);
                    result.put("status", status);
                    result.put("count", count);
                    flattenedResults.add(result);
                }
            }
        }

        // Step 3: Sort and select top 5 results based on count
        List<Map<String, Object>> top5Results = flattenedResults.stream()
                .sorted((entry1, entry2) -> Long.compare((Long) entry2.get("count"), (Long) entry1.get("count")))  // Sort by count descending
                .limit(5)  // Get the top 5
                .collect(Collectors.toList());

        System.out.println("top5Results...........");
        System.out.println(top5Results);

        // Step 4: Update the TableView with the top 5 results
        votesData.setAll(top5Results);

        //votesData.setAll(votes);
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
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("associationGestionDeTresorerie.fxml"));
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
