package com.app.cms.entities;

import com.app.cms.dtos.ProductCreateDto;
import com.app.cms.helpers.CmsConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    private String brand;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, columnDefinition = "DOUBLE CHECK (price > 0.0)")
    private Double price;

    @Column(nullable = false, columnDefinition = "INT CHECK (quantity >= 0 AND quantity <= 1000)")
    private Integer quantity;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = CmsConstants.TIMEZONE)
    private Date dateAdded;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Kolkata")
    private Date lastUpdated;

    public Product(String name, String brand, String description, Double price, Integer quantity, String category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public void updateProduct(ProductCreateDto productCreateDto) {
        this.name = productCreateDto.getName();
        this.brand = productCreateDto.getBrand();
        this.description = productCreateDto.getDescription();
        this.price = productCreateDto.getPrice();
        this.quantity = productCreateDto.getQuantity();
        this.category = productCreateDto.getCategory();
        this.lastUpdated = new Date();
    }
}
