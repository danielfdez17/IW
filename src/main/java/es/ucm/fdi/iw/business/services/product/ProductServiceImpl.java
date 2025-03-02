package es.ucm.fdi.iw.business.services.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.mapper.SubastaMapper;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.repository.SubastaRepository;

@Service
public class ProductServiceImpl implements ProductService {
    
    private static Map<Integer, ProductDTO> objetos;

    private SubastaRepository subastaRepository;
    
    // static {
    //     objetos = new HashMap<>();
    //     objetos.put(1, new ProductDTO("Nombre 1", "Descripcion 1", 50.00));
    //     objetos.put(2, new ProductDTO("Nombre 2", "Descripcion 2", 51.00));
    //     objetos.put(3, new ProductDTO("Nombre 3", "Descripcion 3", 52.00));
    // }

    @Override
    public Map<Integer, ProductDTO> getProducts() {
        return objetos;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return subastaRepository.findAll().stream().map(SubastaMapper.INSTANCE::subastaToProductDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProduct(final int id) {
        return objetos.get(id);
    }

}
