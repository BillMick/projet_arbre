import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dependances.Arbre;
import dependances.Membre;
import dependances.Notification;

public class Association extends Entite {

    // private String nom;
    // private int solde;
    private Membre president;
    private ArrayList<Depense> depenses = new ArrayList<Depense>();
    private ArrayList<Membre> membres = new ArrayList<Membre>();
    private ArrayList<Integer> anneesExercice = new ArrayList<Integer>();
    private int anneeExercice; // année d'exercice en cours
    private int montantCotisation;
    private ArrayList<Notification> notifications = new ArrayList<Notification>();
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

    // ### setters ###

    // solde
    public int ajoutSolde(int montant) {
        this.solde += montant;
        return this.solde;
        // Persistance de données
    }

    // president
    public Membre choisirPresident(Membre m) {
        this.president = m;
        return this.president;
    }

    // ajouter donateur
    public boolean ajouterDonateur(String nom, String email, TypeDonateur type) {
        Donateur donateur = new Donateur(nom, email, type);
        this.donateurs.add(donateur);
        // Persistance de données
        return true; 
    }

    // supprimer un donateur
    public boolean supprimerDonateur(Donateur d) {
        // à la place de "membre", on pourrait donner un identificateur unique
        this.donateurs.remove(d);
        // Persistance de données
        return true;
    }
    

    // modifier montant de la cotisation
    public boolean modifierCotisation(int m) {
        if (m < 0) {
            throw new IllegalArgumentException("Montant non")
        }
        this.montantCotisation = m;
        return true;
    }

    // // Getters

    // membres
    public ArrayList<Membre> membres() {
        return this.membres;
    }

    // solde
    public int solde() {
        return this.solde;
    }

    // dépenses
    public ArrayList<Depense> depenses() {
        return this.depenses;
    }

    // année d'exercice budgétaire en cours
    public int anneeExercice() {
        return this.anneeExercice;
    }

    // années d'exercice budgétaire
    public ArrayList<Integer> anneesExercice() {
        return this.anneesExercice;
    }

    // recupérer le montant de la cotisation
    public int montantCotisation() {
        return this.montantCotisation;
    }

    // notifications
    public ArrayList<Notification> notifications() {
        return this.notifications;
    }

    // classification des arbres
    public ArrayList<Arbre> classification() {
        return this.classification;
    }

    // activités
    public ArrayList<Activite> activites() {
        return this.activites;
    }

    // dons
    public ArrayList<Recette> dons() {
        return this.dons;
    }

    // cotisations
    public ArrayList<Recette> cotisations() {
        return this.cotisations;
    }

    // dettes
    public ArrayList<Dette> dettes() {
        return this.dettes;
    }

    // donateurs
    public ArrayList<Donateur> donateurs() {
        return this.donateurs;
    }

    // ### Logic ###

    // définir l'année d'exercice budgétaire (normalement, on doit l'appeler qu'une fois à la création de l'association...au début de sa première année d'activité)
    public boolean lancerAnnee() {
        // int numberOfDigits = Integer.toString(a).length();
        // System.out.println("Number of digits: " + numberOfDigits);
        // if (a < 2000) { // 
        //     throw new IllegalArgumentException("Nous somme au 21e siècle.");
        // }
        if (this.anneesExercice.isEmpty()) {
            this.anneeExercice = LocalDate.now().getYear();
            this.anneesExercice.add(this.anneeExercice);
            return true;
        }
        this.anneeExercice += 1;
        this.anneesExercice.add(this.anneeExercice);

        for (Membre membre: this.membres) {
            Recette r = new Recette(this.montantCotisation, TypeRecette.COTISATION, membre);
            this.cotisations.add(r);
        }

        return true;
        // Persistance de données ...
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

    // désinscrire un membre
    public boolean desinscrire(Membre membre) {
        // à la place de "membre", on pourrait donner un identificateur unique
        this.membres.remove(membre);
        // Persistance de données
        return true;
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
            if (m.cotisation().statutRecette() == StatutRecette.NONPERCUE) {
                desinscrire(m);
            }
        }
    }

    // planification de visite
    public boolean planifierActivite() {

    }

    // proposition de liste d'arbres
    public List<Arbre> classification(int nombre) {
        if (nombre < 1 ) {
            throw new IllegalArgumentException("Le nombre d'arbres à choisir ne peut pas être inférieur à 1.");
        }

        Map<Arbre, Integer> treeVotes = new HashMap<>();
        for (Membre member: this.membres) {
            for (Arbre tree: member.getProposedTrees()) {
                treeVotes.put(tree, treeVotes.getOrDefault(tree, 0) + 1);
            }
        }

        // Sort trees by votes in descending order
        List<Map.Entry<Arbre, Integer>> sortedTrees = new ArrayList<>(treeVotes.entrySet());
        sortedTrees.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        // Get and set the top 5 trees
        // List<Arbre> top5Trees = new ArrayList<>();
        for (int i = 0; i < Math.min(nombre, sortedTrees.size()); i++) {
            this.classification.add(sortedTrees.get(i).getKey());
        }

        // Persistance de données ...
        return this.classification;
        // ArrayList<ArrayList<String>> classification; //String or Arbre ???
        // for (Membre m: this.membres) {
        //     // get the proposition of each member and add it to a list
        //     classification.add(m.classification);

        //     // Step 1: Flatten the list
        // List<String> all_votes = classification.stream()
        //         .flatMap(List::stream)
        //         .collect(Collectors.toList());

        // // Step 2: Count the votes
        // Map<String, Long> votes = all_votes.stream()
        //     .collect(Collectors.groupingBy(
        //         vote -> vote,
        //         Collectors.counting()
        //     ));

        // // Step 3: Sort by vote count and select top 5
        // List<String> top_5 = votes.entrySet().stream()
        //     .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
        //     .limit(5)
        //     .map(Map.Entry::getKey)
        //     .collect(Collectors.toList());
        // }

        // return top_5;

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