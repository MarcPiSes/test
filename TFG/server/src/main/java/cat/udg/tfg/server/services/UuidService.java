package cat.udg.tfg.server.services;

import cat.udg.tfg.server.exceptions.CannotGenerateIdException;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("uuidService")
public class UuidService {

    public static final Integer MAX_NUMBER_OF_TRIES = 1000;

    public String generateId(JpaRepository repository) {
        String id = null;
        Integer tries = 0;
        do {
            String uuid = UUID.randomUUID().toString();
            if (!repository.existsById(uuid)) {
                id = uuid;
            }
            tries++;
        } while (id == null && tries < 1000);

        if (id == null) {
            Class c = ResolvableType.forType(repository.getClass()).getInterfaces()[0].getGenerics()[1].getRawClass();
            String name = "entity";
            if (c != null) {
                name = c.getName();
            }
            throw new CannotGenerateIdException(name);
        }
        return id;
    }
}
