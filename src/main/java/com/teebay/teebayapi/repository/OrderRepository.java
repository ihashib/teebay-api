package com.teebay.teebayapi.repository;

import com.teebay.teebayapi.domain.Order;
import com.teebay.teebayapi.domain.enumeration.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByBuyerId(UUID buyerId);

    @Query("select order from product_order order where order.product.id = :productId and order.type = 'RENT' "
            + "and ((order.rentStart <= :to and order.rentEnd >= :from))")
    List<Order> findOverlappingRents(@Param("pid") UUID productId,
                                     @Param("from") Instant from,
                                     @Param("to") Instant to);

    List<Order> findByBuyerIdAndType(UUID buyerId, OrderType type);

    List<Order> findByProductOwnerId(UUID ownerId);

    List<Order> findByProductOwnerIdAndType(UUID ownerId, OrderType type);
}
