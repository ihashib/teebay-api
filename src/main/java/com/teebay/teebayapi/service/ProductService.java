package com.teebay.teebayapi.service;

import com.teebay.teebayapi.domain.Product;
import com.teebay.teebayapi.repository.ProductRepository;
import com.teebay.teebayapi.repository.UserRepository;
import com.teebay.teebayapi.service.dto.ProductDto;
import com.teebay.teebayapi.service.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Product createProduct(ProductDto productDto) throws BadRequestException {
        log.info("Creating new product: {}", productDto);

        // verify owner
        if(!ObjectUtils.isEmpty(productDto.getOwner())){
            userRepository
                    .findById(productDto.getOwner().getId())
                    .orElseThrow(() -> {
                        log.warn("Owner not found");

                        return new BadRequestException("Owner not found");
                    });
        } else {
            throw new BadRequestException("Owner is null");
        }

        // Convert to entity
        Product product = productMapper.toEntity(productDto);

        try {
            return productRepository.save(product);
        } catch (Exception e){
            throw new InternalException("Unable to save product");
        }
    }

    public Product updateProduct(ProductDto productDto) throws BadRequestException {
        log.info("Updating product: {}", productDto);

        Product updatedProduct = productMapper.toEntity(productDto);

        Product existingProduct = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new Not
        BeanUtils.copyProperties(tag, existingTag, Utils.getIgnoreProperties());
        if (tagDTO.getStatus() != null) {
            existingTag.setStatus(tagDTO.getStatus());
        }
        tag = tagRepository.save(existingTag);
        return tagMapper.toDto(tag, localeConfig);
    }

    public boolean deleteProduct(ProductDto productDto) throws BadRequestException {
        log.info("Deleting product: {}", productDto);

    }

    public Set<Product> getAllProducts() throws BadRequestException {
        log.info("Request to get all products");

    }

    public Set<Product> getProductsByOwner(UUID ownerId) throws BadRequestException {
        log.info("Request to get {}'s all products", ownerId);

    }
}
