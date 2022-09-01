package cat.udg.tfg.server.services;

import cat.udg.tfg.server.domain.Folder;
import cat.udg.tfg.server.domain.User;
import cat.udg.tfg.server.repositories.FileRepository;
import cat.udg.tfg.server.repositories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

@Service("folderService")
public class FolderService {

    @Value("${folders.rootName}")
    private String rootName;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    FileService fileService;
    @Autowired
    private UuidService uuidService;

    public Folder generateRootFolder(User user) {
        Folder folder = new Folder();
        folder.setChildren(new ArrayList<>());
        folder.setId(uuidService.generateId(folderRepository));
        folder.setName(rootName);
        folder.setOwner(user);
        folder.setRoot(true);
        folder.setFiles(new ArrayList<>());
        folder.setParent(null);
        return folder;
    }

    public Folder get(String id) {
        Optional<Folder> optionalFolder = folderRepository.findById(id);
        if(optionalFolder.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return optionalFolder.get();
    }

    public Folder update(Folder folder) {
        Folder dbFolder = get(folder.getId());

        dbFolder.setName(folder.getName());
        dbFolder.setRoot(folder.isRoot());
        dbFolder.setParent(get(folder.getParent().getId()));

        return folderRepository.save(dbFolder);
    }

    public Folder create(Folder folder, User user) {
        folder.setId(uuidService.generateId(folderRepository));
        folder.setOwner(user);
        folder.setFiles(new ArrayList<>());
        folder.setChildren(new ArrayList<>());
        return folderRepository.save(folder);
    }

    public void delete(String id) {
        Optional<Folder> optionalFolder = folderRepository.findById(id);
        if(optionalFolder.isPresent()) {
            optionalFolder.get().getChildren().forEach(f -> delete(f.getId()));
            optionalFolder.get().getFiles().forEach(file -> fileService.delete(file.getId()));
            folderRepository.deleteById(id);
        }
    }

    public Folder generateRootFolder(Folder folder, User user) {
        folder.setRoot(true);
        folder.setParent(null);
        return create(folder, user);
    }
}
