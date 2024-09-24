package com.app.cms.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.app.cms.dtos.ProductCreateDto;
import com.app.cms.dtos.ProductUpdateDto;
import com.app.cms.entities.Product;
import com.app.cms.pojos.Response;
import com.app.cms.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@DisplayName("User Controller Tests")
@SpringBootTest
public class UserControllerTest {

        private static final String PRODUCT_URL = "/cms/product";
        private static final String PRODUCTS_URL = "/cms/products";

        @MockBean
        private UserServiceImpl userService;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        @DisplayName("Test Get All Products")
        void testGetAllProducts() throws Exception {
                List<Product> productList = new ArrayList<>();
                Page<Product> productPage = new PageImpl<>(productList);
                when(userService.getAllProducts(0, null, null, null, null, null)).thenReturn(productPage);
                mockMvc.perform(get(PRODUCTS_URL))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(Response.Status.SUCCESS.name()));
                verify(userService, times(1)).getAllProducts(0, null, null, null, null, null);
        }

        @Test
        @DisplayName("Test Get Product by ID")
        void testGetProductById() throws Exception {
                Product product = new Product(1L, "Product A", "Brand A", "Description of Product A", 100.0, 10,
                                "Category A",
                                null, null);
                when(userService.getProductById(1L)).thenReturn(product);

                mockMvc.perform(get(PRODUCT_URL + "/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.name").value("Product A"))
                                .andExpect(jsonPath("$.status").value(Response.Status.SUCCESS.name()));
        }

        @Test
        @DisplayName("Test Create Product")
        void testCreateProduct_Valid() throws Exception {
                ProductCreateDto productCreateDto = new ProductCreateDto("Product A", "Brand A",
                                "Description of Product A",
                                100.0, 10, "Category A");
                Product product = new Product(1L, "Product A", "Brand A", "Description of Product A", 100.0, 10,
                                "Category A",
                                null, null);
                when(userService.createProduct(productCreateDto)).thenReturn(product);

                mockMvc.perform(post(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productCreateDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status").value(Response.Status.SUCCESS.name()));
        }

        @Test
        @DisplayName("Test Update Product")
        void testUpdateProduct_Valid() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Updated Product", "Updated Brand",
                                "Updated Description", 120.0, 15, "Updated Category");
                Product updatedProduct = new Product(1L, "Updated Product", "Updated Brand", "Updated Description",
                                120.0, 15,
                                "Updated Category", null, null);
                when(userService.updateProduct(productUpdateDto)).thenReturn(updatedProduct);

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productUpdateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Product updated successfully"))
                                .andExpect(jsonPath("$.status").value(Response.Status.SUCCESS.name()));
        }

        @Test
        @DisplayName("Test Delete Product")
        void testDeleteProduct() throws Exception {
                Long productId = 1L;
                Product product = new Product(productId, "Product A", "Brand A", "Description of Product A", 100.0, 10,
                                "Category A", null, null);
                when(userService.getProductById(productId)).thenReturn(product);
                when(userService.deleteProduct(productId)).thenReturn(product);

                mockMvc.perform(delete(PRODUCT_URL + "/" + productId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(Response.Status.SUCCESS.name()));
        }

        @Test
        @DisplayName("Test Update Product With ID Null")
        void testUpdateProduct_InvalidId_Null() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(null, "Valid Name", "Valid Brand",
                                "Valid Description",
                                100.0, 10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Product Id is Required"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Name Null")
        void testUpdateProduct_InvalidName_Null() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, null, "Valid Brand", "Valid Description",
                                100.0,
                                10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Product Name Cannot be Empty"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Name Too Short")
        void testUpdateProduct_InvalidName_TooShort() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "A", "Valid Brand", "Valid Description",
                                100.0, 10,
                                "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message")
                                                .value("Product Name must be between 2 and 50 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Name Too Long")
        void testUpdateProduct_InvalidName_TooLong() throws Exception {
                String longName = "A".repeat(51);
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, longName, "Valid Brand",
                                "Valid Description",
                                100.0, 10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message")
                                                .value("Product Name must be between 2 and 50 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Brand Null")
        void testUpdateProduct_InvalidBrand_Null() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", null, "Valid Description",
                                100.0, 10,
                                "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Brand Cannot be Empty"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Brand Too Short")
        void testUpdateProduct_InvalidBrand_TooShort() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "A", "Valid Description",
                                100.0, 10,
                                "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Brand must be between 2 and 30 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Brand Too Long")
        void testUpdateProduct_InvalidBrand_TooLong() throws Exception {
                String longBrand = "A".repeat(31);
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", longBrand,
                                "Valid Description",
                                100.0, 10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Brand must be between 2 and 30 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Description Null")
        void testUpdateProduct_InvalidDescription_Null() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand", null, 100.0,
                                10,
                                "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Description Cannot be Empty"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Description Too Short")
        void testUpdateProduct_InvalidDescription_TooShort() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand", "Short",
                                100.0, 10,
                                "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message")
                                                .value("Description must be between 10 and 200 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Description Too Long")
        void testUpdateProduct_InvalidDescription_TooLong() throws Exception {
                String longDescription = "A".repeat(201);
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                longDescription,
                                100.0, 10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message")
                                                .value("Description must be between 10 and 200 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Price Null")
        void testUpdateProduct_InvalidPrice_Null() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                null, 10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Price is Required"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product Negative Price")
        void testUpdateProduct_InvalidPrice_Negative() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                -50.0, 10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Price Must be Greater than Zero"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Too Many Digits In Price")
        void testUpdateProduct_InvalidPrice_TooManyDigits() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                12345678901.00, 10, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message")
                                                .value("Price Must be a Valid number with up to 2 Decimal Places"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product Negative Quantity")
        void testUpdateProduct_InvalidQuantity_Negative() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                100.0, -5, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Quantity Cannot Be Negative"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Quantity Exceeding Maximum")
        void testUpdateProduct_InvalidQuantity_ExceedsMaximum() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                100.0, 1001, "Valid Category");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Quantity cannot Exceed 1000"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Category Null")
        void testUpdateProduct_InvalidCategory_Null() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                100.0, 10, null);

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Category Cannot be Empty"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Category Too Short")
        void testUpdateProduct_InvalidCategory_TooShort() throws Exception {
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                100.0, 10, "A");

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Category must be between 3 and 30 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

        @Test
        @DisplayName("Test Update Product With Category Too Long")
        void testUpdateProduct_InvalidCategory_TooLong() throws Exception {
                String longCategory = "A".repeat(31);
                ProductUpdateDto productUpdateDto = new ProductUpdateDto(1L, "Valid Name", "Valid Brand",
                                "Valid Description",
                                100.0, 10, longCategory);

                mockMvc.perform(put(PRODUCT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productUpdateDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Category must be between 3 and 30 characters"))
                                .andExpect(jsonPath("$.status").value(Response.Status.FAILURE.toString()));
        }

}
