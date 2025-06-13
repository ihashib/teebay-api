package com.teebay.teebayapi.domain;

import com.teebay.teebayapi.domain.enumeration.Category;
import com.teebay.teebayapi.domain.enumeration.PeriodUnit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    private String description;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="product_categories",
            joinColumns=@JoinColumn(name="product_id"))
    @Column(name="category")
    private Set<Category> categories;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, name="rent_price")
    private BigDecimal rentPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="rent_unit")
    private PeriodUnit rentUnit;
}
