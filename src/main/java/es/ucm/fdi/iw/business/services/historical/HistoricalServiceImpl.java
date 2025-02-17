package es.ucm.fdi.iw.business.services.historical;

import java.util.List;

import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.HistoricalDTO;

@Service
public class HistoricalServiceImpl implements HistoricalServices {

    private static List<HistoricalDTO> historicalBids = List.of(
            new HistoricalDTO("001", "Pintura al óleo", "/img/default-pic.jpg", "$1.200", "usuario123", "01/10/2023"),
            new HistoricalDTO("002", "Reloj", "/img/default-pic.jpg", "$850", "MR", "05/10/2023"),
            new HistoricalDTO("003", "Piruleta", "/img/default-pic.jpg", "$89.500", "antoñito", "05/10/2023"),
            new HistoricalDTO("004", "OpelCorsa", "/img/default-pic.jpg", "$2.000", "robertofan7", "06/10/2023"),
            new HistoricalDTO("005", "Felicidad", "/img/default-pic.jpg", "$0", "sad_user_666", "05/12/2023"),
            new HistoricalDTO("404", "Amor", "/img/default-pic.jpg", "-", "not you", "14/02/1999")
        );


    @Override
    public List<HistoricalDTO> getHistorical() {
        return historicalBids;
    }
    
}
