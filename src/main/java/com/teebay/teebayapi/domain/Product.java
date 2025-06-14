package com.teebay.teebayapi.domain;

import com.teebay.teebayapi.domain.enumeration.Category;
import com.teebay.teebayapi.domain.enumeration.PeriodUnit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
    name = "products",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_products_owner_title",
        columnNames = { "owner_id", "title" }
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
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
    private double price;

    @Column(nullable = false, name="rent_price")
    private double rentPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="rent_unit")
    private PeriodUnit rentUnit;
}
