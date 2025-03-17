package es.ucm.fdi.iw.business.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class PujaDTO {
    private long usuarioId;
    private long subastaId;
    private BigDecimal dineroPujado;
    private int puntuacion;
    private String comentario;
    private LocalDateTime fecha;
}