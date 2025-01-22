package org.example.Models;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.stream.Collectors;


public class Association extends Entite {

    // private String nom;
    // private int solde;
    public static final String datePattern = "dd-MM-yyyy";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

    private Membre president;
    // private ArrayList<Depense> depenses = new ArrayList<Depense>();
    private ArrayList<Membre> membres = new ArrayList<Membre>();
    private ArrayList<String> anneesExercice = new ArrayList<String>();
    private Date debutAnneeExercice;
    private Date finAnneeExercice;
    private int montantCotisation;
    private ArrayList<Notification> notifications = new ArrayList<Notification>();
    private ArrayList<Arbre> classification = new ArrayList<Arbre>();
    private ArrayList<Activite> activites = new ArrayList<Activite>();
    // private ArrayList<Rapport> rapports = new ArrayList<Rapport>();
    private ArrayList<Recette> dons = new ArrayList<Recette>();
    private ArrayList<Recette> cotisations = new ArrayList<Recette>();
    private ArrayList<Dette> dettes = new ArrayList<Dette>();
    private ArrayList<Donateur> donateurs = new ArrayList<>();
    // private ArrayList<String> donateurs = new ArrayList<>(Arrays.asList("services municipaux", "entreprises", "associations", "individus"));

    public Association(String nom, String email, int solde) {
        super(nom, email, solde);
    }

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
        if (m <= 0) {
            throw new IllegalArgumentException("Montant négatif ou nul non autorisé.");
        }
        this.montantCotisation = m;
        // Persistance des données ...
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
    // public ArrayList<Depense> depenses() {
    //     return this.depenses;
    // }

    // année d'exercice budgétaire en cours
    public Date debutAnneeExercice() {
        return this.debutAnneeExercice;
    }

    // année d'exercice budgétaire en cours
    public Date finAnneeExercice() {
        return this.finAnneeExercice;
    }

    // années d'exercice budgétaire
    // public ArrayList<Integer> anneesExercice() {
        //return this.anneesExercice;
    //}

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
        //if (this.anneesExercice.isEmpty()) {
            this.debutAnneeExercice = new Date();
            // #### créer le dossier associé
            // this.anneesExercice.add(this.anneeExercice);
            for (Membre membre: this.membres) {
                Recette r = new Recette(this.montantCotisation, Recette.TypeRecette.COTISATION, membre.getEmail());
                this.cotisations.add(r);
            }
            return true;
        //}
//        this.anneeExercice += 1;
//        this.anneesExercice.add(this.anneeExercice);
//
//        for (Membre membre: this.membres) {
//            Recette r = new Recette(this.montantCotisation, Recette.TypeRecette.COTISATION, membre.getEmail());
//            this.cotisations.add(r);
//        }

        // return true;
        // Persistance de données ...
    }

    // faire une demande de don / subvention
    public boolean demandeDeDon(int montant, Donateur donateur) {
        // Verifier que le montant est bien positif.
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant à transférer doit être strictement positif.");
        }
        // Verifier que l'émetteur est différent du récepteur.
        if (donateur.equals(this) || donateur == null) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous envoyer une demande de don.");
        }

        // Date date = new Date();
        Recette don = new Recette(montant, Recette.TypeRecette.DON, donateur.email); // Création de l'objet Recette
        dons.add(don); // Ajout à la liste des dons
        this.ajoutSolde(montant); // Ajout au solde
        don.modifierStatut(Recette.StatutRecette.PERCUE);
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
            if (m.getLastCotisation().statutRecette() == Recette.StatutRecette.NONPERCUE) {
                desinscrire(m);
            }
        }
    }

    // planification de visite
    public boolean planifierActivite(Activite.TypeActivite type, Date dateDePlanification, int cout, Arbre arbre) {
        if (cout <= 0) {
            throw new IllegalArgumentException("Le cout doit être strictement positif.");
        }
        if (type == Activite.TypeActivite.VISITE) {
            Visite v = new Visite(type, dateDePlanification, arbre, cout);
            activites.add(v);
        }
        else {
            Activite a = new Activite(type, dateDePlanification, cout);
            activites.add(a);
        }
        return true;
    }

    // proposition de liste d'arbres
    public List<Arbre> classification(int nombre) {
        if (nombre < 1 ) {
            throw new IllegalArgumentException("Le nombre d'arbres à choisir ne peut pas être inférieur à 1.");
        }

        Map<Arbre, Integer> treeVotes = new HashMap<>();
        for (Membre member: this.membres) {
            for (Arbre tree: member.getPropositionsClassification()) {
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

    // // s'abonner aux notifications du service des espaces verts
    // public boolean sAbonner() {
    //     // Persistance de données
    //     ajouterAbonne(this.nom); // ajout à la liste des abonnés (fonction de la classe unique ServiceDesEspacesVerts)
    //     return true;
    // }

    // // se désabonner
    // public boolean SeDesabonner() {
    //     // Persistance de données
    //     retirerAbonne(this.nom);
    //     return true;
    // }

    // #############################################
//    public static void main(String[] args) {
//        // Faire une vérification du stockage pour savoir si c'est la première exécution ou non.
//            // si ce n'est pas la première, lancer l'interface de login
//            // si c'est la première, faire comme suit:
//        Association a1 = new Association("Assoc1", "assoc1@g.m", 100);
//        System.out.println("Solde après création:" + a1.solde());
//
//        // inscrire des membres
//        a1.inscrire("Thomas", "Anderson", "thomas@a.n");
//        a1.inscrire("Mouse", "jerry", "jerry@j.m");
//        a1.inscrire("Cat", "Tom", "tom@t.c");
//        System.out.println("Membres:" + a1.membres());
//
//        // définir montant de cotisation
//        a1.modifierCotisation(50);
//
//        // lancer le début de l'année d'exercice budgétaire
//        a1.lancerAnnee(); // mentionner l'année en argument ???
//        System.out.println("Cotisations attendues:" + a1.cotisations());
//
//        // créer quelques arbres
//        Arbre ar1 = new Arbre("Arbre1", "Ile-de-France");
//        Arbre ar2 = new Arbre("Arbre2", "Polynésie");
//        Arbre ar3 = new Arbre("Arbre3", "Essonne");
//
//        // planifier une visite
//        a1.planifierActivite(Activite.TypeActivite.VISITE, new Date(), 75, ar1);
//
//        // ajouter des donateurs
//        a1.ajouterDonateur("Donateur1", "don1@d.f", TypeDonateur.ENTREPRISE);
//        a1.ajouterDonateur("Donateur2", "don2@d.f", TypeDonateur.AUTRE);
//        System.out.println("Donateurs:" + a1.donateurs());
//
//        // faire une demande de don
//        for (Donateur d : a1.donateurs()) {
//            a1.demandeDeDon(250, d);
//            break;
//        }
//        System.out.println("Solde après demande de don:" + a1.solde());
//
//        // payer cotisation
//        for (Membre m: a1.membres()) {
//            a1.ajoutSolde(a1.montantCotisation);
//        }
//        System.out.println("Solde après paiement des cotisations:" + a1.solde());
//    }
    // #############################################
}
