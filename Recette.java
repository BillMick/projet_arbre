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
    private final Entite debiteur; // nom ou email
    private final Date date = new Date(); // à revoir pour question de test
    private StatutRecette statut = StatutRecette.NONPERCUE;
    private boolean modifie = false;

    public Recette(int montant, TypeRecette type, Entite debiteur) {
        this.montant = montant;
        this.type = type;
        this.debiteur = debiteur;
        // Persistance des données ...
    }

    public void modifierStatut(StatutRecette statut) {
        if (this.modifie == true) {
            throw new IllegalStateException("Opération non autorisée: statut non modifiable.");
        }
        this.statut = statut;
        this.modifie = true;
    }

    public void payer() {
        // ...
    }

    public Date date() {
        return this.date; // vérifier le format de retour
    }

    public StatutRecette statutRecette() {
        return this.statut;
    }

    public int montant() {
        return this.montant;
    }

    public Entite debiteur() {
        return this.debiteur;
    }

    public TypeRecette typeRecette() {
        return this.type;
    }
}
