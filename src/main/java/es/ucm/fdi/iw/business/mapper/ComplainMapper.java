package es.ucm.fdi.iw.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import es.ucm.fdi.iw.business.dto.ComplainDTO;
import es.ucm.fdi.iw.business.model.Complain;

@Mapper
public interface ComplainMapper {
    ComplainMapper INSTANCE = Mappers.getMapper(ComplainMapper.class);

    @Mapping(target = "creatorId", source = "creador.id")
    ComplainDTO entityToDTO(Complain complain);

}
