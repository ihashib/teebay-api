package com.teebay.teebayapi.service;

import com.teebay.teebayapi.domain.Order;
import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.domain.enumeration.OrderType;
import com.teebay.teebayapi.domain.enumeration.UserType;
import com.teebay.teebayapi.exception.BadRequestException;
import com.teebay.teebayapi.exception.ForbiddenException;
import com.teebay.teebayapi.exception.InternalErrorException;
import com.teebay.teebayapi.repository.OrderRepository;
import com.teebay.teebayapi.service.dto.OrderRequestDto;
import com.teebay.teebayapi.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final UserService userService;
    private final OrderRepository orderRepository;

    Order orderProduct(OrderRequestDto orderRequestDto){
        log.info("Request to order: {}", orderRequestDto);

        // verify buyer and userType is user
        User buyer = userService.getUserById(orderRequestDto.getBuyerId());

        if(!buyer.getUserType().equals(UserType.USER)){
            log.warn("Admin cannot buy");
            throw new ForbiddenException("Admin cannot buy");
        }

        // verify product
        Product product = productService.getProductById(orderRequestDto.getProductId());

        // User cannot buy or rent their own product
        if(product.getOwner().getId().equals(buyer.getId())){
            log.warn("User cannot buy or rent their own product");
            throw new ForbiddenException("User cannot buy or rent their own product");
        }

        // Rent overlap validation
        if(orderRequestDto.getType().equals(OrderType.RENT)
            && orderRequestDto.getRentStart() != null
            && orderRequestDto.getRentEnd() != null
        ) {
            List<Order> overLappingRentOrders = orderRepository.findOverlappingRents(
                    product.getId(),
                    orderRequestDto.getRentStart(),
                    orderRequestDto.getRentEnd()
            );

            if(!overLappingRentOrders.isEmpty()){
                log.warn("User already rented this product");
                throw new BadRequestException("Product already rented by you, cannot rent this product");
            }
        }

        // create new order
        Order order = new Order()
                .setBuyer(buyer)
                .setProduct(product)
                .setType(orderRequestDto.getType());

        // save order
        try{
            log.info("Saving new order");
            return orderRepository.save(order);
        } catch (Exception e){
            log.warn("Unable to save new order");
            throw new InternalErrorException("Unable to save new order");
        }
    }

    List<Order> getBuyerAllOrders(UUID buyerId){
        log.info("Get buyer id: {} 's all orders", buyerId);

        // verify buyer
        User buyer = userService.getUserById(buyerId);

        // get buyer all orders
        return orderRepository.findByBuyerId(buyer.getId());
    }

    List<Order> getBuyerBoughtOrders(UUID buyerId){
        log.info("Get buyer id: {} 's bought orders", buyerId);

        // verify buyer
        User buyer = userService.getUserById(buyerId);

        // get buyer all bought orders
        return orderRepository.findByBuyerIdAndType(buyer.getId(), OrderType.BUY);
    }

    List<Order> getBuyerBorrowedOrders(UUID buyerId){
        log.info("Get buyer id: {} 's borrowed orders", buyerId);

        // verify buyer
        User buyer = userService.getUserById(buyerId);

        // get buyer all bought orders
        return orderRepository.findByBuyerIdAndType(buyer.getId(), OrderType.RENT);
    }

    List<Order> getOwnerAllOrders(UUID ownerId){
        log.info("Get all order for owner id: {}", ownerId);

        // verify owner
        User owner = userService.getUserById(ownerId);

        // get owner all orders
        return orderRepository.findByProductOwnerId(owner.getId());
    }

    List<Order> getOwnerSoldOrders(UUID ownerId){
        log.info("Get sold order for owner id: {}", ownerId);

        // verify owner
        User owner = userService.getUserById(ownerId);

        // get owner all orders
        return orderRepository.findByProductOwnerIdAndType(owner.getId(), OrderType.BUY);
    }

    List<Order> getOwnerLentOrders(UUID ownerId){
        log.info("Get lent order for owner id: {}", ownerId);

        // verify owner
        User owner = userService.getUserById(ownerId);

        // get owner all orders
        return orderRepository.findByProductOwnerIdAndType(owner.getId(), OrderType.RENT);
    }
}
