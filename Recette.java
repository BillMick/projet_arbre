// type Recette
enum TypeRecette {
    COTISATION,
    DON
}

//enum statut Recette
enum StatutRecette {
    PERCUE,
    NONPERCUE
}

public class Recette {
    private int montant;
    private TypeRecette type;
    private String donateur;
    private StatutRecette statut = StatutRecette.NONPERCUE;

    public Recette(int montant, TypeRecette type, String donateur) {
        this.montant = montant;
        this.type = type;
        this.donateur = donateur;
        // Persistance des données ...
    }

    public void modifierStatut(StatutRecette statut) {
        this.statut = statut;
    }
}
