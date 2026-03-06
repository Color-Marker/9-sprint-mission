package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageResponseMapper {

  private final MessageMapper messageMapper;

  public <T, R> PageResponse<R> fromSlice(Slice<T> slice, Function<T, R> converter) {
    List<R> dtos = slice.getContent().stream()
        .map(converter)
        .toList();
    return new PageResponse<>(
        dtos,
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null // slice는 전체 개수 X
    );
  }

  public <T> PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
