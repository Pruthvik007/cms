package com.app.cms.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductDto {

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotBlank(message = "Brand cannot be empty")
    private String brand;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Price is required and cannot be null")
    private Double price;

    @NotNull(message = "Quantity is required and cannot be null")
    private Integer quantity;

    @NotBlank(message = "Category cannot be empty")
    private String category;
}