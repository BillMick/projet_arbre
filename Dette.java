// type de dette
enum TypeDette {
    FACTURE,
    DEFRAIEMENT
}

enum StatutDepense {
    PAYEE,
    IMPAYEE
}

public class Dette {
    
    private int montant;
    private TypeDette type;

    public int montant() {
        return this.montant;
    }

    public TypeDette type() {
        return this.type;
    }
}
