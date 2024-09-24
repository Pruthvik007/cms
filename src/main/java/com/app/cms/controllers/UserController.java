package com.app.cms.controllers;

import com.app.cms.dtos.ProductCreateDto;
import com.app.cms.dtos.ProductUpdateDto;
import com.app.cms.entities.Product;
import com.app.cms.pojos.PageResponse;
import com.app.cms.pojos.Response;
import com.app.cms.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cms")
@Tag(name = "Products API", description = "Operations Related to Products")
public class UserController {

    private final UserService userService;

    // Get All Products (Paginated) and Filter by Name, Brand, Category or Price range
    @GetMapping("/products")
    public ResponseEntity<PageResponse<List<Product>>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        Page<Product> productsPage = userService.getAllProducts(pageNumber, name, brand, category, minPrice, maxPrice);

        PageResponse<List<Product>> response = PageResponse.<List<Product>>builder()
                .data(productsPage.getContent())
                .pageNo(productsPage.getNumber())
                .totalPages(productsPage.getTotalPages())
                .status(Response.Status.SUCCESS)
                .build();

        return ResponseEntity.ok(response);
    }


    // Get a product by ID
    @GetMapping("/product/{id}")
    public ResponseEntity<Response<Product>> getProductById(@PathVariable Long id) {
        Product product = userService.getProductById(id);

        Response<Product> response = Response.<Product>builder()
                .data(product)
                .status(Response.Status.SUCCESS)
                .build();

        return ResponseEntity.ok(response);
    }

    // Create a New Product
    @PostMapping("/product")
    public ResponseEntity<Response<Product>> createProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        Product product = userService.createProduct(productCreateDto);

        Response<Product> response = Response.<Product>builder()
                .data(product)
                .status(Response.Status.SUCCESS)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update an Existing Product
    @PutMapping("/product")
    public ResponseEntity<Response<Product>> updateProduct(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        Product updatedProduct = userService.updateProduct(productUpdateDto);

        Response<Product> response = Response.<Product>builder()
                .data(updatedProduct)
                .message("Product updated successfully")
                .status(Response.Status.SUCCESS)
                .build();

        return ResponseEntity.ok(response);
    }

    // Delete a Product
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Response<Product>> deleteProduct(@PathVariable Long id) {
        Response<Product> response = Response.<Product>builder()
                .data(userService.deleteProduct(id))
                .status(Response.Status.SUCCESS)
                .build();
        return ResponseEntity.ok(response);
    }
}

