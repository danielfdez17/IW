package es.ucm.fdi.iw.business.services.product;

import java.util.List;
import java.util.Map;

import es.ucm.fdi.iw.business.dto.ProductDTO;

public interface ProductService {
    Map<Long, ProductDTO> getProducts();

    List<ProductDTO> getAllProducts();

    ProductDTO getProduct(final long id);

    void updateProduct(ProductDTO productDTO);

    void updateAdminProduct(ProductDTO productDTO);

    ProductDTO createSubasta(ProductDTO productDTO);

    void toggleProduct(long id, final boolean active);

    List<ProductDTO> obtenerSubastasPujadasPorUsuario(Long userId);

    boolean isProductActive(long productId);

    void addComentarioYValoracion(long id, String comentario, byte valoracion);

    void updateSendProduct(long id, String reparto);
}
