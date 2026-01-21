package repository.jcf;

import entity.Channel;
import entity.ServerRoom;
import repository.ChannelRepository;
import repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    private final List<Channel> data;

    public JCFChannelRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public Channel save(Channel channel) {
        Optional<Channel> isNew = data.stream()
                .filter(c -> c.getId().equals(channel.getId()))
                .findAny();
        if (isNew.isPresent()) {
            // 이미 같은 값 있으므로 업데이트임.
            data.remove(isNew.get());
            data.add(channel);
            return channel;
        } else {
            // 새로운 값 추가임.
            boolean dupCheck = data.stream().noneMatch(c -> c.getChannelName().equals(channel.getChannelName()));
            if (dupCheck) {
                data.add(channel);
                return channel;
            }
            throw new IllegalArgumentException("Not allowed name");
        }
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return data.stream()
                .filter(c -> c.getId().equals(id))
                .findAny();
    }

    @Override
    public List<Channel> findAll() {
        return data;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        data.removeIf(s -> s.getId().equals(id));
    }
}
