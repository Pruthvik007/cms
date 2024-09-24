package com.app.cms.repositories;

import com.app.cms.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback
@DisplayName("Product Repository Tests")
public class ProductRepositoryTest {

        @Autowired
        private ProductRepository productRepository;

        @BeforeEach
        void setUp() {
                Product product1 = new Product(null, "Product A", "Brand A", "Description of Product A", 100.0, 10,
                                "Category A", null, null);
                Product product2 = new Product(null, "Product B", "Brand B", "Description of Product B", 200.0, 5,
                                "Category B",
                                null, null);
                Product product3 = new Product(null, "Product C", "Brand A", "Description of Product C", 150.0, 20,
                                "Category A", null, null);

                productRepository.save(product1);
                productRepository.save(product2);
                productRepository.save(product3);
        }

        @Test
        @DisplayName("Test Get Products by All Filters")
        public void testGetProductsByCategoryAndBrandAndNameAndPriceRangeAllFilters() {
                Pageable pageable = PageRequest.of(0, 10);

                Page<Product> result = productRepository.getProductsByCategoryAndBrandAndNameAndPriceRange(
                                "Product A", "Brand A", "Category A", 50.0, 150.0, pageable);

                assertEquals(1, result.getTotalElements());
                assertEquals("Product A", result.getContent().get(0).getName());
        }

        @Test
        @DisplayName("Test Get Products by Category")
        public void testGetProductsByCategory() {
                Pageable pageable = PageRequest.of(0, 10);

                Page<Product> result = productRepository.getProductsByCategoryAndBrandAndNameAndPriceRange(
                                null, null, "Category A", null, null, pageable);

                assertEquals(2, result.getTotalElements());
        }

        @Test
        @DisplayName("Test Get Products by Brand")
        public void testGetProductsByBrand() {
                Pageable pageable = PageRequest.of(0, 10);

                Page<Product> result = productRepository.getProductsByCategoryAndBrandAndNameAndPriceRange(
                                null, "Brand A", null, null, null, pageable);

                assertEquals(2, result.getTotalElements());
        }

        @Test
        @DisplayName("Test Get Products by Price Range")
        public void testGetProductsByPriceRange() {
                Pageable pageable = PageRequest.of(0, 10);

                Page<Product> result = productRepository.getProductsByCategoryAndBrandAndNameAndPriceRange(
                                null, null, null, 100.0, 200.0, pageable);

                assertEquals(3, result.getTotalElements());
        }

        @Test
        @DisplayName("Test Get Products When No Matches Found")
        public void testGetProductsNoMatches() {
                Pageable pageable = PageRequest.of(0, 10);

                Page<Product> result = productRepository.getProductsByCategoryAndBrandAndNameAndPriceRange(
                                "Nonexistent Product", "Brand C", "Category C", 300.0, 400.0, pageable);

                assertEquals(0, result.getTotalElements());
        }
}
