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

    // Getters et setters
    public String getIdBase() { return idBase; }
    public void setIdBase(String idBase) { this.idBase = idBase; }

    public String getTypeEmplacement() { return typeEmplacement; }
    public void setTypeEmplacement(String typeEmplacement) { this.typeEmplacement = typeEmplacement; }

    public String getDomanialite() { return domanialite; }
    public void setDomanialite(String domanialite) { this.domanialite = domanialite; }

    public String getArrondissement() { return arrondissement; }
    public void setArrondissement(String arrondissement) { this.arrondissement = arrondissement; }

    public String getComplementAdresse() { return complementAdresse; }
    public void setComplementAdresse(String complementAdresse) { this.complementAdresse = complementAdresse; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getLieuAdresse() { return lieuAdresse; }
    public void setLieuAdresse(String lieuAdresse) { this.lieuAdresse = lieuAdresse; }

    public String getIdEmplacement() { return idEmplacement; }
    public void setIdEmplacement(String idEmplacement) { this.idEmplacement = idEmplacement; }

    public String getLibelleFr() { return libelleFr; }
    public void setLibelleFr(String libelleFr) { this.libelleFr = libelleFr; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getEspece() { return espece; }
    public void setEspece(String espece) { this.espece = espece; }

    public String getVarieteOuCultivar() { return varieteOuCultivar; }
    public void setVarieteOuCultivar(String varieteOuCultivar) { this.varieteOuCultivar = varieteOuCultivar; }

    public String getCirconference() { return circonference; }
    public void setCirconference(String circonference) { this.circonference = circonference; }

    public String getHauteur() { return hauteur; }
    public void setHauteur(String hauteur) { this.hauteur = hauteur; }

    public String getStadeDeveloppement() { return stadeDeveloppement; }
    public void setStadeDeveloppement(String stadeDeveloppement) { this.stadeDeveloppement = stadeDeveloppement; }

    public String getRemarquable() { return remarquable; }
    public void setRemarquable(String remarquable) { this.remarquable = remarquable; }

    public String getGeo2D() { return geo2D; }
    public void setGeo2D(String geo2D) { this.geo2D = geo2D; }
}
