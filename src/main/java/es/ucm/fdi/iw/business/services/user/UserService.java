package es.ucm.fdi.iw.business.services.user;

import java.util.List;

import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.dto.UserDashboardDTO;
public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    boolean disableUser(long id);

    boolean enableUser(long id);

    List<UserDTO> findChatUsersByUsername(final String username, final String userChatNew);

    UserDTO findUserByUsername(String username);
    Double subtractMoney(long userId, Double puja);

    double addMoney(long userId, double money);

    UserDashboardDTO getUserDashboardDTO(long userId);
}
