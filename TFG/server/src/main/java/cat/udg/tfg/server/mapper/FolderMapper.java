package cat.udg.tfg.server.mapper;

import cat.udg.tfg.server.domain.File;
import cat.udg.tfg.server.domain.Folder;
import cat.udg.tfg.server.dto.FileDto;
import cat.udg.tfg.server.dto.FolderChildrenDto;
import cat.udg.tfg.server.dto.FolderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        FileMapper.class
})
public interface FolderMapper {

    @Mapping(source = "children", target = "childrenIds")
    FolderDto entityToDto(Folder folder);

    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(source = "childrenIds", target = "children")
    Folder dtoToEntity(FolderDto folderDto);

    @Mapping(source = "parent.id", target = "parentId")
    FolderChildrenDto entityToChildrenDto(Folder entity);
}
