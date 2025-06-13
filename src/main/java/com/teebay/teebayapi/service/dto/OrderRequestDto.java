package com.teebay.teebayapi.service.dto;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.domain.enumeration.OrderType;
import jakarta.validation.constraints.NotNull;
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
public class OrderRequestDto {
    @NotNull
    private UUID productId;
    @NotNull
    private UUID buyerId;
    @NotNull
    private OrderType type;
    private Instant rentStart;
    private Instant rentEnd;
}
