package cat.udg.tfg.server.mapper;

import cat.udg.tfg.server.domain.File;
import cat.udg.tfg.server.domain.User;
import cat.udg.tfg.server.dto.FileDto;
import cat.udg.tfg.server.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    FileDto entityToDto(File user);
    File dtoToEntity(FileDto userDto);
}
