package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent create(BinaryContentCreateDto dto){
        BinaryContent binaryContent = new BinaryContent(dto.fileName(), dto.contentType(), dto.size());
        return binaryContentRepository.save(binaryContent);
    }

    public Optional<BinaryContent> find(UUID id){
        return binaryContentRepository.findById(id);
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> idList){
        List<BinaryContent> fileList = new ArrayList<>();
        for(UUID id : idList){
            if(binaryContentRepository.findById(id).isPresent()) {
                fileList.add(binaryContentRepository.findById(id).get());
            }
        }
        return fileList;
    }

    public void delete(UUID id){
        if(!binaryContentRepository.existsById(id)){
            throw new NoSuchElementException("Can't find file");
        }
        binaryContentRepository.deleteById(id);

    }
}
