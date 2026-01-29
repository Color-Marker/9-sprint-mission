package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserLoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User login(UserLoginDto userLoginDto){
        return userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(userLoginDto.username()))
                .filter(u->u.getPassword().equals(userLoginDto.password()))
                .findAny()
                .orElseThrow(()->new NoSuchElementException("No user found"));
    }
}
