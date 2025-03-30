package es.ucm.fdi.iw.business.services.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    @PersistenceContext
    public void setEntityManager(EntityManager em){
        this.entityManager = em;
    }
    
    @Autowired
    public ProductServiceImpl(SubastaRepository subastaRepository) {
        this.subastaRepository = subastaRepository;
    }


    public void programarDesactivacion(Subasta subasta) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaFin = subasta.getFechaFin();

        long delay = Duration.between(ahora, fechaFin).toMillis();
        if (delay <= 0) { // Si la fecha ya ha pasado desactivar 
            scheduleDeactivation(subasta.getId());
        } else { // Programar la desactivacion
            scheduler.schedule(() -> scheduleDeactivation(subasta.getId()), delay, TimeUnit.MILLISECONDS);
        }
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
                .map( SubastaMapper.INSTANCE::subastaToProductDTO)
                .orElse(null);
    }

    @Override
    public void updateProduct(ProductDTO producto) {
        Subasta subasta = subastaRepository.findById(producto.getId())
                                        .orElseThrow(() -> new RuntimeException("Subasta no encontrada"));

        subasta.setPrecio(producto.getPrecio()); 
        subasta.setMaximoPujador(producto.getMaximoPujador());

        subastaRepository.save(subasta);  
    }

    @Override
    @Transactional
    public void updateAdminProduct(ProductDTO p){
        Subasta subasta = subastaRepository.findById(p.getId()).orElseThrow(() -> new RuntimeException("Subasta no encontrada"));
        subasta.setNombre(p.getNombre());
        subasta.setDescripcion(p.getDescripcion()); 
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
        subasta.setEnabled(productDTO.isEnabled());
        User creador = this.entityManager.find(User.class, productDTO.getCreadorUserId());
        if (creador == null) {
            return null;
        }
        subasta.setCreador(creador);

        this.entityManager.persist(subasta);
        this.entityManager.flush();
        
        programarDesactivacion(subasta);

        return SubastaMapper.INSTANCE.subastaToProductDTO(subasta);
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public  void scheduleDeactivation(long subastaId) {
        Subasta subasta = subastaRepository.findById(subastaId).orElse(null);
        if (subasta != null && subasta.isEnabled()) {
            subasta.setEnabled(false);
            subastaRepository.save(subasta);
            System.out.println("Subasta con ID " + subastaId + " ha sido desactivada automáticamente.");

            ProductDTO productDTO = SubastaMapper.INSTANCE.subastaToProductDTO(subasta);
            messagingTemplate.convertAndSend("/topic/product-updates/" + subastaId, productDTO);
        }
    }
    

    @Override
    @Transactional
    public void toggleProduct(long id, final boolean active) {
        Subasta subasta = subastaRepository.findById(id).orElseThrow(() -> new RuntimeException("Subasta no encontrada"));
        subasta.setEnabled(active);
        subastaRepository.save(subasta);
    }

    public boolean isProductActive(long subastaId) {
        Subasta subasta = subastaRepository.findById(subastaId).orElse(null);
        if (subasta != null) {
            return subasta.isEnabled();  // Retorna si la subasta está activa o no
        }
        return false;
    }
}