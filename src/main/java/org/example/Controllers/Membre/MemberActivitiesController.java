package org.example.Controllers.Membre;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.Models.Activite;
import org.example.Models.Association;
import org.example.Models.Visite;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class MemberActivitiesController {

    @FXML
    private ComboBox<Visite> treeSelection;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker visitDatePicker;
    @FXML
    private TableView<Visite> visitsTable;
    @FXML
    private TableColumn<Map<String, Object>, Date> dateColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> typeColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> descriptionColumn;
    @FXML
    private Label totalReimbursementLabel;
    @FXML
    private ListView<String> reportListView; // Liste pour afficher les rapports

    private ObservableList<Visite> visitList = FXCollections.observableArrayList();
    private List<Report> reports = new ArrayList<>(); // Liste pour stocker les rapports
    private int reimbursementPerVisit = 20; // Montant fixe par visite
    private String selectedTreeType;
    private static final String VISITS_FILE_PATH = "Storage/visits.json"; // Chemin vers le fichier JSON
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // objectMapper.registerModule(new JavaTimeModule());

    @FXML
    public void initialize() throws IOException {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDePlanification"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("executeur"));

        visitsTable.setItems(visitList);

        File file = new File("Storage/activites.json");
        if (!file.exists()) {
            System.out.println("Aucune activité proposée.");
            return ;
        }
        System.out.println(new Date());
        updateComboBox();

        loadVisitsAndReports();
        updateTableData();
        visitsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void updateComboBox() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        File file = new File("Storage/activites.json");
        List<Visite> activites = objectMapper.readValue(file, new TypeReference<List<Visite>>() {});
        List<Visite> activitesEnAttente = activites.stream()
                .filter(ac -> Activite.StatutActivte.ATTENTE.equals(ac.getStatut()))
                .collect(Collectors.toList());
        System.out.println(activitesEnAttente);
        System.out.println("Activites En Attente");
        treeSelection.getItems().clear();
        treeSelection.getItems().addAll(activitesEnAttente);

        treeSelection.setConverter(new StringConverter<Visite>() {
            @Override
            public String toString(Visite visite) {
                return visite.getNomArbre() + " | " + visite.getLocalisationArbre();
            }

            @Override
            public Visite fromString(String string) {
                throw new UnsupportedOperationException("Conversion from String to Visite is not supported.");
            }
        });
    }

    private void updateTableData() throws IOException {
        File file = new File("Storage/activitesMembre.json");
        if (!file.exists()) {
            System.out.println("No trees data found.");
            return;
        }

        // Parse the JSON file into a list of maps
        List<Visite> data = objectMapper.readValue(file, new TypeReference<List<Visite>>() {});
        visitList.setAll(data);
        visitsTable.setItems(visitList);
    }

    private void loadVisitsAndReports() {
        try {
            File file = new File(VISITS_FILE_PATH);
            if (!file.exists()) {
                System.out.println("No visits file found. Starting fresh.");
                return;
            }

            VisitData data = objectMapper.readValue(file, VisitData.class);
            visitList.setAll(data.getVisits());
            reports.clear();
            reports.addAll(data.getReports());
            updateReportListView();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load visits and reports: " + e.getMessage());
        }
    }

    @FXML
    private void onTreeSelected() {
        Visite tree = treeSelection.getValue();
        if (tree != null) {
            selectedTreeType = tree.getType().toString();
            descriptionField.setText("Visite de " + tree);
            visitDatePicker.setValue(tree.getDateDePlanification().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
    }

    @FXML
    private void onAddVisit() throws IOException, InstantiationException, IllegalAccessException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Check if required fields are filled
        if (selectedTreeType == null || descriptionField.getText().isEmpty() || visitDatePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez remplir tous les champs.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Get the values from the form
        LocalDate dateL = visitDatePicker.getValue();
        Date date = Date.from(dateL.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String description = descriptionField.getText();
        System.out.println(treeSelection.getValue()); //
        Visite selectedVisit = (Visite) treeSelection.getValue();
        System.out.println(treeSelection.getValue()); //
        System.out.println("AFTER GET VISIT");

        // Read existing activities (visits) from activites.json
        File file = new File("Storage/activites.json");
        List<Visite> activities = new ArrayList<>();
        if (file.exists()) {
            try {
                activities = objectMapper.readValue(file, new TypeReference<List<Visite>>() {});
            } catch (IOException e) {
                System.err.println("Error reading activities file: " + e.getMessage());
                return;
            }
        }

        // Check if a visit is selected (edit mode)
        if (selectedVisit != null) {
            // Find the activity to update based on some unique property (e.g., the "arbre" or another identifier)
            for (Visite activity : activities) {
                if (activity.getNomArbre().equals(selectedVisit.getNomArbre())) {
                    // Update the selected visit's fields
                    activity.setExecuteur(description);  // Assuming 'description' is used for 'executeur'
                    activity.setDateDePlanification(date.getClass().newInstance());
                    activity.setStatut(Activite.StatutActivte.PLANIFIEE);
                    selectedVisit = activity;
                    break;
                }
            }

            System.out.println(selectedVisit);
            System.out.println(selectedVisit.getDateDePlanification());
            // Refresh or update the visit in your list if needed
            System.out.println(visitList.indexOf(selectedVisit));
            if (visitList.indexOf(selectedVisit) == -1) {
                System.out.println("Hereeee.");
                System.out.println(selectedVisit);
                visitList.add(selectedVisit);
            }
            else {
                visitList.set(visitList.indexOf(selectedVisit), selectedVisit);
            }
            updateVisit(selectedVisit);
            System.out.println("selectedVisit.............");
        }

        // Write the updated list back to the JSON file
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, activities);
        } catch (IOException e) {
            System.err.println("Error saving activities file: " + e.getMessage());
        }

        // Clear the form fields
        treeSelection.setValue(null);
        descriptionField.clear();
        visitDatePicker.setValue(null);
        selectedTreeType = null;
        selectedVisit = null;

        // Optionally, show a confirmation message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Visite ajoutée/Modifiée avec succès.", ButtonType.OK);
        alert.showAndWait();
        initialize();
    }

    private void updateVisit(Visite activity) throws IOException {
        File file = new File("Storage/activitesMembre.json");
        List<Visite> activities = new ArrayList<>();
        if (file.exists()) {
            try {
                activities = objectMapper.readValue(file, new TypeReference<List<Visite>>() {});
                activities.add(activity);
                System.out.println("Blabla blabla");
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, activities);
                System.out.println("MiISE à jour dans le fichier");
            } catch (IOException e) {
                System.err.println("Error reading activities file: " + e.getMessage());
                return;
            }
        }
        else {
            activities.add(activity);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, activities);
            System.out.println("MiiiiiiiiiiISE à jour dans le fichier");
        }
    }

    @FXML
    private void onCreateReport() {
        Visite selectedVisit = visitsTable.getSelectionModel().getSelectedItem();
        if (selectedVisit != null) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Créer un Compte-Rendu");
            dialog.setHeaderText("Compte-Rendu de la " + selectedVisit.getType() + " du " + Association.dateFormat.format(selectedVisit.getDateDePlanification()));

            TextArea reportTextArea = new TextArea();
            reportTextArea.setPromptText("Écrire le compte-rendu ici...");

            dialog.getDialogPane().setContent(reportTextArea);
            ButtonType submitButtonType = new ButtonType("Soumettre", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(button -> {
                if (button == submitButtonType) {
                    return reportTextArea.getText();
                }
                return null;
            });

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(reportContent -> {
                String reportTitle = "Rapport de la " + selectedVisit.getType() + " du " + Association.dateFormat.format(selectedVisit.getDateDePlanification());
                Report report = new Report(reportTitle, "\n Arbre: " + selectedVisit.getNomArbre() + "\n Arbre: " + selectedVisit.getLocalisationArbre() + reportContent);
                reports.add(report);
                updateReportListView();
                saveVisitsAndReports();
                saveReportToPDF(report);
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une visite pour créer un rapport.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void saveReportToPDF(Report report) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le Rapport");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            // Utiliser le titre du rapport pour le nom du fichier
            fileChooser.setInitialFileName(report.getTitle() + ".pdf");
            File pdfFile = fileChooser.showSaveDialog(new Stage());

            if (pdfFile != null) {
                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);

                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.newLineAtOffset(25, 700); // Position du texte
                        contentStream.showText(report.getTitle()); // Afficher le titre du rapport
                        contentStream.newLineAtOffset(0, -20); // Nouvelle ligne
                        contentStream.showText(report.getContent()); // Afficher le contenu du rapport
                        contentStream.endText();
                    }

                    document.save(pdfFile); // Enregistrer le fichier PDF
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rapport sauvegardé : " + pdfFile.getAbsolutePath(), ButtonType.OK);
                    alert.showAndWait();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la sauvegarde du rapport.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void updateReportListView() {
        List<String> reportTitles = new ArrayList<>();
        for (Report report : reports) {
            reportTitles.add(report.getTitle());
        }
        reportListView.setItems(FXCollections.observableArrayList(reportTitles));
    }

    @FXML
    private void onViewReport() {
        int selectedIndex = reportListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Report selectedReport = reports.get(selectedIndex);
            saveReportToPDF(selectedReport); // Visualiser le rapport après avoir généré un fichier PDF
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un rapport à ouvrir.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void saveVisitsAndReports() {
        try {
            VisitData data = new VisitData(visitList, reports);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(VISITS_FILE_PATH), data);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save visits and reports: " + e.getMessage());
        }
    }

    @FXML
    private void onBackButtonClick() {
        // Ferme la fenêtre actuelle ou naviguer vers une autre vue
        Stage stage = (Stage) visitsTable.getScene().getWindow();
        stage.close(); // Fermer la fenêtre
    }

//    public static class Visit {
//        private LocalDate date;
//        private String type;
//        private String description;
//
//        public Visit(LocalDate date, String type, String description) {
//            this.date = date;
//            this.type = type;
//            this.description = description;
//        }
//
//        public LocalDate getDate() {
//            return date;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//    }

    public static class Report {
        private String title;
        private String content;

        public Report(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }

    public static class VisitData {
        private List<Visite> visits;
        private List<Report> reports;

        public VisitData() {
            this.visits = new ArrayList<>();
            this.reports = new ArrayList<>();
        }

        public VisitData(List<Visite> visits, List<Report> reports) {
            this.visits = visits;
            this.reports = reports;
        }

        @JsonProperty
        public List<Visite> getVisits() {
            return visits;
        }

        public List<Report> getReports() {
            return reports;
        }
    }
}