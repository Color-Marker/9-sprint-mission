package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binaryContent.FileNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    BinaryContent binaryContent = new BinaryContent(
        fileName,
        (long) bytes.length,
        contentType
    );
    BinaryContent saved = binaryContentRepository.save(binaryContent);

    log.info("파일 엔티티 리포지토리 저장 완료 - 파일 상세 정보: {}", saved);

    binaryContentStorage.put(binaryContent.getId(), bytes);
    return saved;
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    log.debug("파일 검색 시도 - 파일 ID: {}", binaryContentId);
    BinaryContent file = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> {
          log.warn("파일 검색 실패 - 파일 ID: {}", binaryContentId);
          return new FileNotFoundException(binaryContentId);
        });
    return binaryContentMapper.toDto(file);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    List<BinaryContent> fileList = binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .toList();
    return fileList.stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      log.warn("파일 검색 실패 - 파일 ID: {}", binaryContentId);
      throw new FileNotFoundException(binaryContentId);
    }
    binaryContentRepository.deleteById(binaryContentId);
  }

}
