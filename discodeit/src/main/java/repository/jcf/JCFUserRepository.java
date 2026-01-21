package repository.jcf;

import entity.Channel;
import entity.User;
import repository.ServerRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private final List<User> data;
    public JCFUserRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        Optional<User> isNew = data.stream()
                .filter(c -> c.getId().equals(user.getId()))
                .findAny();
        if (isNew.isPresent()) {
            // 이미 같은 값 있으므로 업데이트임.
            data.remove(isNew.get());
            data.add(user);
            return user;
        } else {
            // 새로운 값 추가임.
            boolean dupCheck = data.stream().noneMatch(p -> p.getDisplayName().equals(user.getDisplayName()));
            if (dupCheck) {
                data.add(user);
                return user;
            }
            throw new IllegalArgumentException("Not allowed name");

        }}

    @Override
    public Optional<User> findById(UUID id) {
        return data.stream()
                .filter(p -> p.getId().equals(id))
                .findAny();
    }

    @Override
    public List<User> findAll() {
        return data;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        data.removeIf(p -> p.getId().equals(id));
    }
}
