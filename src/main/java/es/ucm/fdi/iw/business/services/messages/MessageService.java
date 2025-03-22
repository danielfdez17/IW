package es.ucm.fdi.iw.business.services.messages;

import java.util.List;

import es.ucm.fdi.iw.business.dto.ChatMessage;
import es.ucm.fdi.iw.business.dto.MessageDTO;

public interface MessageService {
    List<MessageDTO> getMessagesOfUser(long userId);
    MessageDTO saveMessage(ChatMessage message);
}
