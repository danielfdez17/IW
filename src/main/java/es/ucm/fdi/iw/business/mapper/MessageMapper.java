package es.ucm.fdi.iw.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import es.ucm.fdi.iw.business.dto.MessageDTO;
import es.ucm.fdi.iw.business.model.Message;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "recipient.id", target = "recipientId")
    @Mapping(source = "sender.username", target = "senderName")
    @Mapping(source = "recipient.username", target = "recipientName")
    MessageDTO entityToDto(Message message);
}
