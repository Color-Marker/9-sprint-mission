package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserFindResDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserCreateDto userCreateDto) {
        List<User> allUser = userRepository.findAll();
        allUser.stream()
                .filter(p -> p.getUsername().equals(userCreateDto.username()))
                .findAny()
                .ifPresent(p->{
                    throw new IllegalArgumentException("Already existing user name: " + userCreateDto.username());
                });
        allUser.stream()
                .filter(p -> p.getEmail().equals(userCreateDto.email()))
                .findAny()
                .ifPresent(p->{
                    throw new IllegalArgumentException("Already existing email: " + userCreateDto.email());
                });
        User user = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password());
        UserStatus userStatus = new UserStatus(user.getId());
        BinaryContentCreateDto binaryContentDto = userCreateDto.profile();
        userStatusRepository.save(userStatus);
        if(binaryContentDto!=null){
            binaryContentRepository.save(binaryContentDto);
        }
        return userRepository.save(user);
    }

    @Override
    public UserFindResDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        return new UserFindResDto(user.getUsername(),user.getEmail(), userStatus.isOnline());
    }

    @Override
    public List<UserFindResDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        List<UserFindResDto> results = new ArrayList<>();
        for(User u: users){
            UserStatus userStatus = userStatuses.stream()
                    .filter(us -> us.getUserId().equals(u.getId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            UserFindResDto data = new UserFindResDto(u.getUsername(), u.getEmail(), userStatus.isOnline());
            results.add(data);
        }
        return results;
    }

    @Override
    public User update(UserUpdateDto userDto) {
        User user = userRepository.findById(userDto.userId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + userDto.userId() + " not found"));
        user.update(userDto.username(), userDto.email(), userDto.password());
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        if(userRepository.findById(userId).isPresent()){
            UUID profileId = userRepository.findById(userId).get().getProfileId();
            binaryContentRepository.deleteById(profileId);
            userStatusRepository.deleteByUserId(userId);
            userRepository.deleteById(userId);
        }

    }
}
