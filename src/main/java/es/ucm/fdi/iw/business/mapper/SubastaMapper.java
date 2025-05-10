package es.ucm.fdi.iw.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import es.ucm.fdi.iw.business.dto.CreateProductDTO;
import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.dto.SubastaDTO;
import es.ucm.fdi.iw.business.model.Subasta;

@Mapper
public interface SubastaMapper {

    SubastaMapper INSTANCE = Mappers.getMapper(SubastaMapper.class);

    @Mapping(target = "creadorUserId", source = "creador.id")
    @Mapping(target = "creadorUsername", source = "creador.username")
    @Mapping(target = "estadoSubasta", source = "estado")
    ProductDTO subastaToProductDTO(Subasta subasta);

    Subasta productDTOToSubasta(ProductDTO productDTO);

    ProductDTO createProductDTOToProductDTO(CreateProductDTO productDTO);

    @Mapping(target = "idUserCreator", source = "creador.id")
    @Mapping(target = "idUserGanador", source = "ganador.id")
    SubastaDTO entityToDto(Subasta subasta);
}
