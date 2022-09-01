package cat.udg.tfg.gui.shared;

import cat.udg.tfg.gui.http.ApiService;
import cat.udg.tfg.gui.http.StatusResponseError;
import cat.udg.tfg.gui.http.exceptions.CannotReadResponseError;
import cat.udg.tfg.gui.http.exceptions.NoResponseError;
import cat.udg.tfg.gui.http.exceptions.UserAlreadyConnected;
import cat.udg.tfg.gui.http.exceptions.UserNotConnected;
import cat.udg.tfg.gui.json.Session;
import cat.udg.tfg.gui.json.User;
import cat.udg.tfg.gui.shared.exceptions.CannotUpdateFile;

public class LoginService {

    private String token;
    private User userData;
    private ConfigurationFileData configurationFileData;

    public LoginService(ConfigurationFileData configurationFileData) {
        this.configurationFileData = configurationFileData;
    }

    public boolean isLogged() {
        return token != null;
    }

    public String getToken() {
        return token;
    }

    public User getUserData() {
        return userData;
    }

    public boolean login(String username, String password) throws NoResponseError, UserAlreadyConnected, CannotUpdateFile, CannotReadResponseError, StatusResponseError {
        Session response = null;
        response = ApiService.login(username, password, configurationFileData.getApi());
        if (!response.isCorrect()) {
            return false;
        } else {
            this.token = response.getId();
            this.userData = response.getUser();
            configurationFileData.updateDataFile(username, password);
            return true;
        }
    }

    public void logout() throws UserNotConnected, CannotUpdateFile, CannotReadResponseError, StatusResponseError {
        ApiService.logout(configurationFileData.getUsername(), configurationFileData.getPassword(), configurationFileData.getApi(), token);
        configurationFileData.updateDataFile(null, null);
        token = null;
    }

    public boolean relogin(String username, String password) throws CannotUpdateFile, NoResponseError, CannotReadResponseError, StatusResponseError {
        Session response = ApiService.relogin(username, password, configurationFileData.getApi());
        if(response.isCorrect()) {
            return false;
        } else {
            this.token = response.getId();
            this.userData = response.getUser();
            configurationFileData.updateDataFile(username, password);
            return true;
        }
    }
}
