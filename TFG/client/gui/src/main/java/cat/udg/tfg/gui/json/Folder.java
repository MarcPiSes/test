package cat.udg.tfg.gui.json;

import java.util.List;

public class Folder {
    private String id;
    private String name;
    private boolean root;
    private String parentId;
    private List<FolderChildren> childrenIds;
    private List<File> files;

    public Folder(String id, String name, boolean root, String parentId, List<FolderChildren> childrenIds, List<File> files) {
        this.id = id;
        this.name = name;
        this.root = root;
        this.parentId = parentId;
        this.childrenIds = childrenIds;
        this.files = files;
    }

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public List<FolderChildren> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(List<FolderChildren> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
