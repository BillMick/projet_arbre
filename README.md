# projet_arbre
L'objet de ce projet est de développer de petites applications de gestion pour une association d'amateurs d'arbres.
-> Application Espace vert:
Cette application est destinée à la gestion et au suivi des arbres dans une zone donnée. Elle permet de visualiser, ajouter, modifier et supprimer les informations relatives aux arbres tout en gérant les notifications associées à des actions spécifiques telles que la plantation, la classification, ou l'abattage d'arbres.
Fonctionnalités:
-Gestion des Arbres:
Ajout d'arbre : Permet l'ajout d'un nouvel arbre avec des informations détaillées telles que l'emplacement, l'espèce, le stade de développement, et plus encore.
Modification des informations d'un arbre : L'utilisateur peut mettre à jour les informations existantes d'un arbre sélectionné.
Suppression d'arbre : Supprime un arbre de la liste et génère automatiquement une notification d'abattage.
Visualisation des arbres : Une interface utilisateur affiche la liste des arbres avec toutes leurs caractéristiques. Un champ de recherche est disponible pour filtrer les résultats.
-Gestion des Notifications:
Notifications générées automatiquement pour :
Plantation d'un arbre
Classification d'un arbre
Abattage d'un arbre
Notifications sauvegardées dans un fichier JSON (notifications.json) pour un suivi persistant.
Les notifications peuvent être visualisées dans une interface dédiée.
Importation de Données depuis un Fichier CSV (liste_arbres.csv)
L'application peut charger des données sur les arbres à partir d'un fichier CSV avec un format spécifique.
Interfaces Utilisateur
Navigation intuitive grâce à des fenêtres distinctes pour chaque fonctionnalité.
Les listes et tableaux sont interactifs, permettant une gestion rapide des données.
Architecture
-Packages
model : Contient les classes métier, comme Arbre, Notification,LectureCSV et NotificationType .
controller : Contient les contrôleurs responsables de la gestion des événements et des interactions utilisateur.
-Classes Principales:
Arbre : Représente un arbre avec toutes ses caractéristiques.
Notification : Gère les notifications reçues et envoyées, avec sauvegarde dans un fichier JSON.
LectureCSV : Charge les données sur les arbres depuis un fichier CSV.
NotificationType : Enumération des types de notifications.
NotificationController : Contrôleur pour afficher et gérer les notifications.
FenetreAjouterArbre : Gère l'ajout d'arbres.
FenetreModifierArbre : Gère la modification des arbres.
FenetreModifSuppArbre : Gère la suppression des arbres et l'accès à l'édition.
AppGestion : Contrôleur principal pour la gestion des arbres.
AppPrincipale : Point d'entrée pour l'application.
-Dépendances :
Gson : Utilisé pour la manipulation des fichiers JSON.
OpenCSV : Utilisé pour lire les fichiers CSV.
JavaFX : Utilisé pour l'interface utilisateur.
-Configuration :
Fichier JSON des Notifications : Assurez-vous que le dossier projet_arbre existe à la racine de votre projet. Les notifications y seront sauvegardées dans notifications.json.
Fichier CSV : Préparez un fichier CSV avec les données des arbres selon le format attendu qui sera placées dans le dossier resources .
Images : Les images utilisées pour l'interface sont placées dans le dossier resources/images.
-Instructions pour l'Exécution :
Importation du Projet : Importez le projet dans votre IDE préféré (IntelliJ IDEA, Eclipse, etc.).
Configuration des Librairies :
Gson : Assurez-vous que la bibliothèque gson-2.10.1.jar est incluse dans le projet. Vous pouvez la télécharger manuellement et l'ajouter via : IntelliJ IDEA : File > Project Structure > Libraries > + > Maven et sélectionnez le fichier com.google.code.gson:gson:2.10.1
OpenCSV : Ajoutez également la bibliothèque OpenCSV à votre projet en suivant la même méthode et en sélectionnent le fichier com.opencsv:opencsv:5.10 .
Configuration JavaFX : Configurez JavaFX dans votre IDE pour exécuter les fichiers FXML correctement.
Exécution : Lancez la classe Main.java pour démarrer l'application.
