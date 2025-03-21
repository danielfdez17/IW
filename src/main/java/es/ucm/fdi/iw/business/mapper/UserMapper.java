package es.ucm.fdi.iw.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO entityToDto(User entity);
}
