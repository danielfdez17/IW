package es.ucm.fdi.iw.business.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageDTO {
    private long id;
	private long senderId;
    private long recipientId;
	private String text;
	private LocalDateTime dateSent;
	private LocalDateTime dateRead;
}
