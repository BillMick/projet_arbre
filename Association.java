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
    private ArrayList<Dette> dettes = new ArrayList<Dette>;

    // modifier le solde
    public int recette(int montant) {
        this.solde += montant;
        return this.solde;
        // Persistance de données
    }

    // faire une demande de don / subvention
    public boolean demandeDeDon(int montant, String donateur) {
        // s'assurer que montant > 0 et donateur != null
        Recette don = ... // Création de l'objet Recette
        dons.add(don); // Ajout à la liste des dons
        this.recette(montant); // Ajout au solde
        // Persistance de données
        return true;
    }

    // inscrire un membre
    public boolean inscrire() {
        // passer en argument les infos nécessaires à l'inscription
        Membre membre = new Membre(...); // créer l'instance
        this.membres.add(membre); 
        // Persistance de données
        return true;
    }

    // désinscrire un membre
    public boolean desinscrire(Membre membre) {
        // à la place de "membre", on pourrait donner un identificateur unique
        this.membres.remove(membre);
        // Persistance de données
        return true;
    }

    // s'abonner aux notifications du service des espaces verts
    public boolean sAbonner() {
        // Persistance de données
        ajouterAbonne(this.nom); // ajout à la liste des abonnés (fonction de la classe unique ServiceDesEspacesVerts)
        return true;
    }

    // se désabonner
    public boolean SeDesabonner() {
        // Persistance de données
        retirerAbonne(this.nom);
        return true;
    }

    // type de dette
    enum typeDette{
        FACTURE,
        DEFRAIEMENT
    }
    
    // payer ses dettes
    public boolean payerDette (Dette dette) {
        recette(-dette.montant); // retirer le montant, de la dette, du solde
        if (dette.type = typeDette.DEFRAIEMENT) {
            // rembourser aussi le membre
        }
        return true;
        // Persistance de données
    }

}