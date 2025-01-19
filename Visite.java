// Statut Visite
enum StatutVisite {
    PLANIFIEE, 
    EXECUTEE
}

public class Visite extends Activite {
    Visite(TypeActivite type) {
        super(type);
    }
}
