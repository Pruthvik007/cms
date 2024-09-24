package com.app.cms.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class ProductUpdateDto extends ProductCreateDto {
    @NotNull(message = "Product Id is Required")
    private Long id;

    public ProductUpdateDto(Long id, String name, String brand, String description, Double price, Integer quantity, String category) {
        super(name, brand, description, price, quantity, category);
        this.id = id;
    }
}
