package cat.udg.tfg.gui.shared;

public class FileService {
    private String token;
    private ConfigurationFileData configurationFileData;
    private LoginService loginService;

    public FileService(ConfigurationFileData configurationFileData, LoginService loginService) {
        this.configurationFileData = configurationFileData;
        this.loginService = loginService;
    }
}
