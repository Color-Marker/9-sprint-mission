package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.MessageCreateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    //
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateDto messageCreateDto) {
        if (!channelRepository.existsById(messageCreateDto.channelId())) {
            throw new NoSuchElementException("Channel not found with id " + messageCreateDto.channelId());
        }
        if (!userRepository.existsById(messageCreateDto.authorId())) {
            throw new NoSuchElementException("Author not found with id " + messageCreateDto.authorId());
        }
        List<BinaryContent> binaryContent = messageCreateDto.files();

        if(binaryContent != null){
            for(BinaryContent b:binaryContent){
                BinaryContentCreateDto dto = new BinaryContentCreateDto(b.getFileName(),b.getContentType(),b.getSize());
                binaryContentRepository.save(dto);
            }
            List<UUID> binaryContentIdList = binaryContent.stream()
                    .map(BinaryContent::getId)
                    .toList();
            Message message = new Message(messageCreateDto.content(), messageCreateDto.channelId(), messageCreateDto.authorId(), binaryContentIdList);
        }
        Message message = new Message(messageCreateDto.content(), messageCreateDto.channelId(), messageCreateDto.authorId(), null);

        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public Message update(MessageUpdateDto messageUpdateDto) {
        Message message = messageRepository.findById(messageUpdateDto.messageId())
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageUpdateDto.messageId() + " not found"));
        message.update(messageUpdateDto.newContent());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        List<UUID> attachmentIdList = messageRepository.findById(messageId).get().getAttachmentIds();
        for(UUID u:attachmentIdList){
             binaryContentRepository.deleteById(u);
        }
        messageRepository.deleteById(messageId);
    }
}
