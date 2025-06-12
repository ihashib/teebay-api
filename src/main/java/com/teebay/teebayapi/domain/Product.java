package com.teebay.teebayapi.domain;

import com.teebay.teebayapi.domain.enumeration.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Product {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    User owner;

    String title;

    String description;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    Set<Category> categories = new HashSet<>();
}
