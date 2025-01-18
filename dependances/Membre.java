package dependances;
public class Membre {
    private String nom;
    private String prenom;
    private String email;

    public Membre(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String nom() { return this.nom;}
    public String prenom() { return this.prenom;}
    public String email() { return this.email;}
}

// prévoir une fonction de recharge de compte pour les membres.