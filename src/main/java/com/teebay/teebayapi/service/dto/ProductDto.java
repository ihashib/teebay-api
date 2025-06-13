package com.teebay.teebayapi.service.dto;

import com.teebay.teebayapi.domain.enumeration.Category;
import com.teebay.teebayapi.domain.enumeration.PeriodUnit;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID id;

    @NotBlank
    private String title;

    private String description;

    @Size(min = 1)
    private Set<@NotNull Category> categories;

    @NotNull
    private float price;

    @NotNull
    private float rentPrice;

    @NotNull
    private PeriodUnit rentUnit;
}
