public class Association {

    private String nom;
    private int solde;
    private Membre president;
    private ArrayList<Depense> = new ArrayList<Depense>;
    private ArrayList<Membre> membres = new ArrayList<Membre>;
    private ArrayList<int> annees_exercice= new ArrayList<int>;
    private ArrayList<Arbre> classification = new ArrayList<Arbre>;
    private ArrayList<Activite> activites = new ArrayList<Activite>;
    private ArrayList<Rapport> rapports = new ArrayList<Rapport>;
    private ArrayList<Recette> dons = new ArrayList<Recette>;
    private ArrayList<Recette> cotisations = new ArrayList<Recette>;

    // modifier le solde
    public int recette(int montant) {
        this.solde += montant;
        return this.solde;
        // Persistance de données
    }

    // faire une demande de don / subvention
    public void demandeDeDon(int montant, String donateur) {
        // s'assurer que montant > 0 et donateur != null
        Recette don = ... // Création de l'objet Recette
        dons.add(don); // Ajout à la liste des dons
        this.recette(montant); // Ajout au solde
        // Persistance de données
    }

    // inscrire un membre
    public void inscrire() {
        // passer en argument les infos nécessaires à l'inscription
        Membre membre = new Membre(...); // créer l'instance
        this.membres.add(membre); 
        // Persistance de données
    }

    // désinscrire un membre
    public void desinscrire(Membre membre) {
        // à la place de "membre", on pourrait donner un identificateur unique
        this.membres.remove(membre);
        // Persistance de données
    }

}