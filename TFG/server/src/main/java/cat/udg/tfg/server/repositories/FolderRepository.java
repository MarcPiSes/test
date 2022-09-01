package cat.udg.tfg.server.repositories;

import cat.udg.tfg.server.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, String> {
}