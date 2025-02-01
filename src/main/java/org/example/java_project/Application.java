package org.example.java_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        // récupérer les infos de connexion ...
        // ...
        // Association association = new Association("SauvonsLesArbres", "sauvons@les.arbre", 0.0);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("node.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 370);
        stage.setTitle("Point de distribution");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        // when logged, create the appropriate instance of the user
    }
}


//+