// type activités
enum TypeActivite {
    VISITE
}

public class Activite {
    private int a;
    private final TypeActivite type;
    private Date date;
    // private String nomAssociation; // pas vraiment nécessaire...
    private Membre executeur; // juste email ou nom ?
    private Object compteRendu;
    

    public TypeActivite type() {
        return this.type;
    }
}

Activités  (Visites)
- date => année de l’exercice
- statut => (PLANIFIÉE, EXÉCUTÉE)
- association => nom
- acteur (le membre responsable de la visite) => email
-compte rendu
