package es.ucm.fdi.iw.business.services.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.mapper.UserMapper;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Optional<User> optionalUser = this.userRepository.findByUsername(userDTO.getUsername());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("El nombre de usuario " + userDTO.getUsername() + " ya existe");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDeliveryAddress(userDTO.getDeliveryAddress());
        user.setRoles("USER");
        user.setEnabled(true);
        user.setAvailableMoney(0.0);
        return UserMapper.INSTANCE.entityToDto(this.userRepository.save(user));
    }

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
    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public double addMoney(long userId, double money) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            return 0;
        User u = user.get();
        u.setAvailableMoney(u.getAvailableMoney() + money);
        userRepository.save(u);
        return u.getAvailableMoney();
    }

    @Override
    @Transactional
    public Double subtractMoney(long userId, Double money) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No existe el usuario con id " + userId));

        if (user.getAvailableMoney() < money) {
            throw new RuntimeException("El usuario con id " + userId + " no tiene dinero suficiente para pujar");
        }

        user.setAvailableMoney(user.getAvailableMoney() - money);

        return money;
    }
}
