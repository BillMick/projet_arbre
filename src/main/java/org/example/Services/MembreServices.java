package org.example.Services;

import org.example.Models.Membre;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class MembreServices {
    private static final String FICHIER_MEMBRES = "membres.json"; // Nom du fichier pour stocker les membres
    private final ObjectMapper objectMapper = new ObjectMapper(); // Gère la conversion JSON

    // Sauvegarde un membre dans le fichier spécifié
    public static void sauvegarderMembre(Membre membre, String fichier) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(fichier), membre);
            System.out.println("Membre sauvegardé dans le fichier : " + fichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du membre dans le fichier JSON.");
            e.printStackTrace();
        }
    }

    // Charge un membre depuis le fichier spécifié
    public static Membre chargerMembre(String fichier) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(fichier), Membre.class);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du membre depuis le fichier JSON.");
            e.printStackTrace();
            return null;
        }
    }

    // Affiche les informations d'un membre chargé depuis un fichier
    public static void afficherMembre(String fichier) {
        Membre membre = chargerMembre(fichier);
        if (membre != null) {
            membre.afficherDonnees();
        } else {
            System.out.println("Impossible de lire le membre depuis le fichier " + fichier);
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
