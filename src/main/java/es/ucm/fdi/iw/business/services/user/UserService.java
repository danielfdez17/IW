package es.ucm.fdi.iw.business.services.user;

import java.util.List;

import es.ucm.fdi.iw.business.dto.UserDTO;

public interface UserService {
    boolean disableUser(long id);
    boolean enableUser(long id);
    List<UserDTO> findChatUsersByUsername(final String username, final String userChatNew);
    UserDTO getUserByUsername(String username);
}
