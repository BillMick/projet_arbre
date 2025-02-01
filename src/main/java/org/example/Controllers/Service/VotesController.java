package org.example.Controllers.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.example.Controllers.Node.AppChosenController;
import org.example.java_project.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class VotesController {
    public static final String REPERTOIRE_DE_BASE = "Storage";
    public static final String REPERTOIRE_ASSOC = "Associations";
    public static final String REPERTOIRE_SERVICE = "Municipalite";

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

    private void updateTableData() throws IOException {
        if (REPERTOIRE_DE_BASE == null || REPERTOIRE_SERVICE == null ) {
            throw new IllegalArgumentException("Chemin inexistant.");
        }
        System.out.println(REPERTOIRE_DE_BASE + " " + REPERTOIRE_SERVICE);
        File folder = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "votes").toFile();
        List<Map<String, Object>> votes = new ArrayList<>();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                if (file.exists() && !file.getName().equals("votes.json")) {
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

        List<Map<String, Object>> flattenedResults = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map<String, Long>>> nameEntry : groupedOccurrences.entrySet()) {
            String name = nameEntry.getKey();

            for (Map.Entry<String, Map<String, Long>> locationEntry : nameEntry.getValue().entrySet()) {
                String location = locationEntry.getKey();

                for (Map.Entry<String, Long> statusEntry : locationEntry.getValue().entrySet()) {
                    String status = statusEntry.getKey();
                    Long count = statusEntry.getValue();

                    Map<String, Object> result = new HashMap<>();
                    result.put("name", name);
                    result.put("location", location);
                    result.put("status", status);
                    result.put("count", count);
                    flattenedResults.add(result);
                }
            }
        }

        List<Map<String, Object>> results = flattenedResults.stream()
                .sorted((entry1, entry2) -> Long.compare((Long) entry2.get("count"), (Long) entry1.get("count")))
                // .limit(5)
                .collect(Collectors.toList());
        votesData.setAll(results);
        Path path = Paths.get(REPERTOIRE_DE_BASE, REPERTOIRE_SERVICE, "votes");
        File dir = new File(path.toString());
        dir.mkdirs();
        File file = Paths.get(dir.toString(), "votes.json").toFile();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, results);
    }

    @FXML
    public void gestionArbre(javafx.event.ActionEvent event){
        try {
            // Load the new interface from the FXML file
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("AppListeArbres.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Interface de Gestion des Arbres");

            // Show the new stage
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Fermer(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
