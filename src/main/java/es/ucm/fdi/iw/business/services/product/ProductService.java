package es.ucm.fdi.iw.business.services.product;

import java.util.List;

import es.ucm.fdi.iw.business.dto.ProductDTO;

public interface ProductService {
    List<ProductDTO> getProducts();
    ProductDTO getProduct(final int id);
}
