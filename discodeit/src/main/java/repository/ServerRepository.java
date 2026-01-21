package repository;

import entity.ServerRoom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerRepository {
    ServerRoom save(ServerRoom serverRoom);
    ServerRoom findById(UUID id);
    List<ServerRoom> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
