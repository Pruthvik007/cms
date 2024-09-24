package com.app.cms.services;

import com.app.cms.dtos.ProductCreateDto;
import com.app.cms.dtos.ProductUpdateDto;
import com.app.cms.entities.Product;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<Product> getAllProducts(int pageNumber, String name, String brand, String category, Double minPrice, Double maxPrice);

    Product getProductById(Long id);

    Product createProduct(ProductCreateDto productCreateDto);

    Product updateProduct(ProductUpdateDto productUpdateDto);

    Product deleteProduct(Long id);
}
