package homework.spring.dto.restaurant;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishDto {
    public String name;
    public String description;
    public BigDecimal price;
    public Integer quantity;
}
