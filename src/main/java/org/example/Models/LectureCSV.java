package org.example.Models;


import com.opencsv.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class LectureCSV {

    public static ObservableList<Arbre> arbresList = FXCollections.observableArrayList();
    @FXML
    public static void loadCSVData(String filePath) {
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


        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }

}