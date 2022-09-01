package cat.udg.tfg.server.controllers;

import cat.udg.tfg.server.domain.Folder;
import cat.udg.tfg.server.domain.User;
import cat.udg.tfg.server.dto.FolderDto;
import cat.udg.tfg.server.dto.SessionDto;
import cat.udg.tfg.server.dto.UserDto;
import cat.udg.tfg.server.exceptions.UserAlreadyExistAuthenticationException;
import cat.udg.tfg.server.mapper.FolderMapper;
import cat.udg.tfg.server.mapper.UserMapper;
import cat.udg.tfg.server.services.FolderService;
import cat.udg.tfg.server.services.TokenService;
import cat.udg.tfg.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Long TOKEN_LIFE_TIME = 10800L;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FolderService folderService;
    @Autowired
    private FolderMapper folderMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private SessionDto createSessionDto(UserDto userDto) throws Exception {
        SessionDto sessionDto = createPCSessionDto(userDto);
        sessionDto.setExpirationAt(System.currentTimeMillis() + TOKEN_LIFE_TIME * 1000);
        return sessionDto;
    }

    private SessionDto createPCSessionDto(UserDto userDto) throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(tokenService.createToken(TOKEN_LIFE_TIME, userDto.getUsername(), userDto.getPassword()));
        sessionDto.setUser(userDto);
        return sessionDto;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) throws Exception {
        try {
            userService.registerUser(userMapper.dtoToEntity(userDto), passwordEncoder);
        } catch (UserAlreadyExistAuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) { //TODO exception handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().body(createSessionDto(userDto));
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<?> unregister(@RequestBody UserDto userDto) throws Exception {
        userService.deleteUser(userMapper.dtoToEntity(userDto));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) throws Exception {
        User user = userMapper.dtoToEntity(userDto);
        if (userService.authenticate(user, passwordEncoder)) {
            if(userService.sessionActive(user)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Active Session Open");
            }
            return ResponseEntity.ok().body(createSessionDto(userDto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/relogin")
    public ResponseEntity<?> relogin(@RequestBody UserDto userDto) throws Exception {
        if (userService.authenticate(userMapper.dtoToEntity(userDto), passwordEncoder)) {
            return ResponseEntity.ok().body(createSessionDto(userDto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserDto userDto) throws Exception {
        User user = userMapper.dtoToEntity(userDto);
        if (userService.authenticate(user, passwordEncoder)) {
            if(!userService.sessionActive(user)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("No Session Active Found");
            }
            userService.logout(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/login/PC")
    public ResponseEntity<?> loginPC(@RequestBody UserDto userDto) throws Exception {
        User user = userMapper.dtoToEntity(userDto);
        if (userService.authenticate(user, passwordEncoder)) {
            if(userService.sessionActivePC(user)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Active Session Open");
            }
            return ResponseEntity.ok().body(createPCSessionDto(userDto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/relogin/PC")
    public ResponseEntity<?> reloginPC(@RequestBody UserDto userDto) throws Exception {
        if (userService.authenticate(userMapper.dtoToEntity(userDto), passwordEncoder)) {
            return ResponseEntity.ok().body(createPCSessionDto(userDto));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout/PC")
    public ResponseEntity<?> logoutPC(@RequestBody UserDto userDto) throws Exception {
        User user = userMapper.dtoToEntity(userDto);
        if (userService.authenticate(user, passwordEncoder)) {
            if(!userService.sessionActivePC(user)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("No Session Active Found");
            }
            userService.logoutPC(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("{id}/newRoot")
    public ResponseEntity<?> newRoot(@PathVariable String id, @RequestBody FolderDto folderDto) {
        User user = userService.getUserById(id);
        folderService.delete(user.getRoot().getId());
        user.setRoot(folderService.generateRootFolder(folderMapper.dtoToEntity(folderDto), user));
        userService.save(user);
        return ResponseEntity.ok().build();
    }
}
