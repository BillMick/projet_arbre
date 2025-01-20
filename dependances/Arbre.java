public class Arbre {
    private String nom;
    private String localisation;

    public Arbre(String nom, String localisation) {
        this.nom = nom;
        this.localisation = localisation;
    }

    public String nom() {
        return this.nom;
    }

    public String localisation() {
        return this.localisation;
    }
}