package org.example.Models;
public class Entite {
    protected double solde;
    protected String nom;
    protected String email;
    
    public Entite(String nom, String email, double solde) {
        this.nom = nom;
        this.email = email;
        this.solde = solde;
    }

    // second constructeur
    public Entite(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public double solde() {
        return this.solde;
    }

    public String nom() {
        return this.nom;
    }

    public String email() {
        return this.email;
    }

    // Maybe prévoir une toString() ...
}
