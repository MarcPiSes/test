package cat.udg.tfg.gui.json;

public class Session {
    private String id;
    private long expirationAt;
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCorrect() {
        return id != null;
    }
}
