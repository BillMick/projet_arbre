// Statut Visite
package org.example.Models;
import java.util.Date;


public class Visite extends Activite {
    private String nomArbre;
    private String localisationArbre;

    public Visite(Activite.TypeActivite type, Date dateDePlanification, Arbre arbre, String executeur) {
        super(type, dateDePlanification, executeur);
        this.nomArbre = arbre.nom();
        this.localisationArbre = arbre.localisation();
    }

//    public enum StatutVisite {
//        PLANIFIEE,
//        EXECUTEE
//    }

    public Visite(Activite.TypeActivite type, Date dateDePlanification, Arbre arbre, int cout) {
        super(type, dateDePlanification, cout);
        this.nomArbre = arbre.nom();
        this.localisationArbre = arbre.localisation();
    }

    public String nomArbre() {
        return this.nomArbre;
    }

    public String localisationArbre() {
        return this.localisationArbre;
    }
}
