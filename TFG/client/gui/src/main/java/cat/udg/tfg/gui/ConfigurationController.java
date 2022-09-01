package cat.udg.tfg.gui;

import cat.udg.tfg.gui.http.ApiService;
import cat.udg.tfg.gui.http.StatusResponseError;
import cat.udg.tfg.gui.http.exceptions.CannotReadResponseError;
import cat.udg.tfg.gui.http.exceptions.UserNotConnected;
import cat.udg.tfg.gui.shared.SingletonService;
import cat.udg.tfg.gui.shared.exceptions.CannotUpdateFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ConfigurationController implements ControllerInterface{
    public static final String NAME = "configuration";
    public Button openFolderButton;
    public Button logoutButton;
    public Button loginButton;
    public TextField folderPathText;
    public TextField apiUriText;
    public TextField usernameText;
    public ImageView usernameImage;
    public ImageView webImage;
    public ImageView folderImage;
    public Button editFolderButton;
    public Button openApiButton;
    public Button editApiButton;

    public ControllerInterface previous;

    public static FXMLLoader getFxml() {
        return new FXMLLoader(App.class.getResource(NAME + ".fxml"));
    }

    public void fillData() {
        apiUriText.setText(SingletonService.getConfigurationFileData().getApi());
        folderPathText.setText(SingletonService.getConfigurationFileData().getFolderPath());
        usernameText.setText(SingletonService.getConfigurationFileData().getUsername());
    }

    public void editFolder(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("New save Folder");

        File defaultDirectory = new File("c:/dev/javafx");
        chooser.setInitialDirectory(defaultDirectory);

        File selectedDirectory = chooser.showDialog(App.getStage());
        try {
            if (SingletonService.getFolderService().newRoot(
                    SingletonService.getLoginService().getUserData().getUsername(),
                    SingletonService.getLoginService().getToken(),
                    selectedDirectory.getName()
            )) {
                try {
                    folderPathText.setText(selectedDirectory.getAbsolutePath());
                    SingletonService.getConfigurationFileData().updateDataFile(selectedDirectory.getAbsolutePath());
                } catch (CannotUpdateFile e) {
                    SingletonService.getTrayIcon().displayMessage(
                            "Cannot update configuration document.",
                            "Cannot update the folder path in the configuration document.",
                            TrayIcon.MessageType.ERROR
                    );
                }
            } else {
                SingletonService.getTrayIcon().displayMessage(
                        "Cannot update server.",
                        "Cannot update the folder structure in the server",
                        TrayIcon.MessageType.ERROR
                );
            }
        } catch (CannotReadResponseError e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Response error",
                    "Cannot read the server response",
                    TrayIcon.MessageType.ERROR
            );
        } catch (StatusResponseError e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Response error",
                    "Response status no correct.",
                    TrayIcon.MessageType.ERROR
            );
        }
    }

    public void openFolder(ActionEvent actionEvent) {
        try {
            String path = SingletonService.getConfigurationFileData().getFolderPath();
            if (path != null) {
                File folder = new File(path);
                if (folder.exists()) {
                    Desktop.getDesktop().open(folder);
                } else {
                    SingletonService.getTrayIcon().displayMessage(
                            "Folder doesn't exists",
                            "The selected folder doesn't exists. Change the configuration.",
                            TrayIcon.MessageType.ERROR
                    );
                }

            }
        } catch (IOException e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Cannot access the folder",
                    "Cannot access the selected folder.",
                    TrayIcon.MessageType.ERROR
            );
        }
    }

    public void openApi(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI(SingletonService.getConfigurationFileData().getApi()));
        } catch (IOException e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Cannot open browser.",
                    "Cannot open browser.",
                    TrayIcon.MessageType.ERROR
            );
        } catch (URISyntaxException e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Url malformation",
                    "The format of the saved url is incorrect.",
                    TrayIcon.MessageType.ERROR
            );
        }
    }

    public void editApi(ActionEvent actionEvent) {
        apiUriText.setEditable(true);
        editApiButton.setText("Save");
        editApiButton.setOnAction(this::saveApi);
    }

    public void saveApi(ActionEvent actionEvent) {
        if (ApiService.correctApi(apiUriText.getText())) {
            apiUriText.setEditable(false);
            editApiButton.setText("Edit");
            editApiButton.setOnAction(this::editApi);
            try {
                SingletonService.getConfigurationFileData().updateApiDataFile(apiUriText.getText());
            } catch (CannotUpdateFile e) {
                SingletonService.getTrayIcon().displayMessage(
                        "Cannot update configuration document.",
                        "Cannot update the folder path in the configuration document.",
                        TrayIcon.MessageType.ERROR
                );
            }
        } else {
            SingletonService.getTrayIcon().displayMessage(
                    "Incorrect URL",
                    "The URL doesn't point to a valid server.",
                    TrayIcon.MessageType.ERROR
            );
        }
    }

    public void logout(ActionEvent actionEvent) {
        try {
            SingletonService.getLoginService().logout();
            usernameText.setText("");
        } catch (UserNotConnected e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Not connected",
                    "Cannot logout if not logged in.",
                    TrayIcon.MessageType.INFO
            );
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
                    "Server has returned an error status code. " + e.getCode(),
                    TrayIcon.MessageType.ERROR
            );
        }

    }

    public void login(ActionEvent actionEvent) {
        try {
            App.setRoot(LoginController.getFxml());
        } catch (IOException e) {
            SingletonService.getTrayIcon().displayMessage(
                    "Cannot open login page.",
                    "Cannot open the login page.",
                    TrayIcon.MessageType.ERROR
            );
        }
    }

    @Override
    public String getName() {
        return NAME;
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

