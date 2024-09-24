package com.app.cms.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {

    @NotBlank(message = "Product Name Cannot be Empty")
    @Size(min = 2, max = 50, message = "Product Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Brand Cannot be Empty")
    @Size(min = 2, max = 30, message = "Brand must be between 2 and 30 characters")
    private String brand;

    @NotBlank(message = "Description Cannot be Empty")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters")
    private String description;

    @NotNull(message = "Price is Required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price Must be Greater than Zero")
    @Digits(integer = 10, fraction = 2, message = "Price Must be a Valid number with up to 2 Decimal Places")
    private Double price;

    @NotNull(message = "Quantity is Required")
    @Min(value = 0, message = "Quantity Cannot Be Negative")
    @Max(value = 1000, message = "Quantity cannot Exceed 1000")
    private Integer quantity;

    @NotBlank(message = "Category Cannot be Empty")
    @Size(min = 3, max = 30, message = "Category must be between 3 and 30 characters")
    private String category;
}
