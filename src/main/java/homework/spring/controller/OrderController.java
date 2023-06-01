package homework.spring.controller;

import homework.spring.domain.entity.Order;
import homework.spring.domain.entity.OrderStatus;
import homework.spring.domain.entity.User;
import homework.spring.dto.restaurant.OrderCreationDto;
import homework.spring.service.OrderService;
import homework.spring.service.UserService;
import homework.spring.utils.StatusValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("create")
    @Transactional
    public ResponseEntity<String> createOrder(@RequestBody OrderCreationDto orderCreationDto) {
        if (orderCreationDto.getUsername() == null || orderCreationDto.getSpecialRequests() == null ||
                orderCreationDto.getStatus() == null) {
            return new ResponseEntity<>("Ошибка! Не все поля заполнены!", HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByUsername(orderCreationDto.getUsername());
        if (user == null) {
            return new ResponseEntity<>("Пользователь с таким ником не найден!", HttpStatus.BAD_REQUEST);
        } else {
            OrderStatus orderStatus;
            try {
                orderStatus = StatusValidator.validate(orderCreationDto.getStatus());
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>("Некорректный статус заказа!", HttpStatus.BAD_REQUEST);
            }

            orderService.saveOrder(orderCreationDto, user, orderStatus);
            return new ResponseEntity<>("Заказ создан успешно!", HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrder(@PathVariable Long id) {
        try {
            Order order = orderService.findById(id);
            return new ResponseEntity<>(
                    "USERNAME = " + order.getUser().getUsername() +
                    "; STATUS = " + order.getOrderStatus() +
                    "; REQUESTS = " + order.getSpecialRequests() +
                    ";", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Некорректный ID заказа!", HttpStatus.BAD_REQUEST);
        }
    }
}
