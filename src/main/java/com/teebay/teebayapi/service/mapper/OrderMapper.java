package com.teebay.teebayapi.service.mapper;

import com.teebay.teebayapi.domain.Order;
import com.teebay.teebayapi.service.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderDto orderDto);

    OrderDto toDto(Order order);
}
