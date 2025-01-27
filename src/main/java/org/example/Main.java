package org.example;

import org.example.Models.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

// import java.util.ArrayList;
// import java.util.List;

public class Main {
    public static void main(String[] args) {
//        // Faire une vérification du stockage pour savoir si c'est la première exécution ou non.
//        // si ce n'est pas la première, lancer l'interface de login
//        // si c'est la première, faire comme suit:
//        Association a1 = new Association("Assoc1", "assoc1@g.m", 100);
//        System.out.println("Solde après création:" + a1.solde());
//
//        // inscrire des membres
//        a1.inscrire("Thomas", "Anderson", "thomas@a.n");
//        a1.inscrire("Mouse", "jerry", "jerry@j.m");
//        a1.inscrire("Cat", "Tom", "tom@t.c");
//        System.out.println("Membres:" + a1.membres());
//        for (Membre m: a1.membres()) {
//            m.afficherDonnees();
//        }
//
//        // définir montant de cotisation
//        a1.modifierCotisation(50);
//
//        // lancer le début de l'année d'exercice budgétaire
//        a1.lancerAnnee(); // mentionner l'année en argument ???
//        System.out.println("Année en cours:" + Association.dateFormat.format(a1.debutAnneeExercice()));
//        System.out.println("Cotisations attendues:" + a1.cotisations());
//
//
//        // Créer un membre
//        // Membre membre = new Membre("Dupont", "Jean", "jean.dupont@example.com");
//
//        // Afficher les données du membre initiales
//        // membre.afficherDonnees();
//
//        // Créer des objets Recette, Visite et Vote pour tester
//        // Recette cotisation1 = new Recette(20, Recette.TypeRecette.COTISATION, membre.getEmail());
//        // Recette cotisation2 = new Recette(100, Recette.TypeRecette.COTISATION, membre.getEmail());
//
//        // créer quelques arbres
//        Arbre ar1 = new Arbre("Arbre1", "Ile-de-France");
//        Arbre ar2 = new Arbre("Arbre2", "Polynésie");
//        Arbre ar3 = new Arbre("Arbre3", "Essonne");
//        Arbre ar4 = new Arbre("Arbre4", "Aude");
//        Arbre ar5 = new Arbre("Arbre5", "Orsay");
//        Arbre ar6 = new Arbre("Arbre6", "Antony");
//
//        Visite visite1 = new Visite(Activite.TypeActivite.VISITE, new LocalDate(), ar1, "a@b.c");
//        Visite visite2 = new Visite(Activite.TypeActivite.VISITE, new LocalDate(), ar2, "b@a.c");
//
//        Vote vote1 = new Vote("Vote sur l'adhésion");
//        Vote vote2 = new Vote("Vote sur la proposition 1");
//
//        // Ajouter des cotisations, visites, votes et afficher
//        // membre.ajouterCotisation(cotisation1);
//        // membre.ajouterCotisation(cotisation2);
//        // membre.ajouterVisite(visite1);
//        // membre.ajouterVisite(visite2);
//        // membre.ajouterVote(vote1);
//        // membre.ajouterVote(vote2);
//
//        // Afficher les données après ajout
//        // membre.afficherDonnees();
//
//        // Test de paiement de cotisation
//        // membre.payercotisation(50.0); // Paiement valide
//        // membre.payercotisation(200.0); // Paiement invalide (solde insuffisant)
//
//        // Ajouter une proposition de classification
//        // membre.ajouterPropositionClassification(ar1);
//        // membre.ajouterPropositionClassification(ar2);
//        // membre.ajouterPropositionClassification(ar3);
//        // membre.ajouterPropositionClassification(ar4);
//        // membre.ajouterPropositionClassification(ar5);
//        // membre.ajouterPropositionClassification(ar6); // Cette proposition ne sera pas ajoutée
//        for (Membre m: a1.membres()) {
//            m.ajouterPropositionClassification(ar1);
//            break;
//        }
//        for (Membre m: a1.membres()) {
//            m.ajouterPropositionClassification(ar2);
//            break;
//        }
//        for (Membre m: a1.membres()) {
//            m.ajouterPropositionClassification(ar3);
//            break;
//        }
//        for (Membre m: a1.membres()) {
//            m.ajouterPropositionClassification(ar4);
//            break;
//        }
//        for (Membre m: a1.membres()) {
//            m.ajouterPropositionClassification(ar5);
//            break;
//        }
//        for (Membre m: a1.membres()) {
//            m.ajouterPropositionClassification(ar6);
//            break;
//        }
//
//        // Afficher les propositions après ajout
//        // membre.afficherDonnees();
//        for (Membre m: a1.membres()) {
//            m.afficherDonnees();
//            m.ajoutersolde(50);
//        }
//
//        // Mise à jour du solde
//        // membre.ajoutersolde(50.0);
//
//        // Quitter l'association
//        // membre.quitterAssociation();
//
//        // Afficher les données après avoir quitté
//        // membre.afficherDonnees();
    }
}
