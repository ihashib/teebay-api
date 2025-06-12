package com.teebay.teebayapi.service;

import com.sun.jdi.InternalException;
import com.teebay.teebayapi.domain.User;
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
}
