package org.example.Models;

// type de dette
enum TypeDette {
    FACTURE,
    DEFRAIEMENT
}

enum StatutDette {
    PAYEE,
    IMPAYEE
}

public class Dette {
    
    private int montant;
    private TypeDette type;
    private StatutDette statut;

    public int montant() {
        return this.montant;
    }

    public TypeDette type() {
        return this.type;
    }

    public StatutDette statut() {
        return this.statut;
    }
}
