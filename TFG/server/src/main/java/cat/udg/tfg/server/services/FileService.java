package cat.udg.tfg.server.services;

import cat.udg.tfg.server.domain.File;
import cat.udg.tfg.server.domain.Folder;
import cat.udg.tfg.server.dto.FileDto;
import cat.udg.tfg.server.repositories.FileRepository;
import cat.udg.tfg.server.storage.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;
    @Autowired
    private FileSystemStorageService storageService;
    @Autowired
    private UuidService uuidService;

    public void delete(String id) {
        storageService.delete(id);
        fileRepository.deleteById(id);
    }

    public File create(File file, Folder owner) {
        file.setOwner(owner);
        return fileRepository.save(file);
    }

    public File create(File file, String id, Folder owner) {
        file.setId(id);
        return create(file, owner);
    }

    public File storeAndCreate(MultipartFile file, File f, String id, Folder owner) {
        storageService.store(file, id);
        return create(f, id, owner);
    }

    public String generateId() {
        return uuidService.generateId(fileRepository);
    }

    public File get(String id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if(optionalFile.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return optionalFile.get();
    }

    public File update(MultipartFile file, File f, String id, Folder folder) {
        storageService.store(file, id);
        f.setId(id);
        f.setOwner(folder);
        return fileRepository.save(f);
    }
}
