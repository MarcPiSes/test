package cat.udg.tfg.gui.shared;

import cat.udg.tfg.gui.shared.exceptions.CannotCreateTrayIconError;
import cat.udg.tfg.gui.shared.exceptions.CannotReadConfigurationFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SingletonService {
    private static final String iconImageLoc = "./client/gui/icon.png";
    private static LoginService loginService;
    private static TrayIcon trayIcon;
    private static FolderService folderService;
    private static FileService fileService;
    private static ConfigurationFileData configurationFileData;

    private SingletonService() {
    }

    public static void initialize() throws CannotReadConfigurationFile, CannotCreateTrayIconError {
        configurationFileData = new ConfigurationFileData();
        loginService = new LoginService(configurationFileData);
        folderService = new FolderService(configurationFileData, loginService);
        fileService = new FileService(configurationFileData, loginService);
        try {
            trayIcon = new TrayIcon(ImageIO.read(new File(iconImageLoc)));
        } catch (IOException e) {
            throw new CannotCreateTrayIconError();
        }

    }

    public synchronized static FolderService getFolderService() {
        return folderService;
    }

    public synchronized static FileService getFileService() {
        return fileService;
    }

    public synchronized static LoginService getLoginService() {
        return loginService;
    }

    public synchronized static TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public synchronized static ConfigurationFileData getConfigurationFileData() {
        return configurationFileData;
    }
}
