package cat.udg.tfg.gui;

import cat.udg.tfg.gui.shared.SingletonService;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.io.IOException;

public interface ControllerInterface {

    String getName();

    void back() throws IOException;

    default void close() {
        try {
            if (getPrevious() != null) {
                getPrevious().back();
            } else {
                App.close();
            }
        } catch (IOException e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Page error",
                    "Cannot load the page.",
                    TrayIcon.MessageType.ERROR
            );
            App.close();
        }
    }

    ControllerInterface getPrevious();


}
