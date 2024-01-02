module com.example.car_dealership {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.prefs;

    opens com.example.car_dealership to javafx.fxml;
    exports com.example.car_dealership;
    exports com.example.car_dealership.dao;
    opens com.example.car_dealership.dao to javafx.fxml;
}