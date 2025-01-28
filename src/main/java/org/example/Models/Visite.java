// Statut Visite
package org.example.Models;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDate;
import java.util.Date;


public class Visite extends Activite {
    private String nomArbre;
    private String localisationArbre;

    @JsonCreator
    public Visite() {
        // This is a no-argument constructor
        super(Activite.TypeActivite.VISITE, new Date(), ""); // Set some default values
    }

    @JsonSetter("nomArbre")
    public void setNomArbre(String nomArbre) {
        this.nomArbre = nomArbre;
    }

    @JsonSetter("localisationArbre")
    public void setLocalisationArbre(String localisationArbre) {
        this.localisationArbre = localisationArbre;
    }

    public Visite(Activite.TypeActivite type, Date dateDePlanification, Arbre arbre, String executeur) {
        super(type, dateDePlanification, executeur);
        this.nomArbre = arbre.getLibelleFr();
        this.localisationArbre = arbre.getLieuAdresse();
    }

    public Visite(Activite.TypeActivite type, Date dateDePlanification, Arbre arbre, int cout) {
        super(type, dateDePlanification, cout);
        this.nomArbre = arbre.getLibelleFr();
        this.localisationArbre = arbre.getLieuAdresse();
    }

    public String getNomArbre() {
        return this.nomArbre;
    }

    public String getLocalisationArbre() {
        return this.localisationArbre;
    }
}
