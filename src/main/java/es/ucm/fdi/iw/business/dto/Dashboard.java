package es.ucm.fdi.iw.business.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {
    private long subastaId;
    private String nombre;
    private LocalDateTime fechaFin;
    private double miPujaMax;
    private double pujaMaxActual;
    private boolean voyGanando;
}
