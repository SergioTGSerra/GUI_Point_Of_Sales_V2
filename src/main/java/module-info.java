module com.luxrest.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.luxrest.gui to javafx.fxml;
    exports com.luxrest.gui;
    exports com.luxrest.gui.Controllers;
}