package repository.jcf;

import entity.Channel;
import entity.ServerRoom;
import entity.User;
import repository.ServerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFServerRepository implements ServerRepository {
    private final List<ServerRoom> data;

    public JCFServerRepository() {
        this.data = new ArrayList<>();

    }


    @Override
    public ServerRoom save(ServerRoom serverRoom) {
        Optional<ServerRoom> isNew = data.stream()
                .filter(c -> c.getId().equals(serverRoom.getId()))
                .findAny();
        if (isNew.isPresent()) {
            // 이미 같은 값 있으므로 업데이트임.
            data.remove(isNew.get());
            data.add(serverRoom);
            return serverRoom;
        } else {
            // 새로운 값 추가임.
            boolean dupCheck = data.stream().noneMatch(s -> s.getServerName().equals(serverRoom.getServerName()));
            if (dupCheck) {
                data.add(serverRoom);
                return serverRoom;
            }
            throw new IllegalArgumentException("Not allowed name");

        }
    }

    @Override
    public Optional<ServerRoom> findById(UUID id) {
        return data.stream()
                .filter(s -> s.getId().equals(id))
                .findAny();
    }

    @Override
    public List<ServerRoom> findAll() {
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
