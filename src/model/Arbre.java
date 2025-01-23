package model;

public class Arbre {
    private String idBase;
    private String typeEmplacement;
    private String domanialite;
    private String arrondissement;
    private String complementAdresse;
    private String numero;
    private String lieuAdresse;
    private String idEmplacement;
    private String libelleFr;
    private String genre;
    private String espece;
    private String varieteOuCultivar;
    private String circonference;
    private String hauteur;
    private String stadeDeveloppement;
    private String remarquable;
    private String geo2D;

    // Constructeur
    public Arbre(String idBase, String typeEmplacement, String domanialite, String arrondissement,
                 String complementAdresse, String numero, String lieuAdresse, String idEmplacement,
                 String libelleFr, String genre, String espece, String varieteOuCultivar,
                 String circonference, String hauteur, String stadeDeveloppement,
                 String remarquable, String geo2D) {
        this.idBase = idBase;
        this.typeEmplacement = typeEmplacement;
        this.domanialite = domanialite;
        this.arrondissement = arrondissement;
        this.complementAdresse = complementAdresse;
        this.numero = numero;
        this.lieuAdresse = lieuAdresse;
        this.idEmplacement = idEmplacement;
        this.libelleFr = libelleFr;
        this.genre = genre;
        this.espece = espece;
        this.varieteOuCultivar = varieteOuCultivar;
        this.circonference = circonference;
        this.hauteur = hauteur;
        this.stadeDeveloppement = stadeDeveloppement;
        this.remarquable = remarquable;
        this.geo2D = geo2D;
    }

    // Getters et setters pour chaque propriété
    public String getIdBase() { return idBase; }
    public void setIdBase(String idBase) { this.idBase = idBase; }

    public String getTypeEmplacement() { return typeEmplacement; }
    public void setTypeEmplacement(String typeEmplacement) { this.typeEmplacement = typeEmplacement; }

    // Répéter les getters et setters pour tous les champs restants
    // ...
}
