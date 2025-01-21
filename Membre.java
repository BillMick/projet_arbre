package org.example.Models;

import java.util.ArrayList;
import java.util.List;

import static org.example.Models.Recette.TypeRecette.COTISATION;

public class Membre {
    private String nom;
    private String prenom;
    private String email;
    private double solde;
    private List<Recette> cotisations;
    private List<Visite> visites;
    private List<Vote> votes;
    private List<String> propositionsClassification;

    public Membre(String nom,String prenom, String email, double solde){
        this.nom=nom;
        this.prenom=prenom;
        this.email=email;
        this.solde=solde;
        this.cotisations = new ArrayList<>();
        this.visites = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.propositionsClassification = new ArrayList<>();
    }

    public Membre() {
        // Vous pouvez initialiser des valeurs par défaut si nécessaire
        this.nom = "";
        this.prenom = "";
        this.email = "";
        this.solde = 0.0;
        this.cotisations = new ArrayList<>();  // Ajouté ici
        this.visites = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.propositionsClassification = new ArrayList<>();
    }

    // Méthode pour afficher des données du membre
    public void afficherDonnees() {
        System.out.println("\nInformations du membre :");
        System.out.println("Nom: " + nom);
        System.out.println("Prénom: " + prenom);
        System.out.println("Email: " + email);
        System.out.printf("Solde: %.2f €%n", solde);
        afficherCotisations();
        afficherVisites();
        afficherVotes();
        System.out.println("\nPropositions de classification : " + propositionsClassification);
    }

    private void afficherCotisations() {
        System.out.println("\nStatut des cotisations :");
        for (Recette cotisation : cotisations) {
            if (cotisation.typeRecette() == COTISATION) {
                System.out.println("- " + cotisation);
            }
        }
    }

    private void afficherVisites() {
        System.out.println("\nVisites :");
        for (Visite visite : visites) {
            System.out.println("- " + visite); // Supposant que Visite a une méthode toString() correcte
        }
    }

    private void afficherVotes() {
        System.out.println("\nVotes :");
        for (Vote vote : votes) {
            System.out.println("- " + vote); // Supposant que Vote a une méthode toString() correcte
        }
    }

    public void ajouterCotisation(Recette cotisation) {
        if (cotisation != null && cotisation.typeRecette() == COTISATION) {
            cotisations.add(cotisation);
        } else {
            System.out.println("Cotisation invalide.");
        }
    }

    public void ajouterVisite(Visite visite) {
        if (visite != null) {
            this.visites.add(visite);
        } else {
            System.out.println("Visite invalide.");
        }
    }

    public void ajouterVote(Vote vote) {
        if (vote != null) {
            this.votes.add(vote);
        } else {
            System.out.println("Vote invalide.");
        }
    }

    public boolean payercotisation(double montant){
        if (montant > 0 && this.solde >= montant) {
            setSolde(-montant);
            // this.solde -= montant;
            System.out.printf("Cotisation de %.2f € payée avec succès.%n", montant);
            return true;
        } else {
            System.out.println("votre Fond est insuffisant");
            return false;
        }
    }

    public void ajoutersolde(double montant){
        if(montant > 0){
            // this.solde +=montant;
            setSolde(montant);
            System.out.printf("Solde mis à jour : %.2f € ajouté.%n", montant);
        }else {
            System.out.println(" ");
        }
    }
    public void notifier(String message) {
        System.out.println("Notification pour " + nom + " : " + message);
    }
    public void quitterAssociation() {
        this.cotisations.clear();
        this.visites.clear();
        this.votes.clear();
        this.propositionsClassification.clear();
        System.out.println(nom + " a quitté l'association.");
    }
    public void ajouterPropositionClassification(String proposition) {
        if (getPropositionsClassification().size() < 5) {
            getPropositionsClassification().add(proposition);
            System.out.println("Proposition de classification ajoutée pour " + getNom() + ": " + proposition);
        } else {
            System.out.println("Le membre a déjà 5 propositions de classification.");
        }
    }


    // Getter et Setter
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if(nom!=null && !nom.trim().isEmpty()){
            this.nom = nom;
        }else {
            System.out.println("le nom est invalide");
        }
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        if (prenom != null && !prenom.trim().isEmpty()) {
            this.prenom = prenom;
        } else {
            System.out.println("Prénom invalide.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")){
            this.email = email;
        }else {
            System.out.println("email invalide");
        }
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double montant) {
        this.solde += montant;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public List<Recette> getCotisations() {
        return cotisations;
    }

    public void setCotisations(List<Recette> cotisations) {
        this.cotisations.clear();
        for (Recette cotisation : cotisations) {
            if (cotisation.typeRecette() == COTISATION) {
                this.cotisations.add(cotisation);
            }

        }   }

    public List<Visite> getVisites() {
        return visites;
    }

    public void setVisites(List<Visite> visites) {
        this.visites = visites;
    }

    public List<String> getPropositionsClassification() {
        return propositionsClassification;
    }

    public void setPropositionsClassification(List<String> propositionsClassification) {
        this.propositionsClassification = propositionsClassification;
    }
}

