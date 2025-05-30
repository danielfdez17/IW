package es.ucm.fdi.iw.business.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComplainDTO {

    private Long id;

    private Long creatorId;

    private LocalDateTime dateTime;

    private String text;
}
