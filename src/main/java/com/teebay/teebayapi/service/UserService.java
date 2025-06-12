package com.teebay.teebayapi.service;

import com.sun.jdi.InternalException;
import com.teebay.teebayapi.domain.User;
import com.teebay.teebayapi.service.dto.LoginDto;
import com.teebay.teebayapi.service.dto.UserDto;
import com.teebay.teebayapi.repository.UserRepository;
import com.teebay.teebayapi.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User register(UserDto userDto) throws BadRequestException {
        log.info("Starting registration for {}", userDto);

        // check if user already exists
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.warn("User already exists for email: {}", userDto.getEmail());
            throw new BadRequestException("Email already in use");
        }

        // convert dto to entity
        User user = userMapper.toUser(userDto);

        // save new user
        try {
            log.info("saving new user {}", user);

            return userRepository.save(user);
        } catch (Exception e){
            log.warn("unable to save new user {}", user);

            throw new InternalException("Unable to save new user");
        }
    }

    public String login(LoginDto loginDto) throws BadRequestException {
        log.info("logging in {}", loginDto);

        // check email if exists
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> {
                    log.warn("user does not exist with {}", loginDto.getEmail());

                    return new BadRequestException("Wrong email or password");
                });

        // string match password (security not a requirement)
        if(!user.getPassword().equals(loginDto.getPassword())){
            log.warn("Wrong email or password");

            throw new BadRequestException("Wrong email or password");
        }

        log.info("Login successful for {}", loginDto);

        return user.getId().toString();
    }
}
