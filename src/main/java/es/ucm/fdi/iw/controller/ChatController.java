package es.ucm.fdi.iw.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucm.fdi.iw.business.dto.ChatMessage;
import es.ucm.fdi.iw.business.dto.MessageDTO;
import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.services.messages.MessageService;
import es.ucm.fdi.iw.business.services.user.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("chat")
@AllArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final MessageService messageService;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    @GetMapping
    public String chat(Model model, @RequestParam(defaultValue = "", required = false) final String userChatNew) {
        UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO uDto = userService.findUserByUsername(u.getUsername());
        List<UserDTO> users = userService.findChatUsersByUsername(u.getUsername(), userChatNew);
        model.addAttribute("listUser", users);
        model.addAttribute("messages",  users.isEmpty() ? new ArrayList<>() : messageService.getMessagesOfUser(users.get(0).getId(), uDto.getId() ));
        return "chat";
    }

    @GetMapping("/{idUser}/{idUserSender}")
    @ResponseBody
    public List<MessageDTO> getMessages(@PathVariable long idUser, @PathVariable long idUserSender) {
        return messageService.getMessagesOfUser(idUser, idUserSender);
    }
    

    @PostMapping("/private")
    @ResponseBody
    public void handlePrivateMessage(@RequestBody ChatMessage chatMessage) {
        System.out.println("Received message: " + chatMessage.getContent() + " from " + chatMessage.getFrom() + " to " + chatMessage.getRecipient());
        MessageDTO m = messageService.saveMessage(chatMessage);
        messagingTemplate.convertAndSendToUser( 
            chatMessage.getRecipient(),
            "/queue/messages",
            m
        );
    }
    
}
