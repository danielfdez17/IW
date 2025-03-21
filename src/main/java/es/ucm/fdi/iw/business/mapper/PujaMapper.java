package es.ucm.fdi.iw.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.model.Puja;

@Mapper
public interface PujaMapper {

    PujaMapper INSTANCE = Mappers.getMapper(PujaMapper.class);

    @Mapping(target = "usuarioId", source = "id.usuarioId")
    @Mapping(target = "subastaId", source = "id.subastaId")
    PujaDTO pujaToPujaDTO(Puja puja);
    
    Puja pujaDTOToPuja(PujaDTO pujaDTO);
}
