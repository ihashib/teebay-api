package com.teebay.teebayapi.service;

import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.exception.BadRequestException;
import com.teebay.teebayapi.exception.ForbiddenException;
import com.teebay.teebayapi.exception.InternalErrorException;
import com.teebay.teebayapi.exception.NotFoundException;
import com.teebay.teebayapi.repository.ProductRepository;
import com.teebay.teebayapi.repository.UserRepository;
import com.teebay.teebayapi.service.dto.ProductDto;
import com.teebay.teebayapi.service.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Product createProduct(UUID ownerId, ProductDto productDto) throws BadRequestException {
        log.info("Creating new product: {}", productDto);

        // verify owner
        User owner = userRepository
            .findById(ownerId)
            .orElseThrow(() -> {
                log.warn("Owner not found");
                return new NotFoundException("Owner not found");
            });

        // Convert to entity
        Product product = productMapper.toEntity(productDto);

        // set owner
        product.setOwner(owner);

        try {
            return productRepository.save(product);
        } catch (Exception e){
            throw new InternalErrorException("Unable to save product");
        }
    }

    public Product updateProduct(UUID ownerId, ProductDto productDto) throws BadRequestException {
        log.info("Updating product: {}", productDto);

        Product updatedProduct = productMapper.toEntity(productDto);

        // check if product exists
        Product existingProduct = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // verify owner
        if(!existingProduct.getOwner().getId().equals(ownerId)){
            throw new ForbiddenException("Product owner mismatch");
        }

        // copy from updated to existing
        BeanUtils.copyProperties(updatedProduct, existingProduct, "owner");

        // save
        try {
            log.info("Update product id: {}", existingProduct.getId());
            return productRepository.save(existingProduct);
        } catch (Exception e){
            log.warn("Unable to update product, id: {}", existingProduct.getId());
            throw new InternalErrorException("Unable to update product");
        }
    }

    public boolean deleteProduct(UUID ownerId, UUID productId) throws BadRequestException {
        log.info("Deleting product: {}", productId);

        // check if product exists
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // verify owner
        if(!existingProduct.getOwner().getId().equals(ownerId)){
            throw new ForbiddenException("Product owner mismatch");
        }

        // delete by product id
        try {
            productRepository.deleteById(existingProduct.getId());

            log.info("product: {} deleted", existingProduct.getId());

            return true;
        } catch (Exception e){
            log.info("product: {} not deleted", existingProduct.getId());

            return false;
        }
    }

    public List<Product> getAllProducts() throws BadRequestException {
        log.info("Request to get all products");

        try {
            return productRepository.findAll();
        } catch (Exception e){
            log.warn("Unable to get all products");
            throw new InternalErrorException("Unable to get all products");
        }
    }

    public List<Product> getProductsByOwner(UUID ownerId) throws BadRequestException {
        log.info("Request to get {}'s all products", ownerId);

        try{
            return productRepository.findByOwnerId(ownerId);
        } catch (Exception e){
            log.warn("Unable to get {}'s all products", ownerId);
            throw new InternalErrorException("Unable to get all products for owner");
        }
    }
}
