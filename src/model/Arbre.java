package model;

public class Arbre {
    private String idBase;
    private String genre;
    private String espece;
    private String nomCommun;
    private double circonference;
    private double hauteur;
    private String stadeDeveloppement;
    private String adresse;
    private String gpsCoordinates;
    private boolean remarquable;

    // Constructeur
    public Arbre(String idBase, String genre, String espece, String nomCommun, double circonference,
                 double hauteur, String stadeDeveloppement, String adresse, String gpsCoordinates,
                 boolean remarquable) {
        this.idBase = idBase;
        this.genre = genre;
        this.espece = espece;
        this.nomCommun = nomCommun;
        this.circonference = circonference;
        this.hauteur = hauteur;
        this.stadeDeveloppement = stadeDeveloppement;
        this.adresse = adresse;
        this.gpsCoordinates = gpsCoordinates;
        this.remarquable = remarquable;
    }

    @Override
    public String toString() {
        return "Arbre {" +
                "Nom Commun='" + nomCommun + '\'' +
                ", Genre='" + genre + '\'' +
                ", Espèce='" + espece + '\'' +
                ", Hauteur=" + hauteur + " m" +
                ", Circonférence=" + circonference + " cm" +
                ", Remarquable=" + (remarquable ? "Oui" : "Non") +
                '}';
    }


    // Getters et Setters
    public String getIdBase() { return idBase; }
    public String getGenre() { return genre; }
    public String getEspece() { return espece; }
    public String getNomCommun() { return nomCommun; }
    public double getCirconference() { return circonference; }
    public double getHauteur() { return hauteur; }
    public String getStadeDeveloppement() { return stadeDeveloppement; }
    public String getAdresse() { return adresse; }
    public String getGpsCoordinates() { return gpsCoordinates; }
    public boolean isRemarquable() { return remarquable; }
}

