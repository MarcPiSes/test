package cat.udg.tfg.server.domain;

import javax.persistence.*;

@Entity
@Table(name = "FILES")
public class File {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner")
    private Folder owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getOwner() {
        return owner;
    }

    public void setOwner(Folder owner) {
        this.owner = owner;
    }

}
