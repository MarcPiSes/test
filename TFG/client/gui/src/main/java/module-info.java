module cat.udg.tfg.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    opens cat.udg.tfg.gui to javafx.fxml;
    exports cat.udg.tfg.gui;
    exports cat.udg.tfg.gui.http;
    opens cat.udg.tfg.gui.http to javafx.fxml;
    exports cat.udg.tfg.gui.shared.exceptions;
    opens cat.udg.tfg.gui.shared.exceptions to javafx.fxml;
    exports cat.udg.tfg.gui.shared;
    opens cat.udg.tfg.gui.shared to javafx.fxml;
    exports cat.udg.tfg.gui.http.exceptions;
    opens cat.udg.tfg.gui.http.exceptions to javafx.fxml;
}
