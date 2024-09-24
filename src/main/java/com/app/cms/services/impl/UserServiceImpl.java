package com.app.cms.services.impl;

import com.app.cms.dtos.ProductCreateDto;
import com.app.cms.dtos.ProductUpdateDto;
import com.app.cms.entities.Product;
import com.app.cms.exceptions.CmsException;
import com.app.cms.helpers.CmsConstants;
import com.app.cms.repositories.ProductRepository;
import com.app.cms.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ProductRepository productRepository;

    @Override
    public Page<Product> getAllProducts(int pageNumber, String name, String brand, String category, Double minPrice,
            Double maxPrice) {
        return productRepository.getProductsByCategoryAndBrandAndNameAndPriceRange(name, brand, category, minPrice,
                maxPrice, PageRequest.of(pageNumber, CmsConstants.PAGE_SIZE));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CmsException("Product Not Found"));
    }

    @Override
    @Transactional
    public Product createProduct(ProductCreateDto productCreateDto) {
        return productRepository.save(
                new Product(productCreateDto.getName(), productCreateDto.getBrand(), productCreateDto.getDescription(),
                        productCreateDto.getPrice(), productCreateDto.getQuantity(), productCreateDto.getCategory()));
    }

    @Override
    @Transactional
    public Product updateProduct(ProductUpdateDto productUpdateDto) {
        Product product = getProductById(productUpdateDto.getId());
        product.updateProduct(productUpdateDto);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
        return product;
    }
}
