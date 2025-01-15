public class Association {

    private String nom;
    private int budget;
    private Membre president;
    private ArrayList<Depense> = new ArrayList<Depense>;
    private ArrayList<Membre> membres = new ArrayList<Membre>;
    private ArrayList<int> annees_exercice= new ArrayList<int>;
    private ArrayList<Arbre> classification = new ArrayList<Arbre>;
    private ArrayList<Activite> activites = new ArrayList<Activite>;
    private ArrayList<Rapport> rapports = new ArrayList<Rapport>;
    private ArrayList<Recette> dons = new ArrayList<Recette>;
    private ArrayList<Recette> cotisations = new ArrayList<Recette>;

    public boolean demandeDeDon(int montant, String donateur) {
        Recette don = ... // Création de l'objet Recette
        dons.add(don); // Ajout à la liste des dons
        this.recette(montant); // Ajout au budget
        // Persistance de données
    }

}