package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelFindResDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.PublicChannelDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel createPublicChannel(PublicChannelDto publicChannelDto) {
        Channel channel = new Channel(publicChannelDto.type(), publicChannelDto.name(), publicChannelDto.description());
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelDto privateChannelDto) {
        Channel channel = new Channel(privateChannelDto.type(), null,null);
        Channel saved = channelRepository.save(channel);
        privateChannelDto.userIdList()
                .forEach(u->{
                    ReadStatus readStatus = new ReadStatus(u, saved.getId());
                    readStatusRepository.save(readStatus);
                });

        return saved;
    }

    @Override
    public ChannelFindResDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find channel"));
        List<Message> messages = messageRepository.findAll().stream()
                        .filter(m->m.getChannelId().equals(channelId)).toList();
        Instant lastMessageTime = messages.stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder()).orElse(null);
        if(channel.getType().equals(ChannelType.PRIVATE)){
            List<UUID> userIdList = readStatusRepository.findUserIdByChannelId(channelId);
            return new ChannelFindResDto(channel, lastMessageTime, userIdList);
        }
        else{
            return new ChannelFindResDto(channel, lastMessageTime, null);
        }

    }

    @Override
    public List<ChannelFindResDto> findAllByUserId(UUID userId) {
        List<UUID> publicChannelIdList = channelRepository.findAll().stream()
                .filter(c->c.getType().equals(ChannelType.PUBLIC))
                .map(Channel::getId)
                .toList();
        List<UUID> myPrivateChannelIdList = readStatusRepository.findChannelIdByUserId(userId);
        List<UUID> myPrivateChannelOnlyList = myPrivateChannelIdList.stream()
                .filter(i->!publicChannelIdList.contains(i))
                .toList();
        List<ChannelFindResDto> result = new ArrayList<>();
        for(UUID u:publicChannelIdList){
            result.add(find(u));
        }
        for(UUID u:myPrivateChannelOnlyList){
            result.add(find(u));
        }
        return result;
    }

    @Override
    public Channel update(ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelRepository.findById(channelUpdateDto.channelId())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelUpdateDto.channelId() + " not found"));
        if(channel.getType().equals(ChannelType.PRIVATE)) {
            return channelRepository.save(channel);
        }
        channel.update(channelUpdateDto.newName(), channelUpdateDto.newDescription());
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        List<ReadStatus> targetReadStatus = readStatusRepository.findAll().stream()
                .filter(r->r.getChannelId().equals(channelId))
                .toList();
        for(ReadStatus r:targetReadStatus){
            readStatusRepository.deleteById(r.getId());
        }
        List<Message> targetMessage = messageRepository.findAll().stream()
                        .filter(m->m.getChannelId().equals(channelId)).toList();
        for(Message m:targetMessage){
            messageRepository.deleteById(m.getId());
        }
        channelRepository.deleteById(channelId);
    }
}
