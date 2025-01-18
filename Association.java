import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import dependances.Arbre;
import dependances.Membre;

public class Association extends Entite {

    // private String nom;
    // private int solde;
    private Membre president;
    private ArrayList<Depense> depenses = new ArrayList<Depense>();
    private ArrayList<Membre> membres = new ArrayList<Membre>();
    private ArrayList<Integer> annees_exercice = new ArrayList<Integer>();
    private ArrayList<Arbre> classification = new ArrayList<Arbre>();
    private ArrayList<Activite> activites = new ArrayList<Activite>();
    private ArrayList<Rapport> rapports = new ArrayList<Rapport>();
    private ArrayList<Recette> dons = new ArrayList<Recette>();
    private ArrayList<Recette> cotisations = new ArrayList<Recette>();
    private ArrayList<Dette> dettes = new ArrayList<Dette>();
    private ArrayList<Donateur> donateurs = new ArrayList<>();
    // private ArrayList<String> donateurs = new ArrayList<>(Arrays.asList("services municipaux", "entreprises", "associations", "individus"));

    public Association(String nom, String email, int solde) {
        super(nom, email, solde);
    }

    // public record Recette(int montant, TypeRecette type, Entite debiteur, Date date, StatutRecette statut) {
    //     public Recette {
    //         if (montant < 0) {
    //             throw new IllegalArgumentException("Le montant à créditer doit être strictement positif.");
    //         }
    //         // Effectuer une copie du débiteur
    //         // debiteur = new Entite(debiteur.nom(), debiteur.prenom(), debiteur.email());
    //         debiteur = new Entite(debiteur.nom(), debiteur.email());


    //         // Accesseur avec copie défensive
    //         // @Override
    //         // public Membre membre() {
    //         //     return new Membre(debiteur.nom(), debiteur.prenom(), debiteur.email()); 
    //         // }
    //     }
    // }

    // modifier le solde
    public int ajoutSolde(int montant) {
        this.solde += montant;
        return this.solde;
        // Persistance de données
    }

    // faire une demande de don / subvention
    public boolean demandeDeDon(int montant, Entite donateur) {
        // Verifier que le montant est bien positif.
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant à transférer doit être strictement positif.");
        }
        // Verifier que l'émetteur est différent du récepteur.
        if (donateur.equals(this) || donateur == null) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous envoyer une demande de don.");
        }

        // Date date = new Date();
        Recette don = new Recette(montant, TypeRecette.DON, donateur); // Création de l'objet Recette
        dons.add(don); // Ajout à la liste des dons
        this.ajoutSolde(montant); // Ajout au solde
        don.modifierStatut(StatutRecette.PERCUE);
        // Persistance de données
        return true;
    }

    // inscrire un membre
    public boolean inscrire(String nom, String prenom, String email) {
        // passer en argument les infos nécessaires à l'inscription
        Membre membre = new Membre(nom, prenom, email); // créer l'instance
        this.membres.add(membre); 
        // Persistance de données
        return true;
    }

    // ajouter donateur
    public boolean ajouterDonateur(String nom, String email, TypeDonateur type) {
        Donateur donateur = new Donateur(nom, email, type);
        this.donateurs.add(donateur);
        // Persistance de données
        return true; 
    }

    // désinscrire un membre
    public boolean desinscrire(Membre membre) {
        // à la place de "membre", on pourrait donner un identificateur unique
        this.membres.remove(membre);
        // Persistance de données
        return true;
    }

    // s'abonner aux notifications du service des espaces verts
    public boolean sAbonner() {
        // Persistance de données
        ajouterAbonne(this.nom); // ajout à la liste des abonnés (fonction de la classe unique ServiceDesEspacesVerts)
        return true;
    }

    // se désabonner
    public boolean SeDesabonner() {
        // Persistance de données
        retirerAbonne(this.nom);
        return true;
    }

    // type activités
    enum typeActivite {
        VISITE
    }

    
    // statut Visite
    enum statutVisite {
        PLANIFIEE, 
        EXECUTEE
    }

    

    // payer ses dettes
    public boolean payerDette (Dette dette) {
        ajoutSolde(-dette.montant()); // retirer le montant, de la dette, du solde
        if (dette.type() == TypeDette.DEFRAIEMENT) {
            // rembourser aussi le membre
        }
        else if (dette.type() == TypeDette.FACTURE) {
            // payer facture
            // penser à une façon de créer des factures à régler ...
        }
        return true;
        // Persistance de données
    }

    // révocation de membre
    public void revoquerMembre() {
        for (Membre m: this.membres) {
            if (m.cotisation.statut == statutCotisation.NONPAYE) {
                desinscrire(m);
            }
        }
    }


    // Record Recette
    // private final int montant;
    // private final TypeRecette type;
    // private final String debiteur; // nom ou email
    // private final Date date = new Date(); // à revoir pour question de test
    // private StatutRecette statut = StatutRecette.NONPERCUE;
    

    public record Don(int montant, String donateur, Date date, StatutRecette statut) {
        public Don {
            if (montant < 0) {
                throw new IllegalArgumentException("Le montant à créditer doit être strictement positif.");
            }
        }
    }
}