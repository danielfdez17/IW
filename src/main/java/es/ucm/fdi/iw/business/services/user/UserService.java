package es.ucm.fdi.iw.business.services.user;

public interface UserService {
    boolean disableUser(long id);
    boolean enableUser(long id);
}
