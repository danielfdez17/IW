package es.ucm.fdi.iw.business.services.product;

import java.util.List;
import java.util.Map;

import es.ucm.fdi.iw.business.dto.ProductDTO;

public interface ProductService {
    Map<Integer, ProductDTO> getProducts();
    List<ProductDTO> getAllProducts();
    ProductDTO getProduct(final int id);
}
