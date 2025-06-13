package com.teebay.teebayapi.service.dto;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.domain.enumeration.Category;
import com.teebay.teebayapi.domain.enumeration.PeriodUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private User owner;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Set<Category> categories;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal rentPrice;

    @NotNull
    private PeriodUnit rentUnit;
}
