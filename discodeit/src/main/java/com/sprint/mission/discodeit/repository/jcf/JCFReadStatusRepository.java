package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        this.data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<UUID> findUserIdByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(r->r.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .toList();
    }

    @Override
    public List<UUID> findChannelIdByUserId(UUID userId) {
        return findAll().stream()
                .filter(r->r.getUserId().equals(userId))
                .map(ReadStatus::getChannelId)
                .toList();
    }

    @Override
    public Optional<ReadStatus> findByChannelAndUser(UUID channelId, UUID userId) {
        return findAll().stream()
                .filter(r->r.getUserId().equals(userId))
                .filter(r->r.getChannelId().equals(channelId))
                .findAny();
    }

    @Override
    public List<ReadStatus> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public Boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }
}
