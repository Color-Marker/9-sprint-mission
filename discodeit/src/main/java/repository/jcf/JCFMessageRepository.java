package repository.jcf;

import entity.Channel;
import entity.Message;
import repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final List<Message> data;

    public JCFMessageRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public Message save(Message message) {
        Optional<Message> isNew = data.stream()
                .filter(m -> m.getId().equals(message.getId()))
                .findAny();
        if (isNew.isPresent()) {
            // 이미 같은 값 있으므로 업데이트임.
            data.remove(isNew.get());
            data.add(message);
            return message;
        } else {
            // 새로운 값 추가임.
            data.add(message);
            return message;
        }
    }

    @Override
    public Message findById(UUID id) {
        return data.stream()
                .filter(m -> m.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Message> findAll() {
        return data;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id) != null;
    }

    @Override
    public void deleteById(UUID id) {
        data.removeIf(m -> m.getId().equals(id));
    }
}
