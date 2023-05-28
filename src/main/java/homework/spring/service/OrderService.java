package homework.spring.service;

import homework.spring.domain.entity.Order;
import homework.spring.domain.entity.OrderStatus;
import homework.spring.domain.entity.User;
import homework.spring.domain.repository.OrderRepository;
import homework.spring.dto.restaurant.OrderCreationDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(OrderCreationDto orderCreationDto, User user, OrderStatus orderStatus) {

        Order order = new Order()
                .setUser(user)
                .setOrderStatus(orderStatus)
                .setSpecialRequests(orderCreationDto.getSpecialRequests())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    public Order findById(Long id) throws IllegalArgumentException {
        return orderRepository
                .findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)  // 5 Minutes
    @Transactional
    public void processOrders() {
        Random random = new Random();
        if (random.nextBoolean()) {
            // Изменение статуса всех заказов "В процессе" на статус "Готово"
            List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.PENDING);
            for (Order order : orders) {
                order.setOrderStatus(OrderStatus.DONE);
                orderRepository.save(order);
            }
        } else {
            // Смена статуса случайного неготового заказа на "Отменен"
            List<Order> orders = Stream.concat(
                            orderRepository.findByOrderStatus(OrderStatus.ACCEPTED).stream(),
                            orderRepository.findByOrderStatus(OrderStatus.PENDING).stream())
                    .toList();

            if (!orders.isEmpty()) {
                Order randomOrder = orders.get(random.nextInt(orders.size()));
                randomOrder.setOrderStatus(OrderStatus.CANCELED);
                orderRepository.save(randomOrder);
            }
        }
    }
}
