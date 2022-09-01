package cat.udg.tfg.server.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Time;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Folder root;
    @Column(name = "token_pc_date")
    private LocalDateTime tokenPcDate;
    @Column(name = "token_date")
    private LocalDateTime tokenDate;

    public User() {
    }

    public User(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Folder getRoot() {
        return root;
    }

    public void setRoot(Folder root) {
        this.root = root;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getTokenPcDate() {
        return tokenPcDate;
    }

    public void setTokenPcDate(LocalDateTime tokenPcDate) {
        this.tokenPcDate = tokenPcDate;
    }

    public LocalDateTime getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(LocalDateTime tokenDate) {
        this.tokenDate = tokenDate;
    }
}
