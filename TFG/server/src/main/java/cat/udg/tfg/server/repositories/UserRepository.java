package cat.udg.tfg.server.repositories;

import cat.udg.tfg.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String userName);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
