package cat.udg.tfg.gui;

import cat.udg.tfg.gui.shared.exceptions.CannotCreateTrayIconError;
import cat.udg.tfg.gui.shared.SingletonService;
import cat.udg.tfg.gui.shared.exceptions.CannotReadConfigurationFile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * JavaFX App
 */
public class App extends Application {


    private static Stage stage;

    public static void setRoot(FXMLLoader fxmlLoader) throws IOException {
        stage.getScene().setRoot(fxmlLoader.load());
        stage.setResizable(false);
    }

    public static Window getStage() {
        return stage;
    }

    public static void close() {
        stage.close();
    }

    @Override public void start(final Stage s) throws IOException {
        stage = s;
        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("configuration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 530, 240);

        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
    }

    /**
     * Sets up a system tray icon for the application.
     */
    private void addAppToTray() {
        try {
            Toolkit.getDefaultToolkit();

            if (!SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            };

            SystemTray tray = SystemTray.getSystemTray();
            TrayIcon trayIcon = SingletonService.getTrayIcon();

            trayIcon.addActionListener(event -> {
                try {
                    FXMLLoader fxmlLoader = ConfigurationController.getFxml();
                    fxmlLoader.setController(new ConfigurationController());
                    ((ConfigurationController)fxmlLoader.getController()).fillData();
                    App.setRoot(fxmlLoader);
                    Platform.runLater(this::showStage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            MenuItem configuracioItem = new MenuItem("Configuration");
            configuracioItem.addActionListener(event -> {
                try {
                    FXMLLoader fxmlLoader = ConfigurationController.getFxml();
                    ((ConfigurationController)fxmlLoader.getController()).fillData();
                    App.setRoot(fxmlLoader);
                    Platform.runLater(this::showStage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            MenuItem folderItem = new MenuItem("Folder");
            folderItem.addActionListener(event -> {
                try {
                    String path = SingletonService.getConfigurationFileData().getFolderPath();
                    if(path != null) {
                        File folder = new File(path);
                        if (folder.exists()) {
                            Desktop.getDesktop().open(folder);
                        } else {
                            SingletonService.getTrayIcon()
                                    .displayMessage("Folder doesn't exists", "The selected folder doesn't exists. Change the configuration.", TrayIcon.MessageType.ERROR);
                        }

                    }
                } catch (IOException e) {
                    SingletonService.getTrayIcon()
                            .displayMessage("CAnnot access the folder", "Cannot access the selected folder.", TrayIcon.MessageType.ERROR);
                }
            });
            MenuItem userItem = new MenuItem("Transfers");
            userItem.addActionListener(event -> {
                try {
                    App.setRoot(LoginController.getFxml());
                    Platform.runLater(this::showStage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            MenuItem webItem = new MenuItem("Web");
            webItem.addActionListener(event -> {
                try {
                    Desktop.getDesktop().browse(new URI(SingletonService.getConfigurationFileData().getApi()));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(event -> {
                Platform.exit();
                tray.remove(trayIcon);
            });

            final PopupMenu popup = new PopupMenu();
            popup.add(configuracioItem);
            popup.add(userItem);
            popup.add(webItem);
            popup.add(folderItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    public static void main(String[] args) {
        try {
            SingletonService.initialize();
            launch(args);
        } catch (CannotReadConfigurationFile e) { //Todo error handling
            throw new RuntimeException(e);
        } catch (CannotCreateTrayIconError e) {
            throw new RuntimeException(e);
        }
    }
}