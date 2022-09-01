package cat.udg.tfg.server.repositories;

import cat.udg.tfg.server.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}