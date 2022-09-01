package cat.udg.tfg.server.dto;

import cat.udg.tfg.server.domain.User;

public class SessionDto {
    private String id;
    private long expirationAt;
    private UserDto user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(long expirationAt) {
        this.expirationAt = expirationAt;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
