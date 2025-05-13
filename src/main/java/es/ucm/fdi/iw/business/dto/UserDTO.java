package es.ucm.fdi.iw.business.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private String roles;
    private double availableMoney;
}
