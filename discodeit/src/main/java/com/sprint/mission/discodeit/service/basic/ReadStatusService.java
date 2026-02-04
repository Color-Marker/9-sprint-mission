package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatus create(ReadStatusCreateDto dto){
        if(!channelRepository.existsById(dto.channelId())){
            throw new NoSuchElementException("Can't find channel");
        }
        if(!userRepository.existsById(dto.userId())){
            throw new NoSuchElementException("Can't find user");
        }
        if(readStatusRepository.findByChannelAndUser(dto.channelId(), dto.userId()).isPresent()){
            throw new IllegalArgumentException("Already existing read status");
        }
        ReadStatus readStatus = new ReadStatus(dto.userId(), dto.channelId());
        return readStatusRepository.save(readStatus);
    }

    public ReadStatus find(UUID id){
        if(readStatusRepository.findById(id).isEmpty()){
            throw new NoSuchElementException("Can't find read status");
        }
        return readStatusRepository.findById(id).get();
    }

    public List<ReadStatus> findAllByUserId(UUID userId){
        return readStatusRepository.findAll().stream()
                .filter(r -> r.getUserId().equals(userId))
                .toList();
    }

    public ReadStatus update(ReadStatusUpdateDto dto){
        if(readStatusRepository.findById(dto.id()).isEmpty()){
            throw new NoSuchElementException("Can't find read status");
        }
        ReadStatus readStatus = readStatusRepository.findById(dto.id()).get();
        readStatus.update(dto.isRead());
        return readStatusRepository.save(readStatus);
    }

    public void delete(UUID id){
        if(!readStatusRepository.existsById(id)){
            throw new NoSuchElementException("Can't find read status");
        }
        readStatusRepository.deleteById(id);
    }

}
