package homework.spring.service;

import homework.spring.domain.entity.Dish;
import homework.spring.domain.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAvailableDishes() {
        return dishRepository.findAll()
                .stream()
                .filter(dish -> dish.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
