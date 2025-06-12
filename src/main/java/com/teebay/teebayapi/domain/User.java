package com.teebay.teebayapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue
    UUID id;

    @Column(unique=true)
    String username;

    String passwordHash;
}
