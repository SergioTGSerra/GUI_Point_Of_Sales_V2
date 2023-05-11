module com.luxrest.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires org.xerial.sqlitejdbc;
    requires java.net.http;


    opens com.luxrest.gui to javafx.fxml;
    exports com.luxrest.gui;
    exports com.luxrest.gui.Controllers;
    exports com.luxrest.gui.Controllers.Dashboard;
    exports com.luxrest.gui.Controllers.Dashboard.Modules.Orders;
}