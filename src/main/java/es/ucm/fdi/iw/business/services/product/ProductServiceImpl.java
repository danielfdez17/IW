package es.ucm.fdi.iw.business.services.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.ProductDTO;

@Service
public class ProductServiceImpl implements ProductService {
    
    private static Map<Integer, ProductDTO> objetos;
    
    static {
        objetos = new HashMap<>();
        objetos.put(1, new ProductDTO("Nombre 1", "Descripcion 1", 50.00));
        objetos.put(2, new ProductDTO("Nombre 2", "Descripcion 2", 51.00));
        objetos.put(3, new ProductDTO("Nombre 3", "Descripcion 3", 52.00));
    }

    @Override
    public Map<Integer, ProductDTO> getProducts() {
        return objetos;
    }

    @Override
    public ProductDTO getProduct(final int id) {
        return objetos.get(id);
    }

}
