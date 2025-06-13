package com.teebay.teebayapi.controller;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.service.UserService;
import com.teebay.teebayapi.service.dto.LoginDto;
import com.teebay.teebayapi.service.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MutationResolver {
    private final UserService userService;

    @MutationMapping
    public User register(@Argument("input") @Valid UserDto userDto) throws BadRequestException {
        log.info("Register mutation called");

        return userService.register(userDto);
    }

    @MutationMapping
    public String login(@Argument("input") @Valid LoginDto loginDto) throws BadRequestException {
        log.info("Login mutation called for email: {}", loginDto.getEmail());

        return userService.login(loginDto);
    }
}
