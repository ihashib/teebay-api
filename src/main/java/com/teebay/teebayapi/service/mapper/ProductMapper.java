package com.teebay.teebayapi.service.mapper;

import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.service.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);
}
