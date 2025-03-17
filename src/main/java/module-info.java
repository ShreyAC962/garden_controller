module com.example.AutomatedGarden {
    exports com.example.ComputerizedGarden.Model;
    requires javafx.controls;
    requires java.logging;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.desktop;
    opens com.example.ComputerizedGarden to javafx.graphics;
    opens com.example.ComputerizedGarden.Controllers to javafx.graphics;
    opens com.example.ComputerizedGarden.View to javafx.graphics;

}