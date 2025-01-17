public class Entite {
    private int solde;
    private String nom;
    
    public Entite(String nom, int solde) {
        this.nom = nom;
        this.solde = solde;
    }

    public int solde() {
        return this.solde;
    }

    public String nom() {
        return this.nom;
    }
}
