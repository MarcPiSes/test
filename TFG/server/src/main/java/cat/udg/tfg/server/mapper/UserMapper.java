package cat.udg.tfg.server.mapper;

import cat.udg.tfg.server.domain.User;
import cat.udg.tfg.server.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        FolderMapper.class,
        FileMapper.class
})
public interface UserMapper {
    UserDto entityToDto(User user);

    User dtoToEntity(UserDto userDto);
}
