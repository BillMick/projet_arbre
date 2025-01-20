// Statut Visite

import java.util.Date;

enum StatutVisite {
    PLANIFIEE, 
    EXECUTEE
}

public class Visite extends Activite {
    private String nomArbre;
    private String localisationArbre;

    Visite(TypeActivite type, Date dateDePlanification, String executeur) {
        super(type, dateDePlanification, executeur);
    }

    Visite(TypeActivite type, Date dateDePlanification) {
        super(type, dateDePlanification);
    }

    public String nomArbre() {
        return this.nomArbre;
    }

    public String localisationArbre() {
        return this.localisationArbre;
    }
}
