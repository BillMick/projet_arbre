package controller;
import model.Arbre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArbreController {
   /* private List<Arbre> arbres;

    // Charger les arbres depuis un fichier CSV
    public void chargerArbres(String cheminFichier) {
        arbres = new ArrayList<>();
        String ligne;

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            // Lire l'en-tête
            br.readLine();

            // Lire chaque ligne
            while ((ligne = br.readLine()) != null) {
                String[] colonnes = ligne.split(";"); // Sépare les colonnes par un point-virgule

                // Vérifier si la ligne a suffisamment de colonnes
                if (colonnes.length >= 10) {
                    Arbre arbre = new Arbre(
                            colonnes[0], // IDBASE
                            colonnes[1], // GENRE
                            colonnes[2], // ESPECE
                            colonnes[3], // NOM_COMMUN
                            Double.parseDouble(colonnes[4].isEmpty() ? "0" : colonnes[4]), // CIRCONFERENCE
                            Double.parseDouble(colonnes[5].isEmpty() ? "0" : colonnes[5]), // HAUTEUR
                            colonnes[6], // STADE_DEVELOPPEMENT
                            colonnes[7], // ADRESSE
                            colonnes[8], // GPS
                            Boolean.parseBoolean(colonnes[9]) // REMARQUABLE
                    );

                    arbres.add(arbre);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retourner la liste des arbres
    public List<Arbre> getArbres() {
        return arbres;
    }*/
}
