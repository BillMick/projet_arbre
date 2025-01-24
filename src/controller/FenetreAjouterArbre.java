package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class FenetreAjouterArbre {

    @FXML
    private Button btnAjouter;

    // Cette méthode sera appelée lorsque le bouton "Ajouter Arbre" est cliqué
    @FXML
    public void ajouterArbre() {
        // Ici, vous pouvez ajouter la logique pour ajouter un arbre.
        // Pour l'instant, nous affichons simplement une alerte.
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ajout d'Arbre");
        alert.setHeaderText("Arbre ajouté avec succès!");
        alert.setContentText("L'arbre a bien été ajouté à la base de données.");

        alert.showAndWait();
    }
}
