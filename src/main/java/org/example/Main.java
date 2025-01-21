package org.example;

import org.example.Models.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Créer un membre
        Membre membre = new Membre("Dupont", "Jean", "jean.dupont@example.com", 100.0);

        // Afficher les données du membre initiales
        membre.afficherDonnees();

        // Créer des objets Recette, Visite et Vote pour tester
        Recette cotisation1 = new Recette(20, Recette.TypeRecette.COTISATION, membre);
        Recette cotisation2 = new Recette(100, Recette.TypeRecette.COTISATION , membre);

        Visite visite1 = new Visite("Visite d'inspection 1", "01/2025");
        Visite visite2 = new Visite("Visite d'inspection 2", "03/2025");

        Vote vote1 = new Vote("Vote sur l'adhésion");
        Vote vote2 = new Vote("Vote sur la proposition 1");

        // Ajouter des cotisations, visites, votes et afficher
        membre.ajouterCotisation(cotisation1);
        membre.ajouterCotisation(cotisation2);
        membre.ajouterVisite(visite1);
        membre.ajouterVisite(visite2);
        membre.ajouterVote(vote1);
        membre.ajouterVote(vote2);

        // Afficher les données après ajout
        membre.afficherDonnees();

        // Test de paiement de cotisation
        membre.payercotisation(50.0); // Paiement valide
        membre.payercotisation(200.0); // Paiement invalide (solde insuffisant)

        // Ajouter une proposition de classification
        membre.ajouterPropositionClassification("Proposition A");
        membre.ajouterPropositionClassification("Proposition B");
        membre.ajouterPropositionClassification("Proposition C");
        membre.ajouterPropositionClassification("Proposition D");
        membre.ajouterPropositionClassification("Proposition E");
        membre.ajouterPropositionClassification("Proposition F"); // Cette proposition ne sera pas ajoutée

        // Afficher les propositions après ajout
        membre.afficherDonnees();

        // Mise à jour du solde
        membre.ajoutersolde(50.0);

        // Quitter l'association
        membre.quitterAssociation();

        // Afficher les données après avoir quitté
        membre.afficherDonnees();
    }
}
