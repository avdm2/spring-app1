package homework.spring.dto.auth;

import lombok.Data;

@Data
public class RegisterDto {

    private String email;
    private String username;
    private String password;
    private String userRole;
}
