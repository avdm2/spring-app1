package homework.spring.service;

import homework.spring.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    public static String getInfo(User user) {
        return "ID = " + user.getId() + "; USERNAME = " + user.getUsername() + "; EMAIL = " + user.getEmail()
                + "; ROLE = " + user.getRole().toString() + "; CREATED_AT = " + user.getCreatedAt() + ";";
    }
}
