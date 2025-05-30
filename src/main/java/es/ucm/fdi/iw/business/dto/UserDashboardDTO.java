package es.ucm.fdi.iw.business.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDashboardDTO {
    private double totalComprometido;
    private long subastasActivas;
    private long subastasGanando;
    private List<Dashboard> detalle;
}