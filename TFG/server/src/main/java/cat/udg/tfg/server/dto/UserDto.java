package cat.udg.tfg.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public class UserDto {
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "root")
    private FolderDto root;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FolderDto getRoot() {
        return root;
    }

    public void setRoot(FolderDto root) {
        this.root = root;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
