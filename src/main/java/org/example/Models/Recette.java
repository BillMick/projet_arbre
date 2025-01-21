package org.example.Models;
import java.util.Date;


public class Recette {
    private final int montant;
    private final TypeRecette type;
    // private final Object debiteur; // nom ou email
    private final String debiteur;
    private final Date date = new Date(); // à revoir pour question de test
    private StatutRecette statut = StatutRecette.NONPERCUE;
    private boolean modifie = false;

    // public Recette(int montant, TypeRecette type, Entite debiteur) {
    //     this.montant = montant;
    //     this.type = type;
    //     this.debiteur = debiteur;
    //     // Persistance des données ...
    // }

    // type Recette
    public enum TypeRecette {
        COTISATION,
        DON
    }

    //enum statut Recette
    public enum StatutRecette {
        PERCUE,
        NONPERCUE
    }

    public Recette(int montant, TypeRecette type, String debiteur) {
        this.montant = montant;
        this.type = type;
        this.debiteur = debiteur;
        // Persistance des données ...
    }

    public void modifierStatut(StatutRecette statut) {
        if (this.modifie == true) {
            throw new IllegalStateException("Opération non autorisée: statut non modifiable.");
        }
        this.statut = statut;
        this.modifie = true;
    }

    public void payer() {
        // ...
    }

    public Date date() {
        return this.date; // vérifier le format de retour
    }

    public StatutRecette statutRecette() {
        return this.statut;
    }

    public int montant() {
        return this.montant;
    }

    public String debiteur() {
        return this.debiteur;
    }

    public TypeRecette typeRecette() {
        return this.type;
    }
}
