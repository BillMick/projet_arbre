package org.example.Models;

enum TypeDonateur {
    MUNICIPALITE, 
    ENTREPRISE, 
    ASSOCIATION, 
    INDIVIDU,
    AUTRE
}
public class Donateur extends Entite {
    private TypeDonateur type;

    public Donateur(String nom, String email, TypeDonateur type) {
        super(nom, email);
        if (type == null) {
            throw new IllegalArgumentException("Le type du donateur ne peut pas être nul.");
        }
        this.type = type;
    }

    public TypeDonateur type() {
        return this.type;
    }

}
        