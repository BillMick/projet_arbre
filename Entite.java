public class Entite {
    protected int solde;
    protected String nom;
    protected String email;
    
    public Entite(String nom, String email, int solde) {
        this.nom = nom;
        this.email = email;
        this.solde = solde;
    }

    // second constructeur
    public Entite(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    public int solde() {
        return this.solde;
    }

    public String nom() {
        return this.nom;
    }

    public String email() {
        return this.email;
    }

    // Maybe prévoir une toString() ...
}
