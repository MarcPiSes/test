package cat.udg.tfg.gui.http;

import cat.udg.tfg.gui.http.exceptions.*;
import cat.udg.tfg.gui.json.Folder;
import cat.udg.tfg.gui.json.Session;
import cat.udg.tfg.gui.json.User;

import java.io.IOException;
import java.net.HttpURLConnection;

import cat.udg.tfg.gui.shared.SingletonService;
import com.google.gson.Gson;

public class ApiService {
    private ApiService() {

    }

    //gson.fromJson(json, Staff.class)
    public static Session login(String apiUrl, String username, String password) throws UserAlreadyConnected, NoResponseError, StatusResponseError, CannotReadResponseError {
        HttpURLConnection conn = HttpConnectionService.post(apiUrl + "/users/login/PC");
        HttpConnectionService.addBody(conn, new Gson().toJson(new User(username, password)));

        try {
            if (conn.getResponseCode() != 200 && conn.getResponseCode() != 202) {
                throw new StatusResponseError(conn.getResponseCode());
            } else if (conn.getResponseCode() != 202) {
                throw new UserAlreadyConnected("Already connected.");
            }
        } catch (IOException e) {
            throw new CannotReadResponseError(); //TODO
        }
        Session response = (Session) HttpConnectionService.getResponse(conn, Session.class);
        if (response != null) {
            conn.disconnect();
            return response;
        } else {
            throw new NoResponseError();
        }
    }

    public static void logout(String username, String password, String apiUrl, String token) throws UserNotConnected, StatusResponseError, CannotReadResponseError {
        HttpURLConnection conn = HttpConnectionService.post(apiUrl + "/users/logout/PC");
        HttpConnectionService.setToken(conn, token);
        HttpConnectionService.addBody(conn, new Gson().toJson(new User(username, password)));
        try {
            if (conn.getResponseCode() != 200 && conn.getResponseCode() != 202) {
                throw new StatusResponseError(conn.getResponseCode());
            } else if (conn.getResponseCode() != 202) {
                throw new UserNotConnected();
            }
        } catch (IOException e) {
            throw new CannotReadResponseError(); //TODO
        }
    }

    public static Boolean newRoot(String id, String token, String folderName) throws StatusResponseError, CannotReadResponseError {
        HttpURLConnection conn = HttpConnectionService.put(
                String.format("%s/users/%s/newRoot", SingletonService.getConfigurationFileData().getApi(), id)
        );
        HttpConnectionService.setToken(conn, token);
        HttpConnectionService.addBody(conn, new Gson().toJson(new Folder(null, folderName, true, null, null, null)));
        try {
            if (conn.getResponseCode() != 200) {
                throw new StatusResponseError(conn.getResponseCode());
            } else {
                return true;
            }
        } catch (IOException e) {
            throw new CannotReadResponseError(); //TODO
        }
    }

    public static Boolean correctApi(String api) {
        HttpURLConnection conn = HttpConnectionService.get(
                String.format(String.format("%s/exists", api))
        );
        try {
            if (conn.getResponseCode() != 200) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static Session relogin(String username, String password, String api) throws NoResponseError, CannotReadResponseError, StatusResponseError {
        HttpURLConnection conn = HttpConnectionService.post(api + "/users/relogin/PC");
        HttpConnectionService.addBody(conn, new Gson().toJson(new User(username, password)));

        try {
            if (conn.getResponseCode() != 200) {
                throw new StatusResponseError(conn.getResponseCode());
            }
            Session response = (Session) HttpConnectionService.getResponse(conn, Session.class);
            if (response != null) {
                conn.disconnect();
                return response;
            } else {
                throw new NoResponseError();
            }
        } catch (IOException e) {
            throw new CannotReadResponseError(); //TODO
        }
    }
}
