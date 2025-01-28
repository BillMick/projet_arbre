package org.example.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// import java.time.LocalDate;
import java.util.Date;


public class Activite {
    private TypeActivite type;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateDePlanification;
    //private Date dateDExecution;
    private double cout;
    // private String nomAssociation; // pas vraiment nécessaire...
    // private Object executeur; // juste email ou nom ?
    private String executeur;
    private Object rapport;
    private StatutActivte statut = StatutActivte.ATTENTE;
    private String description = "";

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
    public boolean ajouterCompteRendu(Rapport rapport) {
        this.rapport = rapport;
        return true;
    }

    @JsonSetter("executeur")
    public void setExecuteur(String executeur) {
        this.executeur = executeur;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonSetter("rapport")
    public void setRapport(Object rapport) {
        this.rapport = rapport;
    }

    @JsonSetter("dateDePlanification")  // Jackson will now use this setter
    public void setDateDePlanification(Date dateDePlanification) {
        this.dateDePlanification = dateDePlanification;
    }

    @JsonSetter("statut")
    public void setStatut(Activite.StatutActivte statut) {
        this.statut = statut;
    }

    @JsonSetter("cout")
    public void setCout(int cout) {
        this.cout = cout;
    }

    @JsonSetter("type")
    public void setType(TypeActivite type) {
        this.type = type;
    }

    // getters
    public StatutActivte getStatut() {
        return this.statut;
    }

    public TypeActivite getType() {
        return this.type;
    }

    public Date getDateDePlanification() {
        return this.dateDePlanification;
    }

    public String getExecuteur() {
        return this.executeur;
    }

    public String getDescription() { return this.description; }

    public double getCout() {
        return this.cout;
    }

    public Object getRapport() {
        return this.rapport;
    }
}

// Activités  (Visites)
// - date => année de l’exercice
// - statut => (PLANIFIÉE, EXÉCUTÉE)
// - association => nom
// - acteur (le membre responsable de la visite) => email
// -compte rendu
