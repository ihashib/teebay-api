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
    UUID id;

    @ManyToOne
    Product product;

    @ManyToOne
    User buyer;

    @Enumerated(EnumType.STRING)
    OrderType type;

    Instant rentStart;

    Instant rentEnd;
}

