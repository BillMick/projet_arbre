package org.example.Models;

import java.util.Date;


public class Activite {
    private final TypeActivite type;
    private Date dateDePlanification;
    private Date dateDExecution;
    private int cout;
    // private String nomAssociation; // pas vraiment nécessaire...
    // private Object executeur; // juste email ou nom ?
    private String executeur;
    private Object compteRendu;
    private StatutActivte statut = StatutActivte.ATTENTE;

    // utilisé par Membre
    // public Activite(TypeActivite type, Date dateDePlanification, Object executeur) {
    //     this.type = type;
    //     this.dateDePlanification = dateDePlanification; // or new Date()
    //     this.executeur = executeur;

    // }

    // type activités
    public enum TypeActivite {
        VISITE
    }
    public enum StatutActivte {
        ATTENTE,
        PLANIFIEE,
        EXECUTEE
    }

    // utilisé par Membre
    public Activite(TypeActivite type, Date dateDePlanification, String executeur) {
        this.type = type;
        this.dateDePlanification = dateDePlanification; // or new Date()
        // this.cout = cout;
        this.executeur = executeur;
        this.statut = StatutActivte.PLANIFIEE;
    }

    // second constructeur (est utilisé par Association)
    public Activite(TypeActivite type, Date dateDePlanification, int cout) {
        if (cout <= 0) {
            throw new IllegalArgumentException("Le cout doit être strictement positif.");
        }
        this.type = type;
        this.dateDePlanification = dateDePlanification; // or new Date()
        this.cout = cout;
    }

    // setters
    public boolean ajouterCompteRendu(Rapport compteRendu) {
        this.compteRendu = compteRendu;
        return true;
    }

    // getters
    public StatutActivte statut() {
        return this.statut;
    }

    public TypeActivite type() {
        return this.type;
    }

    public Date dateDePlanification() {
        return this.dateDePlanification;
    }

    public Date dateDExecution() {
        return this.dateDExecution;
    }

    public String executeur() {
        return this.executeur;
    }

    public int cout() {
        return this.cout;
    }

    public Object compteRendu() {
        return this.compteRendu;
    }
}

// Activités  (Visites)
// - date => année de l’exercice
// - statut => (PLANIFIÉE, EXÉCUTÉE)
// - association => nom
// - acteur (le membre responsable de la visite) => email
// -compte rendu
