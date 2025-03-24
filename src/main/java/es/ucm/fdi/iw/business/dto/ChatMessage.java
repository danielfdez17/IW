package es.ucm.fdi.iw.business.dto;

import lombok.Data;

@Data
public class ChatMessage {
    private String from;
    private String recipient;
    private String content;
}
