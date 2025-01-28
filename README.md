# projet_arbre
L'objet de ce projet est de développer de petites applications de gestion pour une association d'amateurs d'arbres.

# Particularités fonctionnelles
- **Charge de l'application**
> Le projet a été conçu de sorte à permettre la gestion de plusieurs associations, de plusieurs membres mais un seul service des espaces verts.

- **Gestion des années budgétaires**
A la création du compte d'une association, l'année est automatiquement lancée.
Pour signifier la fin de l'année, il faudra cliquer manuellement sur le bouton correspondant.
Par ailleurs, pour faciliter les tests de notre application, le cas de figure ci-dessous est possible:
  - mettre fin à l'année d'exercice en cours,
  - lancer une nouvelle année (le même jour dans le monde réel).
Dans ce cas, l'algorithme mis en place considérera que l'année est lancée le jour courant +1.
  - Au lancement de la fin de l'exercice budgétaire, un fichier de cloture est créé dans le dossier réservé à l'année d'exercice et un autre fichier mentionne la période.

- **Gestion de trésorerie**
  - Membre
> Il peut créditer son solde à partir d'un solde prévu à cet effet. Le montant de sa cotisation est défalqué de son solde lorsqu'il décide de le payer et que son compte ne risque pas d'être à découvert.
  - Association
> Au lancement de son année d'exercice budgétaire, on lui assigne certaines factures pour couvrir ses besoins en fonds de roulement notamment et une autre, fonction du nombre de ses membres.
> Elle peut payer ses dettes à tout moment avec un compte pouvant être à découvert. Ses demandes de dons sont automatiquement créditées sur son compte.

- **Service des espaces verts**

# Contraintes de développement
- Les dépendances sont présentes dans le pom.xml
- Version de Java utilisée: 21
- IDE: Intellij

# Lancement de l'application
- Retrouver le fichier Application.java (chemin: ```src/main/java/org/example/java_project/Application.java```)
- Exécuter le fichier

# Proposition de procédure de test
> Le projet a été conçu de sorte à permettre la gestion de plusieurs associations, de plusieurs membres mais un seul service des espaces verts.
> Cela dit, seules certaines infos du Service des espaces verts sont prédéfinis. Pour le reste, il faudra les créer manuellement à
> A l'exécution du projet, on vous demande de choisir l'application que vous voulez démarrer.
> Etant donné les relations de dépendances fonctionnelles, on propose de commencer par l'application Association.
> A ce niveau, créer un compte puisqu'il n'y a pas d'association prédéfinie.
> Ajouter des membres, définissez un président, etc.
> Il est possible qu'il y ait des messages d'erreurs signalant l'inexistence de certains fichiers. Cela ne devrait pas empêcher la continuation des tests.

**Problèmes rencontrés**
> Utilisation de JavaFX et Maven
> Utilisation de Intellij
> Gestion de la persistance des données
> Assurer la robustesse et la scalibilité
> Mapping des données à enregistrer avec les classes construites



projet_arbre
L'objet de ce projet est de développer de petites applications de gestion pour une association d'amateurs d'arbres. -> Application Espace vert: Cette application est destinée à la gestion et au suivi des arbres dans une zone donnée. Elle permet de visualiser, ajouter, modifier et supprimer les informations relatives aux arbres tout en gérant les notifications associées à des actions spécifiques telles que la plantation, la classification, ou l'abattage d'arbres. Fonctionnalités: -Gestion des Arbres: Ajout d'arbre : Permet l'ajout d'un nouvel arbre avec des informations détaillées telles que l'emplacement, l'espèce, le stade de développement, et plus encore. Modification des informations d'un arbre : L'utilisateur peut mettre à jour les informations existantes d'un arbre sélectionné. Suppression d'arbre : Supprime un arbre de la liste et génère automatiquement une notification d'abattage. Visualisation des arbres : Une interface utilisateur affiche la liste des arbres avec toutes leurs caractéristiques. Un champ de recherche est disponible pour filtrer les résultats. -Gestion des Notifications: Notifications générées automatiquement pour : Plantation d'un arbre Classification d'un arbre Abattage d'un arbre Notifications sauvegardées dans un fichier JSON (notifications.json) pour un suivi persistant. Les notifications peuvent être visualisées dans une interface dédiée. Importation de Données depuis un Fichier CSV (liste_arbres.csv) L'application peut charger des données sur les arbres à partir d'un fichier CSV avec un format spécifique. Interfaces Utilisateur Navigation intuitive grâce à des fenêtres distinctes pour chaque fonctionnalité. Les listes et tableaux sont interactifs, permettant une gestion rapide des données. Architecture -Packages model : Contient les classes métier, comme Arbre, Notification,LectureCSV et NotificationType . controller : Contient les contrôleurs responsables de la gestion des événements et des interactions utilisateur. -Classes Principales: Arbre : Représente un arbre avec toutes ses caractéristiques. Notification : Gère les notifications reçues et envoyées, avec sauvegarde dans un fichier JSON. LectureCSV : Charge les données sur les arbres depuis un fichier CSV. NotificationType : Enumération des types de notifications. NotificationController : Contrôleur pour afficher et gérer les notifications. FenetreAjouterArbre : Gère l'ajout d'arbres. FenetreModifierArbre : Gère la modification des arbres. FenetreModifSuppArbre : Gère la suppression des arbres et l'accès à l'édition. AppGestion : Contrôleur principal pour la gestion des arbres. AppPrincipale : Point d'entrée pour l'application. -Dépendances : Gson : Utilisé pour la manipulation des fichiers JSON. OpenCSV : Utilisé pour lire les fichiers CSV. JavaFX : Utilisé pour l'interface utilisateur. -Configuration : Fichier JSON des Notifications : Assurez-vous que le dossier projet_arbre existe à la racine de votre projet. Les notifications y seront sauvegardées dans notifications.json. Fichier CSV : Préparez un fichier CSV avec les données des arbres selon le format attendu qui sera placées dans le dossier resources . Images : Les images utilisées pour l'interface sont placées dans le dossier resources/images. -Instructions pour l'Exécution : Importation du Projet : Importez le projet dans votre IDE préféré (IntelliJ IDEA, Eclipse, etc.). Configuration des Librairies : Gson : Assurez-vous que la bibliothèque gson-2.10.1.jar est incluse dans le projet. Vous pouvez la télécharger manuellement et l'ajouter via : IntelliJ IDEA : File > Project Structure > Libraries > + > Maven et sélectionnez le fichier com.google.code.gson:gson:2.10.1 OpenCSV : Ajoutez également la bibliothèque OpenCSV à votre projet en suivant la même méthode et en sélectionnent le fichier com.opencsv:opencsv:5.10 . Configuration JavaFX : Configurez JavaFX dans votre IDE pour exécuter les fichiers FXML correctement. Exécution : Lancez la classe Main.java pour démarrer l'application.