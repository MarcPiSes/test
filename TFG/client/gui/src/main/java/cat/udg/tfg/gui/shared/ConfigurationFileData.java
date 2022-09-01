package cat.udg.tfg.gui.shared;

import cat.udg.tfg.gui.shared.exceptions.CannotReadConfigurationFile;
import cat.udg.tfg.gui.shared.exceptions.CannotUpdateFile;
import cat.udg.tfg.gui.shared.exceptions.FileNotCreatedError;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class ConfigurationFileData {
    public static final String CONFIGURATION_DATA_PATH = "./auth";
    private String username;
    private String password;
    private String api;
    private String folderPath;

    public ConfigurationFileData() throws CannotReadConfigurationFile {
        readData();
    }

    private void createFile(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
        try {
            if(!file.createNewFile()) {
                throw new FileNotCreatedError("Cannot configure login");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData() throws CannotReadConfigurationFile {
        File file = file();
        if(!file.exists()) {
            createFile(file);
        }
        try(FileInputStream fis=new FileInputStream(file);
            Scanner sc=new Scanner(fis)) {
            List<String> data = new ArrayList<>();
            while(sc.hasNextLine())
            {
                data.add(sc.nextLine());
            }
            if(data.size() == 4) {
                username = new String(Base64.getDecoder().decode(data.get(0)));
                password = new String(Base64.getDecoder().decode(data.get(1)));
                api = new String(Base64.getDecoder().decode(data.get(2)));
                folderPath = new String(Base64.getDecoder().decode(data.get(3)));
            }
        } catch (IOException e) { //TODO error handling
            throw new CannotReadConfigurationFile(e);
        }
    }

    public void updateDataFile(String username, String password, String api, String folderPath) throws CannotUpdateFile {
        File file = file();
        if(!file.exists()) {
            createFile(file);
        }
        try(FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);) {
            printWriter.println(Base64.getEncoder().encodeToString(username.getBytes()));
            printWriter.println(Base64.getEncoder().encodeToString(password.getBytes()));
            printWriter.println(Base64.getEncoder().encodeToString(api.getBytes()));
            printWriter.println(Base64.getEncoder().encodeToString(folderPath.getBytes()));
        } catch (IOException e) { //TODO error handling
            throw new CannotUpdateFile(e);
        }
        updateData(username, password, api, folderPath);
    }

    private void updateData(String username, String password, String api, String folderPath) {
        this.username = username;
        this.password = password;
        this.api = api;
        this.folderPath = folderPath;
    }

    public void updateDataFile(String username, String password) throws CannotUpdateFile {
        updateDataFile(username, password, this.getApi(), this.getFolderPath());
    }

    private File file() {
        return new File(CONFIGURATION_DATA_PATH);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getApi() {
        return api;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void updateDataFile(String absolutePath) throws CannotUpdateFile {
        updateDataFile(this.getUsername(), this.getPassword(), this.getApi(), absolutePath);
    }

    public void updateApiDataFile(String api) throws CannotUpdateFile {
        updateDataFile(this.getUsername(), this.getPassword(), api, this.getFolderPath());
    }

    //String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
}
