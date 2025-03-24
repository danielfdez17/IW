package es.ucm.fdi.iw.business.dto;

import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private String roles;
}
