package org.example.Models;

public class Dette {
    public enum StatutDette {
        PAYEE,
        IMPAYEE
    }

    public enum TypeDette {
        FACTURE,
        DEFRAIEMENT
    }

    private double montant;
    private TypeDette type;
    private StatutDette statut = StatutDette.IMPAYEE;
    private String crediteur;

    public Dette(double montant, TypeDette type, String crediteur) {
        this.montant = montant;
        this.type = type;
        this.crediteur = crediteur;
    }
    public Dette(double montant, TypeDette type, StatutDette statut, String crediteur) {
        this.montant = montant;
        this.type = type;
        this.statut = statut;
        this.crediteur = crediteur;
    }

    public double montant() {
        return this.montant;
    }

    public TypeDette type() {
        return this.type;
    }

    public StatutDette statut() {
        return this.statut;
    }

    public String crediteur() { return this.crediteur; }
}
