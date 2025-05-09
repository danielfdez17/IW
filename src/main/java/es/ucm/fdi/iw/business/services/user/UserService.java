package es.ucm.fdi.iw.business.services.user;

import java.util.List;

import es.ucm.fdi.iw.business.dto.UserDTO;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    boolean disableUser(long id);
    boolean enableUser(long id);
    List<UserDTO> findChatUsersByUsername(final String username, final String userChatNew);
    UserDTO findUserByUsername(String username);
    Double subtractMoney(long userId, Double puja);
    void refreshSession(long id, HttpSession session);
}
