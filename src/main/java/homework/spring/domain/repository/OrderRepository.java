package homework.spring.domain.repository;

import homework.spring.domain.entity.Order;
import homework.spring.domain.entity.OrderStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @NotNull Optional<Order> findById(@NotNull Long id);
    List<Order> findByOrderStatus (OrderStatus orderStatus);
}
