package es.ucm.fdi.iw.business.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeMapper {
    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate.atTime(LocalTime.now());
    }
}
