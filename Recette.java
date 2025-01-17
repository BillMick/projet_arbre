import java.util.Date;

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
    private final int montant;
    private final TypeRecette type;
    private final String debiteur; // nom ou email
    private final Date date = new Date(); // à revoir pour question de test
    private StatutRecette statut = StatutRecette.NONPERCUE;

    public Recette(int montant, TypeRecette type, String debiteur) {
        this.montant = montant;
        this.type = type;
        this.debiteur = debiteur;
        // Persistance des données ...
    }

    public void modifierStatut(StatutRecette statut) {
        if (statut == StatutRecette.NONPERCUE) {
            throw new IllegalStateException("Opération non autorisée: statut non modifiable.");
        }
        this.statut = statut;
    }

    public void percevoirMontant() {

    }

    public StatutRecette statutRecette() {
        return this.statut;
    }

    public int montant() {
        return this.montant;
    }

    public String debiteur() {
        return this.debiteur;
    }

    public TypeRecette typeRecette() {
        return this.type;
    }
}
