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

    public <T, R> PageResponse<R> fromSlice(Slice<T> slice, Function<T, R> converter,
                                            Function<T, Object> cursorExtractor) {
        List<R> dtos = slice.getContent().stream()
                .map(converter)
                .toList();
        Object nextCursor = null;
        if (slice.hasNext() && !slice.getContent().isEmpty()) {
            T cursor = slice.getContent().get(slice.getContent().size() - 1);
            nextCursor = cursorExtractor.apply(cursor);
        }
        return new PageResponse<>(
                dtos,
                nextCursor,
                slice.getSize(),
                slice.hasNext(),
                null // slice는 전체 개수 X
        );
    }

    public <T, R> PageResponse<R> fromPage(Page<T> page, Function<T, R> converter,
                                           Function<T, Object> cursorExtractor) {
        List<R> dtos = page.getContent().stream()
                .map(converter)
                .toList();
        Object nextCursor = null;
        if (page.hasNext() && !page.getContent().isEmpty()) {
            T cursor = page.getContent().get(page.getContent().size() - 1);
            nextCursor = cursorExtractor.apply(cursor);
        }
        return new PageResponse<>(
                dtos,
                nextCursor,
                page.getSize(),
                page.hasNext(),
                page.getTotalElements()
        );
    }
}
