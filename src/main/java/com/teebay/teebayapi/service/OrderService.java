package com.teebay.teebayapi.service;

import com.teebay.teebayapi.domain.Order;
import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.domain.enumeration.OrderType;
import com.teebay.teebayapi.domain.enumeration.PeriodUnit;
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

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    public Order orderProduct(OrderRequestDto orderRequestDto){
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
        validateRentOverlap(orderRequestDto, product);

        // calculate total price
        double totalPrice = calculateTotalPrice(orderRequestDto, product);

        // create new order
        Order order = new Order()
                .setBuyer(buyer)
                .setProduct(product)
                .setType(orderRequestDto.getType())
                .setRentStart(orderRequestDto.getRentStart())
                .setRentEnd(orderRequestDto.getRentEnd())
                .setTotalPrice(totalPrice);

        // save order
        try{
            log.info("Saving new order");
            return orderRepository.save(order);
        } catch (Exception e){
            log.warn("Unable to save new order");
            throw new InternalErrorException("Unable to save new order");
        }
    }

    public List<Order> getBuyerAllOrders(UUID buyerId){
        log.info("Get buyer id: {} 's all orders", buyerId);

        // verify buyer
        User buyer = userService.getUserById(buyerId);

        // get buyer all orders
        return orderRepository.findByBuyerId(buyer.getId());
    }

    public List<Order> getBuyerBoughtOrders(UUID buyerId){
        log.info("Get buyer id: {} 's bought orders", buyerId);

        // verify buyer
        User buyer = userService.getUserById(buyerId);

        // get buyer all bought orders
        return orderRepository.findByBuyerIdAndType(buyer.getId(), OrderType.BUY);
    }

    public List<Order> getBuyerBorrowedOrders(UUID buyerId){
        log.info("Get buyer id: {} 's borrowed orders", buyerId);

        // verify buyer
        User buyer = userService.getUserById(buyerId);

        // get buyer all bought orders
        return orderRepository.findByBuyerIdAndType(buyer.getId(), OrderType.RENT);
    }

    public List<Order> getOwnerAllOrders(UUID ownerId){
        log.info("Get all order for owner id: {}", ownerId);

        // verify owner
        User owner = userService.getUserById(ownerId);

        // get owner all orders
        return orderRepository.findByProductOwnerId(owner.getId());
    }

    public List<Order> getOwnerSoldOrders(UUID ownerId){
        log.info("Get sold order for owner id: {}", ownerId);

        // verify owner
        User owner = userService.getUserById(ownerId);

        // get owner all orders
        return orderRepository.findByProductOwnerIdAndType(owner.getId(), OrderType.BUY);
    }

    public List<Order> getOwnerRentedOrders(UUID ownerId){
        log.info("Get rented order for owner id: {}", ownerId);

        // verify owner
        User owner = userService.getUserById(ownerId);

        // get owner all orders
        return orderRepository.findByProductOwnerIdAndType(owner.getId(), OrderType.RENT);
    }

    private void validateRentOverlap(OrderRequestDto orderRequestDto, Product product) {
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
    }

    private double calculateTotalPrice(OrderRequestDto orderRequestDto, Product product) {
        double totalPrice;
        if (orderRequestDto.getType() == OrderType.BUY) {
            // if buying, then straight forward
            totalPrice = product.getPrice();
        } else if (orderRequestDto.getType() == OrderType.RENT) {
            // if renting, then calculation required

            Instant start = orderRequestDto.getRentStart();
            Instant end = orderRequestDto.getRentEnd();

            if (start == null || end == null || !start.isBefore(end)) {
                throw new BadRequestException("Invalid rent period");
            }

            long units = switch (product.getRentUnit()) {
                case HOUR -> Duration.between(start, end).toHours();
                case DAY -> Duration.between(start, end).toDays();
                case WEEK -> Duration.between(start, end).toDays() / 7;
                case MONTH -> ChronoUnit.MONTHS.between(
                    start.atZone(ZoneId.systemDefault()).toLocalDate(),
                    end.atZone(ZoneId.systemDefault()).toLocalDate()
                );
            };

            totalPrice = product.getRentPrice() * units;
        } else {
            throw new BadRequestException("Unsupported order type");
        }

        log.debug("Calculated totalPrice={} for orderType={} on product={}",
                totalPrice, orderRequestDto.getType(), product.getId()
        );

        return totalPrice;
    }
}
