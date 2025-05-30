package es.ucm.fdi.iw.scheduled;

import java.time.LocalDateTime;
import java.util.Comparator;

import java.util.List;
import java.util.Optional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.dto.SubastaDTO;
import es.ucm.fdi.iw.business.enums.EstadoSubasta;
import es.ucm.fdi.iw.business.enums.RepartoSubasta;
import es.ucm.fdi.iw.business.services.puja.PujaService;
import es.ucm.fdi.iw.business.services.subastas.SubastasServices;
import es.ucm.fdi.iw.business.services.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@AllArgsConstructor
public class JobsSubastas {

    private final SubastasServices subastasServices;
    private final PujaService pujaService;
    private final UserService userService;

    @Scheduled(fixedRate = 1000 * 01) // 1 second
    @Transactional
    public void initSubastas() {
        List<SubastaDTO> listSubastasPending = subastasServices.getSubastasByStatus(EstadoSubasta.PENDIENTE);
        final LocalDateTime now = LocalDateTime.now();
        listSubastasPending.stream().forEach(subasta -> {
            if (subasta.getFechaInicio().isBefore(now)) {
                subasta.setEstado(EstadoSubasta.EN_CURSO);
                SubastaDTO s = subastasServices.updateSubasta(subasta);
                log.info("Subasta {} con nombre {} iniciada", s.getId(), s.getNombre());
            }
        });
    }

    @Scheduled(fixedRate = 1000 * 01) // 1 second
    @Transactional
    public void finishSubastas() {
        List<SubastaDTO> listSubastasPending = subastasServices.getSubastasByStatus(EstadoSubasta.EN_CURSO);
        final LocalDateTime now = LocalDateTime.now();
        // Finalizamos las subastas
        listSubastasPending.stream().forEach(subasta -> {
            if (subasta.getFechaFin().isBefore(now)) {
                List<PujaDTO> listPujas = pujaService.getPujasBySubastaId(subasta.getId());
                Optional<PujaDTO> maxPujaOpt = listPujas.stream()
                        .max(Comparator.comparingDouble(PujaDTO::getDineroPujado));
                final EstadoSubasta estado = (listPujas.isEmpty()) ? EstadoSubasta.CANCELADA : EstadoSubasta.FINALIZADA;
                final RepartoSubasta reparto = (listPujas.isEmpty()) ? RepartoSubasta.CANCELADO
                        : RepartoSubasta.REPARTO;
                subasta.setEnabled(false);
                subasta.setEstado(estado);
                subasta.setRepartoSubasta(reparto);
                maxPujaOpt.ifPresent(maxPuja -> {
                    subasta.setIdUserGanador(maxPuja.getUsuarioId());
                    userService.addMoney(subasta.getIdUserCreator(), maxPuja.getDineroPujado());
                    listPujas.stream().filter(p -> !p.equals(maxPuja))
                            .forEach(puja -> userService.addMoney(puja.getUsuarioId(), puja.getDineroPujado()));
                });
                SubastaDTO s = subastasServices.updateSubasta(subasta);
                log.info("Subasta {} con nombre {} finalizada", s.getId(), s.getNombre());
            }
        });
    }

    @Scheduled(fixedRate = 1000 * 01) // 1 second
    @Transactional
    public void updateSubastas() {
        List<SubastaDTO> listSubastasPending = subastasServices.getSubastasByStatus(EstadoSubasta.EN_CURSO);
        final LocalDateTime now = LocalDateTime.now();
        // Finalizamos las subastas
        listSubastasPending.stream().forEach(subasta -> {
            if (subasta.getFechaFin().isBefore(now)) {
                List<PujaDTO> listPujas = pujaService.getPujasBySubastaId(subasta.getId());
                Optional<PujaDTO> maxPujaOpt = listPujas.stream()
                        .max(Comparator.comparingDouble(PujaDTO::getDineroPujado));
                final EstadoSubasta estado = (listPujas.isEmpty()) ? EstadoSubasta.CANCELADA : EstadoSubasta.FINALIZADA;
                final RepartoSubasta reparto = (listPujas.isEmpty()) ? RepartoSubasta.CANCELADO
                        : RepartoSubasta.REPARTO;
                subasta.setEnabled(false);
                subasta.setEstado(estado);
                subasta.setRepartoSubasta(reparto);
                maxPujaOpt.ifPresent(maxPuja -> {
                    subasta.setIdUserGanador(maxPuja.getUsuarioId());
                    userService.addMoney(subasta.getIdUserCreator(), maxPuja.getDineroPujado());
                    listPujas.stream().filter(p -> !p.equals(maxPuja))
                            .forEach(puja -> userService.addMoney(puja.getUsuarioId(), puja.getDineroPujado()));
                });
                SubastaDTO s = subastasServices.updateSubasta(subasta);
                log.info("Subasta {} con nombre {} finalizada", s.getId(), s.getNombre());
            }
        });
    }

}
