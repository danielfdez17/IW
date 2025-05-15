package es.ucm.fdi.iw.business.services.product;

import java.util.List;
import java.util.Map;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.model.Subasta;

public interface ProductService {
    Map<Long, ProductDTO> getProducts();

    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllProductsWithSaleActive();

    ProductDTO getProduct(final long id);

    void updateProduct(ProductDTO productDTO);

    void updateAdminProduct(ProductDTO productDTO);

    ProductDTO createSubasta(ProductDTO productDTO);

    void disabledProduct(long id);

    List<ProductDTO> obtenerSubastasPujadasPorUsuario(Long userId);

    boolean isProductActive(long productId);

    void addComentarioYValoracion(long id, String comentario, byte valoracion);

    void updateSendProduct(long id, String reparto);

    Subasta getSubasta(long id);
}
