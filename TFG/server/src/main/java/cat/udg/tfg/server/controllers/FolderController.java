package cat.udg.tfg.server.controllers;

import cat.udg.tfg.server.domain.User;
import cat.udg.tfg.server.dto.FolderDto;
import cat.udg.tfg.server.mapper.FolderMapper;
import cat.udg.tfg.server.services.FolderService;
import cat.udg.tfg.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/folders")
public class FolderController {
    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderMapper folderMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}}")
    public ResponseEntity<FolderDto> getFolder(@PathVariable String id) throws Exception {
        try {
            FolderDto folderDto = folderMapper.entityToDto(folderService.get(id));
            return ResponseEntity.ok(folderDto);
        } catch (EntityNotFoundException entityNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping
    public ResponseEntity<FolderDto> update(@RequestBody FolderDto folderDto) throws Exception {
        try {
            FolderDto response = folderMapper.entityToDto(folderService.update(folderMapper.dtoToEntity(folderDto)));
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException entityNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<FolderDto> create(@RequestBody FolderDto folderDto) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FolderDto response = folderMapper.entityToDto(
                folderService.create(
                        folderMapper.dtoToEntity(folderDto),
                        userService.loadUserByUsername(user.getUsername()
                        )
                )
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}}")
    public ResponseEntity<FolderDto> delete(@PathVariable String id) throws Exception {
        folderService.delete(id);
        return ResponseEntity.ok().build();
    }
}
