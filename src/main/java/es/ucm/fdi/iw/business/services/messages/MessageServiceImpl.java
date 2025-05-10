package es.ucm.fdi.iw.business.services.messages;

import java.util.List;

import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.business.dto.ChatMessage;
import es.ucm.fdi.iw.business.dto.MessageDTO;
import es.ucm.fdi.iw.business.mapper.MessageMapper;
import es.ucm.fdi.iw.business.model.Message;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.MessageRepository;
import es.ucm.fdi.iw.business.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Override
    public List<MessageDTO> getMessagesOfUser(long userId1, long userId2) {
       return messageRepository.findMessagesByUserId(userId1, userId2).stream().map(MessageMapper.INSTANCE::entityToDto).toList();
    }

    @Override
    public MessageDTO saveMessage(ChatMessage message) {
        User sender = userRepository.findByUsername(message.getFrom()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User recipient = userRepository.findByUsername(message.getRecipient()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Message newMessage = messageRepository.save(new Message(sender, recipient, message.getContent()));
        return MessageMapper.INSTANCE.entityToDto(messageRepository.save(newMessage));
    }
    
}
