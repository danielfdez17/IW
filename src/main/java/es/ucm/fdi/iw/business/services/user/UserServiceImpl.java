package es.ucm.fdi.iw.business.services.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    

}
