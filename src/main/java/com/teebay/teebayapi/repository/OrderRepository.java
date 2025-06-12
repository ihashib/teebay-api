package com.teebay.teebayapi.repository;

import com.teebay.teebayapi.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByBuyerId(UUID buyerId);

    @Query("select order from product_order order where order.product.id = :pid and order.type = 'RENT' "
            + "and ((order.rentStart <= :to and order.rentEnd >= :from))")
    List<Order> findOverlappingRents(@Param("pid") UUID pid,
                                     @Param("from") Instant from,
                                     @Param("to") Instant to);
}
