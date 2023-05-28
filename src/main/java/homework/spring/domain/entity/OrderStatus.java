package homework.spring.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ACCEPTED,
    PENDING,
    DONE,
    CANCELED
}
