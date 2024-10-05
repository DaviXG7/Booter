module com.xg7plugins.booter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.xg7plugins.booter to javafx.fxml;
    exports com.xg7plugins.booter;
    exports com.xg7plugins.booter.server;
    opens com.xg7plugins.booter.server to javafx.fxml;
}