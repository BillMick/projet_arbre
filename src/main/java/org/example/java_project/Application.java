//package org.example.java_project;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        // Load the FXML file
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("org/example/java_project/hello-view.fxml")); // Replace "dashboard.fxml" with your actual file name
//        Parent root = loader.load();
//
//        // Set up the scene
//        Scene scene = new Scene(root);
//
//        // Configure the stage
//        primaryStage.setTitle("Member Dashboard");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args); // Launch the JavaFX application
//    }
//}


package org.example.java_project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.Models.Association;

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