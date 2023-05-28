package homework.spring.utils;

import homework.spring.domain.entity.UserRole;

public class RoleValidator {

    public static UserRole validate(String value) throws IllegalArgumentException {
        String roleStr = value.toUpperCase();
        if (!roleStr.equals("CUSTOMER") && !roleStr.equals("CHEF") && !roleStr.equals("MANAGER")) {
            throw new IllegalArgumentException();
        }
        return UserRole.valueOf(roleStr);
    }
}
