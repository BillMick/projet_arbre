module org.example.java_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires kernel;
    requires layout;
    requires io;
    requires org.apache.pdfbox;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    opens org.example.java_project to javafx.fxml;
    opens org.example.Controllers.Membre to javafx.fxml;
    opens org.example.Controllers.Association to javafx.fxml;
    opens org.example.Controllers.Node to javafx.fxml;
    exports org.example.java_project;
    exports org.example.Models;
}