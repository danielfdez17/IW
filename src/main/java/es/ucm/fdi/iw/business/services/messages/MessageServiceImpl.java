package es.ucm.fdi.iw.business.services.messages;

import java.util.List;

import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.MessageDTO;
import es.ucm.fdi.iw.business.mapper.MessageMapper;
import es.ucm.fdi.iw.business.repository.MessageRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;

    @Override
    public List<MessageDTO> getMessagesOfUser(long userId) {
       return this.messageRepository.findMessagesByUserId(userId).stream().map(MessageMapper.INSTANCE::entityToDto).toList();
    }
    
}
