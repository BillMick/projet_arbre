import java.util.Date;
// type activités
enum TypeActivite {
    VISITE
}
enum StatutActivte {
    ATTENTE,
    PLANIFIEE, 
    EXECUTEE
}

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

    // utilisé par Membre
    public Activite(TypeActivite type, Date dateDePlanification, String executeur) {
        this.type = type;
        this.dateDePlanification = dateDePlanification; // or new Date()
        this.executeur = executeur;
        this.statut = StatutActivte.PLANIFIEE;
    }

    // second constructeur (est utilisé par Association)
    public Activite(TypeActivite type, Date dateDePlanification) {
        this.type = type;
        this.dateDePlanification = dateDePlanification; // or new Date()
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
