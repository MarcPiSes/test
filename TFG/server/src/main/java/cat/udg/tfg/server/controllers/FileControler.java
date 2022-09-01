package cat.udg.tfg.server.controllers;

import cat.udg.tfg.server.dto.FileDto;
import cat.udg.tfg.server.dto.FolderDto;
import cat.udg.tfg.server.exceptions.StorageException;
import cat.udg.tfg.server.mapper.FileMapper;
import cat.udg.tfg.server.services.FileService;
import cat.udg.tfg.server.services.FolderService;
import cat.udg.tfg.server.storage.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

@RestController
@RequestMapping("/files")
public class FileControler {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FileSystemStorageService storageService;


    @PostMapping
    public ResponseEntity<Object> upload(@RequestBody FileDto fileDto, @RequestParam("file") MultipartFile file) throws Exception {
        try {
            String id = fileService.generateId();
            fileDto = fileMapper.entityToDto(fileService.storeAndCreate(file, fileMapper.dtoToEntity(fileDto), id, folderService.get(fileDto.getOwnerId())));

            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(fileDto.getId())
                    .toUriString();

            return ResponseEntity.created(new URI(uri)).body(fileDto);
        } catch (StorageException storageException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(storageException.getMessage());
        }
    }

    @DeleteMapping("/{id}}")
    public ResponseEntity<FileDto> delete(@PathVariable String id) throws Exception {
        fileService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}}")
    public ResponseEntity<?> delete(@PathVariable String id, @RequestBody FileDto fileDto, @RequestParam("file") MultipartFile file) throws Exception {
        try {
            fileDto = fileMapper.entityToDto(fileService.update(file, fileMapper.dtoToEntity(fileDto), id, folderService.get(fileDto.getOwnerId())));

            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(fileDto.getId())
                    .toUriString();

            return ResponseEntity.created(new URI(uri)).body(fileDto);
        } catch (StorageException storageException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(storageException.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {

        Resource resource = storageService.loadAsResource(id);
        FileDto fileDto = fileMapper.entityToDto(fileService.get(id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileDto.getName() + "\"")
                .body(resource);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<FileDto> get(@PathVariable String id) {
        try {
            FileDto fileDto = fileMapper.entityToDto(fileService.get(id));
            return ResponseEntity.ok(fileDto);
        } catch (EntityNotFoundException entityNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
