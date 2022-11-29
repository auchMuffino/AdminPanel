module needs.adminpanel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires org.kordamp.ikonli.javafx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mysql.connector.j;

    opens needs.adminpanel to javafx.fxml;
    opens needs.importantclasses to javafx.fxml;
    exports needs.adminpanel;
    exports needs.importantclasses;
}