package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatus create(UserStatusCreateDto dto){
        if(!userRepository.existsById(dto.userId())){
            throw new NoSuchElementException("Can't find user");
        }
        if(userStatusRepository.findByUserId(dto.userId()).isPresent()){
            throw new IllegalArgumentException("Already exist");
        }
        UserStatus userStatus = new UserStatus(dto.userId());
        return userStatusRepository.save(userStatus);
    }

    public UserStatus find(UUID id){
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User status with id " + id + " not found"));
    }

    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }

    public UserStatus update(UserStatusUpdateDto dto){
        UserStatus userStatus = userStatusRepository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("User status with id " + dto.id() + " not found"));
        userStatus.update(dto.isOnline());
        return userStatusRepository.save(userStatus);
    }

    public UserStatus updateByUserId(UUID userId, UserStatusUpdateDto dto){
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User status with id " + userId + " not found"));
        userStatus.update(dto.isOnline());
        return userStatusRepository.save(userStatus);
    }

    public void delete(UUID id){
        if(!userStatusRepository.existsById(id)){
            throw new NoSuchElementException("Can't find user status");
        }
        userStatusRepository.deleteById(id);

    }

}
