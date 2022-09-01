package cat.udg.tfg.server.services;

import cat.udg.tfg.server.domain.User;
import cat.udg.tfg.server.exceptions.UserAlreadyExistAuthenticationException;
import cat.udg.tfg.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service("userService")
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FolderService folderService;
    @Autowired
    private UuidService uuidService;

    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(userName);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(userName);

        User user = optionalUser.get();

        return new User(user.getUsername(), user.getPassword());
    }

    public boolean authenticate(User userDto, PasswordEncoder passwordEncoder) {
        try {
            User user = loadUserByUsername(userDto.getUsername());
            return passwordEncoder.matches(userDto.getPassword(), user.getPassword());
        } catch (UsernameNotFoundException usernameNotFoundException) {
            return false;
        }
    }

    public void registerUser(User user, PasswordEncoder passwordEncoder) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistAuthenticationException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(uuidService.generateId(userRepository));
        user.setRoot(folderService.generateRootFolder(user));
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getUserByUserName(User user) {
        Optional<User> optionaldbUser = userRepository.findByUsername(user.getUsername());
        if(optionaldbUser.isEmpty()) {
            throw new UsernameNotFoundException(user.getUsername());
        }
        return optionaldbUser.get();
    }

    public User getUserById(String id) {
        Optional<User> optionaldbUser = userRepository.findById(id);
        if(optionaldbUser.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return optionaldbUser.get();
    }

    public boolean sessionActive(User user) {
        User dbUser = getUserByUserName(user);
        return dbUser.getTokenDate() != null && !dbUser.getTokenDate().isBefore(LocalDateTime.now());
    }

    public boolean sessionActivePC(User user) {
        User dbUser = getUserByUserName(user);
        return dbUser.getTokenPcDate() != null;
    }

    public void deleteUser(User user) {
        folderService.delete(user.getRoot().getId());
        userRepository.deleteByUsername(user.getUsername());
    }

    public void logout(User user) {
        User dbUser = getUserByUserName(user);
        dbUser.setTokenDate(null);
        userRepository.save(dbUser);
    }

    public void logoutPC(User user) {
        User dbUser = getUserByUserName(user);
        dbUser.setTokenPcDate(null);
        userRepository.save(dbUser);
    }
}
