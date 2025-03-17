package es.ucm.fdi.iw.business.services.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.mapper.SubastaMapper;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.SubastaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class ProductServiceImpl implements ProductService {

    private final SubastaRepository subastaRepository;

        private EntityManager entityManager;
    
    @PersistenceContext
    public void setEntityManager(EntityManager em){
        this.entityManager = em;
    }
    
    @Autowired
    public ProductServiceImpl(SubastaRepository subastaRepository) {
        this.subastaRepository = subastaRepository;
    }

    

    @Override
    public List<ProductDTO> getAllProducts() {
        // Mapear Subastas a ProductDTO
        return subastaRepository.findAll().stream()
                .map(SubastaMapper.INSTANCE::subastaToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, ProductDTO> getProducts() {
        return subastaRepository.findAll().stream()
                .collect(Collectors.toMap(Subasta::getId, SubastaMapper.INSTANCE::subastaToProductDTO));
    }




    @Override
    public ProductDTO getProduct(long id) {
        return subastaRepository.findById(Long.valueOf(id)) // Convertimos id a Long
                .map(SubastaMapper.INSTANCE::subastaToProductDTO)
                .orElse(null);
    }


    @Override
    public void updateProduct(ProductDTO producto) {
        Subasta subasta = subastaRepository.findById(producto.getId())
                                        .orElseThrow(() -> new RuntimeException("Subasta no encontrada"));

        subasta.setPrecio(producto.getPrecio()); 
        subastaRepository.save(subasta);  
    }

    @Override
    public ProductDTO createSubasta(ProductDTO productDTO) {
        Subasta subasta = new Subasta();
        subasta.setFechaInicio(productDTO.getFechaInicio());
        subasta.setFechaFin(productDTO.getFechaFin());
        subasta.setPrecio(productDTO.getPrecio());
        subasta.setNombre(productDTO.getNombre());
        subasta.setDescripcion(productDTO.getDescripcion());
        User creador = this.entityManager.find(User.class, productDTO.getCreadorUserId());
        if (creador == null) {
            return null;
        }
        subasta.setCreador(creador);

        this.entityManager.persist(subasta);
        this.entityManager.flush();
        return SubastaMapper.INSTANCE.subastaToProductDTO(subasta);
    }



}

