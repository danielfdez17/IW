package es.ucm.fdi.iw.business.services.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.mapper.UserMapper;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    
    @Transactional
    @Override
    public boolean disableUser(long id) {
        return this.userRepository.findById(id).map(user -> {
            user.setEnabled(false);
            return this.userRepository.save(user) != null;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean enableUser(long id) {
        return this.userRepository.findById(id).map(user -> {
            user.setEnabled(true);
            return this.userRepository.save(user) != null;
        }).orElse(false);
    }
    
    @Override
    public List<UserDTO> findChatUsersByUsername(final String username, final String userChatNew) {
        User user = this.userRepository.findByUsername(username).orElseThrow();
        List<UserDTO> users = new ArrayList<>(this.userRepository.findChatPartners(user.getId()).stream().map(UserMapper.INSTANCE::entityToDto)
                                                                                 .toList());
        if(StringUtils.hasText(userChatNew) && users.stream().noneMatch(u -> u.getUsername().equals(userChatNew))){
            Optional<User> userOpt = this.userRepository.findByUsername(userChatNew);
            if (userOpt.isPresent() && !username.equals(userChatNew)) {
                UserDTO userDto = UserMapper.INSTANCE.entityToDto(userOpt.get());
                users.addFirst(userDto);
            }
        }
        return users;
    }
    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("No existe el usuario con username: " + username));
        return UserMapper.INSTANCE.entityToDto(user);
    }
}
