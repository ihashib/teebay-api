package com.teebay.teebayapi.service.dto;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.domain.enumeration.Category;
import com.teebay.teebayapi.domain.enumeration.PeriodUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductDto {
    private UUID id;

    private User owner;

    private String title;

    private String description;

    private Set<Category> categories;

    private BigDecimal price;

    private BigDecimal rentPrice;

    private PeriodUnit rentUnit;
}
