package com.teebay.teebayapi.service.dto;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.domain.enumeration.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDto {
    private UUID id;
    private ProductDto productDto;
    private User buyer;
    private OrderType type;
    private Instant rentStart;
    private Instant rentEnd;
}

