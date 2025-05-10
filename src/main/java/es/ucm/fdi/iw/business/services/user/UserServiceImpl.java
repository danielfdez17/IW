package es.ucm.fdi.iw.business.services.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.parser.Entity;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.util.StringUtils;

import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.mapper.UserMapper;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    @Override
    public boolean disableUser(long id) {
        return userRepository.findById(id).map(user -> {
            user.setEnabled(false);
            return userRepository.save(user) != null;
        }).orElse(false);
    }

    @Override
    @Transactional
    public boolean enableUser(long id) {
        return userRepository.findById(id).map(user -> {
            user.setEnabled(true);
            return userRepository.save(user) != null;
        }).orElse(false);
    }

    @Override
    public List<UserDTO> findChatUsersByUsername(final String username, final String userChatNew) {
        User user = userRepository.findByUsername(username).orElseThrow();
        List<UserDTO> users = new ArrayList<>(
                userRepository.findChatPartners(user.getId()).stream().map(UserMapper.INSTANCE::entityToDto)
                        .toList());
        if (StringUtils.hasText(userChatNew) && users.stream().noneMatch(u -> u.getUsername().equals(userChatNew))) {
            Optional<User> userOpt = userRepository.findByUsername(userChatNew);
            if (userOpt.isPresent() && !username.equals(userChatNew)) {
                UserDTO userDto = UserMapper.INSTANCE.entityToDto(userOpt.get());
                users.addFirst(userDto);
            }
        }
        return users;
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        User u = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.INSTANCE.entityToDto(u);
    }
   
    @Override
    public double addMoney(long userId, double money) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) 
            return 0;
        User u = user.get();
        u.setAvailableMoney(u.getAvailableMoney() + money);
        userRepository.save(u);
        return u.getAvailableMoney();   
    }
}
