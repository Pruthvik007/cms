package com.app.cms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.app.cms.dtos.ProductCreateDto;
import com.app.cms.dtos.ProductUpdateDto;
import com.app.cms.entities.Product;
import com.app.cms.exceptions.CmsException;
import com.app.cms.repositories.ProductRepository;
import com.app.cms.services.impl.UserServiceImpl;

@DisplayName("User Service Implementation Tests")
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Get All Products")
    void testGetAllProducts() {
        Product product1 = new Product("Product1", "Brand1", "Description1", 100.0, 10, "Category1");
        Product product2 = new Product("Product2", "Brand2", "Description2", 200.0, 20, "Category2");
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2));
        when(productRepository.getProductsByCategoryAndBrandAndNameAndPriceRange(any(), any(), any(), any(), any(),
                any())).thenReturn(productPage);
        Page<Product> result = userService.getAllProducts(0, null, null, null, null, null);
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(productRepository, times(1)).getProductsByCategoryAndBrandAndNameAndPriceRange(any(), any(), any(),
                any(), any(), any());
    }

    @Test
    @DisplayName("Test Get Product By ID")
    void testGetProductById() {
        Product product = new Product("Product1", "Brand1", "Description1", 100.0, 10, "Category1");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = userService.getProductById(1L);
        assertNotNull(result);
        assertEquals("Product1", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test Create Product")
    void testCreateProduct() {
        ProductCreateDto productCreateDto = new ProductCreateDto("Product1", "Brand1", "Description1", 100.0, 10,
                "Category1");
        Product savedProduct = new Product("Product1", "Brand1", "Description1", 100.0, 10, "Category1");
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        Product result = userService.createProduct(productCreateDto);
        assertNotNull(result);
        assertEquals("Product1", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test Update Product")
    void testUpdateProduct() {
        Product product = new Product("Product1", "Brand1", "Description1", 100.0, 10, "Category1");
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "UpdatedProduct", "UpdatedBrand",
                "UpdatedDescription", 150.0, 5, "UpdatedCategory");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product result = userService.updateProduct(productUpdateDto);
        assertNotNull(result);
        assertEquals("UpdatedProduct", result.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test Delete Product")
    void testDeleteProduct() {
        Product product = new Product("Product1", "Brand1", "Description1", 100.0, 10, "Category1");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = userService.deleteProduct(1L);
        assertNotNull(result);
        assertEquals("Product1", result.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    @DisplayName("Test Get Product By Invalid ID")
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CmsException.class, () -> userService.getProductById(1L));
        assertEquals("Product Not Found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test Update Product With Invalid ID")
    void testUpdateProductNotFound() {
        ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "UpdatedProduct", "UpdatedBrand",
                "UpdatedDescription", 150.0, 5, "UpdatedCategory");
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CmsException.class, () -> userService.updateProduct(productUpdateDto));
        assertEquals("Product Not Found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test Delete Product With Invalid ID")
    void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CmsException.class, () -> userService.deleteProduct(1L));
        assertEquals("Product Not Found", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }
}
