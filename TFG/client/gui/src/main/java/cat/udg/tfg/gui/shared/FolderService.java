package cat.udg.tfg.gui.shared;

import cat.udg.tfg.gui.http.ApiService;
import cat.udg.tfg.gui.http.StatusResponseError;
import cat.udg.tfg.gui.http.exceptions.CannotReadResponseError;

public class FolderService {
    private ConfigurationFileData configurationFileData;
    private LoginService loginService;

    public FolderService(ConfigurationFileData configurationFileData, LoginService loginService) {
        this.configurationFileData = configurationFileData;
        this.loginService = loginService;
    }

    public boolean newRoot(String username, String token, String folderName) throws CannotReadResponseError, StatusResponseError {
        Boolean response = ApiService.newRoot(username, token, folderName);
        if(response) {
            return false;
        } else {
            return true;
        }
    }
}
