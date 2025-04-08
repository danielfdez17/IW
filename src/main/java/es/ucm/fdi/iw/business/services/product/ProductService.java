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
    public List<ProductDTO> obtenerSubastasPujadasPorUsuario(Long userId) ;
    void scheduleDeactivation(final long subastaId);
    boolean isProductActive(long productId);
}
