package service.file;

import entity.Channel;
import entity.Message;
import entity.ServerRoom;
import entity.User;
import service.ChannelService;
import service.ServerRoomService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileServerService implements ServerRoomService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    private final ChannelService channelService;

    public FileServerService(ChannelService channelService) {
        this.channelService = channelService;
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
    public void addServerRoom(ServerRoom serverRoom) {
        Path path = resolvePath(serverRoom.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(serverRoom);
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addChannelToServer(ServerRoom serverRoom, Channel channel) {
        ServerRoom target = null;
        Path path = resolvePath(serverRoom.getId());
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
        ServerRoom addTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("ServerRoom with id " + serverRoom.getId() + " not found"));
        addTarget.getChannel().add(channel);
        channelService.addChannel(channel);
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(addTarget);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addMemberToServer(ServerRoom server, User user) {
        ServerRoom target = null;
        Path path = resolvePath(server.getId());
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
        ServerRoom addTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("ServerRoom with id " + server.getId() + " not found"));
        addTarget.getMember().add(user);
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(addTarget);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delMemberInServer(ServerRoom server, User user) {
        ServerRoom target = null;
        Path path = resolvePath(server.getId());
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
        ServerRoom addTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("ServerRoom with id " + server.getId() + " not found"));
        addTarget.getMember().removeIf(p->p.equals(user));
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(addTarget);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ServerRoom> getServerByName(String serverName) {
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
                    .filter(s -> s != null && s.getServerName().equals(serverName))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServerRoom getServerRoomByID(UUID id) {
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
        return Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("Server with id " + id + " not found"));
    }

    @Override
    public List<ServerRoom> getInvitedServer(User user) {
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
                    .filter(s -> s != null && s.getMember().stream()
                            .anyMatch(p -> p.getId().equals(user.getId())))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ServerRoom> getOwningServer(User user) {
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
                    .filter(s -> s != null && s.getOwner().equals(user))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ServerRoom> getAllServerRoom() {
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
    public List<Channel> getAllChannel(ServerRoom serverRoom) {
        ServerRoom target = null;
        Path path = resolvePath(serverRoom.getId());
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
        return target.getChannel();
    }

    @Override
    public boolean updateServerRoom(ServerRoom serverRoom, String serverName) {
        ServerRoom target = null;
        Path path = resolvePath(serverRoom.getId());
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
        ServerRoom updatedTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("ServerRoom with id " + serverRoom.getId() + " not found"));
        updatedTarget.setServerName(serverName);
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(updatedTarget);
            return true;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteServerRoom(ServerRoom serverRoom) {
        ServerRoom target = getServerRoomByID(serverRoom.getId());
        for(Channel c : target.getChannel()){
            channelService.deleteChannel(c);
        }
        Path path = resolvePath(serverRoom.getId());
        if(Files.notExists(path)){
            throw new NoSuchElementException("ServerRoom with id " + serverRoom.getId() + " not found");
        }
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
