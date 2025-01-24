package org.example.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class AssociationServices {
    public static final String REPERTOIRE_DE_BASE = "Associations";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Helper method to write a Java object as JSON to a file
    private static void writeJsonToFile(File file, Object data) throws IOException {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }

    // Method to set up the association directory and save base infos
    public static void setupAssociation(Association association) throws IOException {
        if (association.email() == null || association.email().isBlank()) {
            throw new IllegalArgumentException("Infos invalides.");
        }

        // créer le répertoire de stockage
        Path path = Paths.get(REPERTOIRE_DE_BASE, association.email(), Association.dateFormat.format(association.debutAnneeExercice()));
        File dir = new File(path.toString());
        if (!dir.exists()) dir.mkdir();
        writeJsonToFile(new File(dir, "solde.json"), Map.of("solde", association.solde()));

        // enregistrer les infos dites de session
        path = Paths.get(REPERTOIRE_DE_BASE, association.email());
        dir = new File(path.toString());
        writeJsonToFile(new File(dir, "infos.json"), Map.of("nom", association.nom(), "email", association.email()));
        // writeJsonToFile(new File(yearDir, "periode.json"), Map.of("debut", "", "fin", ""));
    }

    // Method to update association balance
    public static void updateBalance(Association association) throws IOException {
        // créer le répertoire de stockage
        File dir = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), Association.dateFormat.format(association.debutAnneeExercice())).toString());
        if (!dir.exists()) dir.mkdir();
        writeJsonToFile(new File(dir, "solde.json"), Map.of("solde", association.solde()));
    }

    // Method to add donor
    public static void createDonor(Association association) throws IOException {
        File dir = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "Donateurs", "donateurs.json").toString());
        if (!dir.exists()) dir.mkdir();
        // Check if the file exists
        if (dir.exists()) {
            // Read the current content of the file (existing donors)
            String content = new String(Files.readAllBytes(Paths.get(dir.getPath())));
            Map<String, Donateur> currentDonors = objectMapper.readValue(content, Map.class);
            for (Donateur d: association.donateurs()) {
                if (!currentDonors.containsKey(d.email())) {
                    currentDonors.put(d.email(), d);
                }
            }
            // Write the updated data back to the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dir, currentDonors);
        } else {
            // If file doesn't exist, create it and add the new donor
            Map<String, Donateur> newDonorsMap = Map.of();
            for (Donateur d: association.donateurs()) {
                newDonorsMap.put(d.email(), d);
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dir, newDonorsMap);
        }
    }

    // Read Donors
    public static Map<String, Donateur> readDonors(Association association) throws IOException {
        File dir = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "Donateurs", "donateurs.json").toString());
        if (!dir.exists()) return Map.of();
        String content = new String(Files.readAllBytes(Paths.get(dir.getPath())));
        if (content.isBlank()) {
            return Map.of();
        }
        return objectMapper.readValue(content, Map.class);
    }

    // Method to delete a donor from the file
    public static void deleteDonor(Association association, Donateur donateur) throws IOException {
        File file = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "Donateurs", "donateurs.json").toString());
        // Check if the file exists
        if (file.exists()) {
            // Read the current content of the file (existing donors)
            String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
            Map<String, Donateur> currentDonors = objectMapper.readValue(content, Map.class);

            // Check if the donor exists
            if (!currentDonors.containsKey(donateur.email())) {
                System.out.println("Donor with this email does not exist!");
                return;
            }
            // Remove the donor by email
            currentDonors.remove(donateur.email());
            // Write the updated data back to the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, currentDonors);

            System.out.println("Donor with email " + donateur.email() + " has been deleted.");
        } else {
            System.out.println("File does not exist!");
        }
    }

    // Method to add member
    public static void createMember(Association association) throws IOException {
        for (Membre m: association.membres()) {
            File dir = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "Membres", m.getEmail() + ".json").toString());
            if (!dir.exists()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(dir, m);
            }
        }
    }

    // Read Members
    public static Map<String, Membre> readMembers(Association association) throws IOException {
        File directory = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "Membres").toString());
        if (!directory.exists()) {
            System.out.println("The specified path is not a valid directory.");
            return Map.of();
        }

        File[] jsonFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("No JSON files found in the directory.");
            return Map.of();
        }
        Map<String, Membre> jsonData = Map.of();

        for (File jsonFile : jsonFiles) {
            System.out.println("Processing file: " + jsonFile.getName());
            try {
                jsonData = objectMapper.readValue(jsonFile, Map.class);
                System.out.println("Contents of " + jsonFile.getName() + ":");
                System.out.println(jsonData);
            } catch (IOException e) {
                System.err.println("Error reading file " + jsonFile.getName() + ": " + e.getMessage());
            }
        }
        return jsonData;
    }

    // Method to delete a member from the folder
    public static void deleteMember(Association association, Membre membre) throws IOException {
        File file = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "Membres", membre.getEmail()).toString());
        // Check if the file exists
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("The file does not exist.");
        }
    }

    // update periodes
    public static void updateYear(Association association) throws IOException {
        // créer le répertoire de stockage
        File dir = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "periodes.json").toString());
        if (!dir.exists()) dir.mkdir();
        // Read the current content of the file (existing donors)
        String content = new String(Files.readAllBytes(Paths.get(dir.getPath())));
        Map<Integer, List<String>> periodes = objectMapper.readValue(content, Map.class);
        List<String> periodeValues = new ArrayList<>();
        if (association.debutAnneeExercice() == null) {
            periodeValues.add("not set");
        }
        else {
            periodeValues.add(Association.dateFormat.format(association.debutAnneeExercice()));
        }
        if (association.finAnneeExercice() == null) {
            periodeValues.add("not set");
        }
        else {
            periodeValues.add(Association.dateFormat.format(association.finAnneeExercice()));
        }
        periodes.put(periodes.size() + 1, periodeValues);
        // Write the updated data back to the file
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(dir, periodes);
    }

    // Read current periodes ?????????????
    public static Map<String, Object> readPeriodes(Association association) throws IOException {
        File dir = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "periodes.json").toString());
        if (!dir.exists()) dir.mkdir();
        String content = new String(Files.readAllBytes(Paths.get(dir.getPath())));
        Map<String, Object> currentPeriode = objectMapper.readValue(content, Map.class);
        return currentPeriode;
    }

    // Read notifications
    public static Map<String, Notification> readNotifications(Association association) throws IOException {
        File dir = new File(Paths.get(REPERTOIRE_DE_BASE, association.email(), "Notifications", "notifications.json").toString());
        if (!dir.exists()) return Map.of();
        String content = new String(Files.readAllBytes(Paths.get(dir.getPath())));
        if (content.isBlank()) {
            return Map.of();
        }
        return objectMapper.readValue(content, Map.class);
    }

    // Read Trees
    public static Map<String, Arbre> readTrees() throws IOException {
        File directory = new File(Paths.get(REPERTOIRE_DE_BASE, "municipalite").toString());
        if (!directory.exists()) {
            System.out.println("The specified path is not a valid directory.");
            return Map.of();
        }

        File[] jsonFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("No JSON files found in the directory.");
            return Map.of();
        }
        Map<String, Arbre> jsonData = Map.of();

        for (File jsonFile : jsonFiles) {
            System.out.println("Processing file: " + jsonFile.getName());
            try {
                jsonData = objectMapper.readValue(jsonFile, Map.class);
                System.out.println("Contents of " + jsonFile.getName() + ":");
                System.out.println(jsonData);
            } catch (IOException e) {
                System.err.println("Error reading file " + jsonFile.getName() + ": " + e.getMessage());
            }
        }
        return jsonData;
    }
}
