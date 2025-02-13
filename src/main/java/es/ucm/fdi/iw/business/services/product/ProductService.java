package es.ucm.fdi.iw.business.services.product;

import java.util.Map;

import es.ucm.fdi.iw.business.dto.ProductDTO;

public interface ProductService {
    Map<Integer, ProductDTO> getProducts();
    ProductDTO getProduct(final int id);
}
