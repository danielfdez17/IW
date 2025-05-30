package es.ucm.fdi.iw.business.services.product;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.enums.EstadoSubasta;
import es.ucm.fdi.iw.business.enums.RepartoSubasta;
import es.ucm.fdi.iw.business.fileconfiglocal.LocalData;
import es.ucm.fdi.iw.business.mapper.SubastaMapper;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.SubastaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final SubastaRepository subastaRepository;
    private final LocalData localData;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ProductDTO> getAllProducts() {
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
                .map(p -> {
                    ProductDTO dto = SubastaMapper.INSTANCE.subastaToProductDTO(p);
                    File[] f = localData.getFolder("subasta/" + dto.getId()).listFiles();
                    List<String> namesPics = Arrays.asList(f).stream().map(File::getName).toList();
                    dto.setPics(namesPics);
                    return dto;
                })
                .orElse(null);
    }

    @Override
    @Transactional
    public void updateProduct(ProductDTO producto) {
        Subasta subasta = subastaRepository.findById(producto.getId())
                .orElseThrow(() -> new RuntimeException("Subasta no encontrada"));

        subasta.setPrecioActual(producto.getPrecioActual());
        subasta.setNombre(producto.getNombre());
        subasta.setDescripcion(producto.getDescripcion());
        subasta.setFechaFin(producto.getFechaFin());
        subasta.setMaximoPujador(producto.getMaximoPujador());
        subasta.setEnabled(producto.isEnabled());

    }

    @Override
    @Transactional
    public void updateAdminProduct(ProductDTO p) {
        Subasta subasta = subastaRepository.findById(p.getId())
                .orElseThrow(() -> new RuntimeException("Subasta no encontrada"));
        subasta.setNombre(p.getNombre());
        subasta.setDescripcion(p.getDescripcion());
        subasta.setEnabled(true);
    }

    @Override
    public ProductDTO createSubasta(ProductDTO productDTO) {
        Subasta subasta = new Subasta();
        subasta.setFechaInicio(productDTO.getFechaInicio());
        subasta.setFechaFin(productDTO.getFechaFin());
        subasta.setPrecio(productDTO.getPrecio());
        subasta.setPrecioActual(productDTO.getPrecio());
        subasta.setNombre(productDTO.getNombre());
        subasta.setDescripcion(productDTO.getDescripcion());
        subasta.setEnabled(productDTO.isEnabled());
        subasta.setEstado(productDTO.getEstadoSubasta());
        User creador = entityManager.find(User.class, productDTO.getCreadorUserId());
        if (creador == null) {
            return null;
        }
        subasta.setCreador(creador);

        entityManager.persist(subasta);
        entityManager.flush();

        return SubastaMapper.INSTANCE.subastaToProductDTO(subasta);
    }

    @Override
    @Transactional
    public void disabledProduct(long id) {
        Subasta subasta = subastaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subasta no encontrada"));
        subasta.setEnabled(false);
        subasta.setEstado(EstadoSubasta.CANCELADA);
        subasta.setComentarioGanador(null);
        subasta.setValoracionGanador(null);
        subasta.setRepartoSubasta(RepartoSubasta.CANCELADO);
        final User creador = subasta.getCreador();
        final User ganador = subasta.getGanador();
        if (ganador != null && ganador.getId() != creador.getId()) {
            creador.setAvailableMoney(creador.getAvailableMoney() - subasta.getPrecioActual());
            subasta.getPujas().stream().filter(puja -> puja.getUser().getId() != creador.getId())
                    .forEach(puja -> {
                        final User user = puja.getUser();
                        user.setAvailableMoney(user.getAvailableMoney() + puja.getDineroPujado());
                    });
        } else {
             subasta.getPujas().forEach(puja -> {
                final User user = puja.getUser();
                user.setAvailableMoney(user.getAvailableMoney() + puja.getDineroPujado());
            });
        }      
    }

    @Override
    public List<ProductDTO> obtenerSubastasPujadasPorUsuario(Long userId) {
        List<Subasta> subastas = subastaRepository.findSubastasByUserId(userId); // Obtén las subastas en las que el
                                                                                 // usuario ha pujado

        return subastas.stream()
                .map(subasta -> ProductDTO.builder()
                        .id(subasta.getId())
                        .creadorUserId(subasta.getCreador().getId())
                        .creadorUsername(subasta.getCreador().getUsername())
                        .fechaInicio(subasta.getFechaInicio())
                        .fechaFin(subasta.getFechaFin())
                        .enabled(subasta.isEnabled())
                        .rutaImagen(subasta.getRutaImagen())
                        .descripcion(subasta.getDescripcion())
                        .precioActual(subasta.getPrecioActual())
                        .precio(subasta.getPrecio())
                        .nombre(subasta.getNombre())
                        .precioInicial(subasta.getPrecioInicial())
                        .dineroPujado(subasta.getPujas().stream()
                                .filter(puja -> puja.getUser().getId() == userId)
                                .mapToDouble(puja -> puja.getDineroPujado())
                                .sum())
                        .usuarioHaPujado(true)
                        .build())
                .toList();

    }

    @Override
    public boolean isProductActive(long subastaId) {
        Subasta subasta = subastaRepository.findById(subastaId).orElse(null);
        if (subasta != null) {
            return subasta.isEnabled(); // Retorna si la subasta está activa o no
        }
        return false;
    }

    @Override
    @Transactional
    public void addComentarioYValoracion(long id, String comentario, byte valoracion) {
        Subasta subasta = subastaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subasta no encontrada"));
        subasta.setComentarioGanador(comentario);
        subasta.setValoracionGanador(valoracion);
    }

    @Override
    @Transactional
    public void updateSendProduct(long id, String reparto) {
        Subasta subasta = subastaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subasta no encontrada"));
        subasta.setRepartoSubasta(RepartoSubasta.valueOf(reparto));
    }

    @Override
    public Subasta getSubasta(long id) {
        return subastaRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductDTO> getAllProductsWithSaleActive() {
        return subastaRepository.findBySaleInProgressOrPending().stream()
                .map(p -> {
                    ProductDTO dto = SubastaMapper.INSTANCE.subastaToProductDTO(p);
                    File[] f = localData.getFolder("subasta/" + dto.getId()).listFiles();
                    List<String> namesPics = Arrays.asList(f).stream().map(File::getName).toList();
                    dto.setPics(namesPics);
                    return dto;
                })
                .toList();
    }

    @Override
    public List<ProductDTO> getProductsByCreator(long userId) {
           return subastaRepository.findByCreador(userId).stream()
                .map(p -> {
                    ProductDTO dto = SubastaMapper.INSTANCE.subastaToProductDTO(p);
                    File[] f = localData.getFolder("subasta/" + dto.getId()).listFiles();
                    List<String> namesPics = Arrays.asList(f).stream().map(File::getName).toList();
                    dto.setPics(namesPics);
                    return dto;
                })
                .toList();
    }
}
