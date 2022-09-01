package cat.udg.tfg.gui;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

import cat.udg.tfg.gui.http.StatusResponseError;
import cat.udg.tfg.gui.http.exceptions.CannotReadResponseError;
import cat.udg.tfg.gui.http.exceptions.NoResponseError;
import cat.udg.tfg.gui.http.exceptions.UserAlreadyConnected;
import cat.udg.tfg.gui.http.exceptions.UserNotConnected;
import cat.udg.tfg.gui.shared.SingletonService;
import cat.udg.tfg.gui.shared.exceptions.CannotUpdateFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements ControllerInterface{
    public static final String NAME = "login";
    public ControllerInterface previous;
    public PasswordField passwordField;
    public Button loginButton;
    public Button cancelButton;
    public TextField usernameField;

    public static FXMLLoader getFxml() {
        return new FXMLLoader(App.class.getResource(NAME + ".fxml"));
    }

    public void login(ActionEvent actionEvent) {
        try {
            SingletonService.getLoginService().login(
                    usernameField.getText(),
                    passwordField.getText()
            );
        } catch (NoResponseError e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Server connection error",
                    "Cannot connect with the server.",
                    TrayIcon.MessageType.ERROR
            );
        } catch (UserAlreadyConnected e) {
            relogin(usernameField.getText(), passwordField.getText());
        } catch (CannotUpdateFile e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Configuration error",
                    "Cannot modify the configuration folder",
                    TrayIcon.MessageType.ERROR
            );
        } catch (CannotReadResponseError e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Response error",
                    "Cannot read the server response",
                    TrayIcon.MessageType.ERROR
            );
        } catch (StatusResponseError e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Server error",
                    "Server has returned an error status code. " +e.getCode(),
                    TrayIcon.MessageType.ERROR
            );
        }
        close();
    }

    private boolean relogin(String username, String password) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Already connected");
        alert.setHeaderText("The user have an active session on the server. If you log in this machine you will lose the established connection.");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                SingletonService.getLoginService().relogin(username, password);
            } catch (CannotUpdateFile e) {
                SingletonService.getTrayIcon().displayMessage(
                        "Configuration error",
                        "Cannot modify the configuration folder",
                        TrayIcon.MessageType.ERROR
                );
                alert.close();
                return false;
            } catch (CannotReadResponseError e) {
                SingletonService.getTrayIcon().displayMessage(
                        "Response error",
                        "Cannot read the server response",
                        TrayIcon.MessageType.ERROR
                );
                alert.close();
                return false;
            } catch (StatusResponseError e) {
                SingletonService.getTrayIcon().displayMessage(
                        "Server error",
                        "Server has returned an error status code. " + e.getCode(),
                        TrayIcon.MessageType.ERROR
                );
                alert.close();
                return false;
            } catch (NoResponseError e) {
                SingletonService.getTrayIcon().displayMessage(
                        "Server connection error",
                        "Cannot connect with the server.",
                        TrayIcon.MessageType.ERROR
                );
                alert.close();
                return false;
            }
        }

        alert.close();
        return true;
    }

    public void cancel(ActionEvent actionEvent) {
        close();
    }

    @Override
    public String getName() {
        return LoginController.NAME;
    }

    @Override
    public void back() throws IOException {
        App.setRoot(LoginController.getFxml());
    }

    @Override
    public ControllerInterface getPrevious() {
        return previous;
    }
}