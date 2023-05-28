package homework.spring.controller;

import homework.spring.domain.entity.Dish;
import homework.spring.dto.restaurant.DishDto;
import homework.spring.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("menu")
    public ResponseEntity<String> menu() {
        List<Dish> menu = dishService.getAvailableDishes();
        if (menu.isEmpty()) {
            return new ResponseEntity<>("Блюда кончились!", HttpStatus.OK);
        }
        StringBuilder menuString = new StringBuilder("Доступные блюда:\n");
        for (Dish dish : menu) {
            menuString
                    .append("NAME = ").append(dish.getName())
                    .append("; DESCRIPTION = ").append(dish.getDescription())
                    .append("; PRICE = ").append(dish.getPrice())
                    .append("; QUANTITY = ").append(dish.getQuantity()).append(";\n");
        }
        return new ResponseEntity<>(menuString.toString(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getDishById(@PathVariable Long id) {
        Dish dish;
        try {
            dish = dishService.getDishById(id);
            return new ResponseEntity<>("Блюдо с ID " + id +
                    ":\n NAME = " + dish.getName() +
                    "; DESCRIPTION = " + dish.getDescription() +
                    "; PRICE = " + dish.getPrice() +
                    "; QUANTITY = " + dish.getQuantity() +
                    ";", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Некорректный ID!", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("create")
    @Transactional
    public ResponseEntity<String> createDish(@RequestBody DishDto dishDto) {
        if (dishDto.getName() == null || dishDto.getDescription() == null || dishDto.getQuantity() == null || dishDto.getPrice() == null) {
            return new ResponseEntity<>("Ошибка! Не все поля заполнены!", HttpStatus.BAD_REQUEST);
        }
        Dish dish = new Dish()
                .setName(dishDto.getName())
                .setDescription(dishDto.getDescription())
                .setQuantity(dishDto.quantity)
                .setPrice(dishDto.getPrice());
        dishService.saveDish(dish);
        return new ResponseEntity<>("Блюдо " + dish.getName() + " добавлено!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDish(@PathVariable Long id, @RequestBody DishDto dishDto) {
        if (dishDto.getName() == null || dishDto.getDescription() == null ||
                dishDto.getPrice() == null || dishDto.getQuantity() == null) {
            return new ResponseEntity<>("Ошибка! Не все поля заполнены!", HttpStatus.BAD_REQUEST);
        }
        Dish dish = dishService.getDishById(id);
        if (dish == null) {
            return new ResponseEntity<>("Ошибка! Блюда с таким ID не существует!", HttpStatus.BAD_REQUEST);
        }
        dish
                .setName(dishDto.getName())
                .setDescription(dishDto.getDescription())
                .setPrice(dishDto.getPrice())
                .setQuantity(dishDto.quantity);
        dishService.saveDish(dish);
        return new ResponseEntity<>("Теперь блюдо с ID " + id + " имеет следедующие характеристики:" +
                "\nNAME = " + dish.getName() +
                "; DESCRIPTION = " + dish.getDescription() +
                "; PRICE = " + dish.getPrice() +
                "; QUANTITY = " + dish.getQuantity() +
                ";", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deleteDish(@PathVariable Long id) {
        Dish dish;
        try {
            dish = dishService.getDishById(id);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Ошибка! Блюда с ID " + id + " не существует!", HttpStatus.BAD_REQUEST);
        }
        dishService.deleteDish(id);
        return new ResponseEntity<>("Блюдо " + dish.getName() + " было удалено!", HttpStatus.OK);
    }
}
