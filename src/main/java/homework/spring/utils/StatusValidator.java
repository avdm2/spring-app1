package homework.spring.utils;

import homework.spring.domain.entity.OrderStatus;

public class StatusValidator {

    public static OrderStatus validate(String value) throws IllegalArgumentException {
        String statusStr = value.toUpperCase();
        if (!statusStr.equals("ACCEPTED") && !statusStr.equals("PENDING") &&
                !statusStr.equals("DONE") && !statusStr.equals("CANCELED")) {
            throw new IllegalArgumentException();
        }
        return OrderStatus.valueOf(statusStr);
    }
}
