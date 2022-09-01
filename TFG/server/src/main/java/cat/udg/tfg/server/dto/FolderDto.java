package cat.udg.tfg.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FolderDto {
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "root")
    private boolean root;
    @JsonProperty(value = "parentId")
    private String parentId;
    @JsonProperty(value = "childrenIds")
    private List<FolderChildrenDto> childrenIds;
    @JsonProperty(value = "files")
    private List<FileDto> files;

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

    public List<FolderChildrenDto> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(List<FolderChildrenDto> childrenIds) {
        this.childrenIds = childrenIds;
    }

    public List<FileDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileDto> files) {
        this.files = files;
    }
}
