package org.example.Services;

import org.example.Models.Membre;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MembreServices {
    private static final String FICHIER_MEMBRES = "membres.json"; // Nom du fichier pour stocker les membres
    private final ObjectMapper objectMapper = new ObjectMapper(); // Gère la conversion JSON

    // Sauvegarde un membre dans le fichier spécifié
    public static void sauvegarderMembre(List<Membre> membres, String fichier) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(fichier), membres);
            System.out.println("Membres sauvegardés dans le fichier : " + fichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture des membres dans le fichier JSON.");
            e.printStackTrace();
        }
    }



    // Charge un membre depuis le fichier spécifié
    // Modifier la méthode pour charger une liste de membres
    public static List<Membre> chargerMembre(String fichier) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Lire la liste de membres à partir du fichier JSON
            List<Membre> membres = objectMapper.readValue(new File(fichier),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Membre.class));
            return membres;
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture des membres depuis le fichier JSON.");
            e.printStackTrace();
            return null;
        }
    }



    // Affiche les informations d'un membre chargé depuis un fichier
    public static void afficherTousLesMembres(String fichier) {
        List<Membre> membres = chargerMembre(fichier);
        if (!membres.isEmpty()) {
            for (Membre membre : membres) {
                membre.afficherDonnees();
            }
        } else {
            System.out.println("Aucun membre à afficher.");
        }
    }


    // Supprime le fichier contenant les membres
    public static boolean supprimerFichierMembre() {
        File fichier = new File(FICHIER_MEMBRES);
        if (fichier.exists()) {
            if (fichier.delete()) {
                System.out.println("Fichier " + FICHIER_MEMBRES + " supprimé avec succès.");
                return true;
            } else {
                System.err.println("Échec de la suppression du fichier.");
                return false;
            }
        } else {
            System.out.println("Fichier introuvable : " + FICHIER_MEMBRES);
            return false;
        }
    }
}
