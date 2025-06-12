package com.teebay.teebayapi.graphql;

import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.service.UserService;
import com.teebay.teebayapi.service.dto.LoginDto;
import com.teebay.teebayapi.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MutationResolver {
    private final UserService userService;

    @MutationMapping
    public User register(@Argument UserDto userDto) throws BadRequestException {
        log.info("Register mutation called");

        return userService.register(userDto);
    }

    @MutationMapping
    public String login(@Argument LoginDto loginDto) throws BadRequestException {
        log.info("Login mutation called for email: {}", loginDto.getEmail());

        return userService.login(loginDto);
    }
}
