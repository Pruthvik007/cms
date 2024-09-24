package com.app.cms.repositories;

import com.app.cms.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:brand IS NULL OR p.brand LIKE %:brand%) " +
            "AND (:category IS NULL OR p.category LIKE %:category%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> getProductsByCategoryAndBrandAndNameAndPriceRange(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);
}
