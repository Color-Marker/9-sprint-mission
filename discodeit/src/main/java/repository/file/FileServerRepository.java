package repository.file;

import entity.Message;
import entity.ServerRoom;
import entity.User;
import repository.ServerRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileServerRepository implements ServerRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileServerRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", ServerRoom.class.getSimpleName());
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id){
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public ServerRoom save(ServerRoom serverRoom) {
        Path path = resolvePath(serverRoom.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(serverRoom);
            return serverRoom;
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ServerRoom> findById(UUID id) {
        ServerRoom target = null;
        Path path = resolvePath(id);
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (ServerRoom) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(target);
    }

    @Override
    public List<ServerRoom> findAll() {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (ServerRoom) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        if(Files.notExists(path)){
            throw new NoSuchElementException("ServerRoom with id " + id + " not found");
        }
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
