module com.example.AutomatedGarden {
    exports com.example.AutomatedGarden.Model;
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
    requires javafx.graphics;
    requires java.desktop;
    opens com.example.AutomatedGarden to javafx.graphics;
    opens com.example.AutomatedGarden.Controllers to javafx.graphics;
    opens com.example.AutomatedGarden.View to javafx.graphics;

}