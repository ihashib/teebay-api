package com.teebay.teebayapi.domain;

import com.teebay.teebayapi.domain.enumeration.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Entity(name="product_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User buyer;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    private Instant rentStart;

    private Instant rentEnd;

    private double totalPrice;
}

