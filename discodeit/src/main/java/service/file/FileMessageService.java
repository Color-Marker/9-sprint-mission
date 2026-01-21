package service.file;

import entity.Channel;
import entity.Message;
import entity.User;
import service.MessageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileMessageService implements MessageService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileMessageService() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Message.class.getSimpleName());
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
    public boolean addMessage(Message message) {
        Path path = resolvePath(message.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(message);
            return true;
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message getMessageById(UUID id) {
        Message target = null;
        Path path = resolvePath(id);
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (Message) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));

    }

    @Override
    public List<Message> getMessageFromUser(User user) {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (Message) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(msg -> msg != null && msg.getSender().equals(user))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> getAllMessage() {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (Message) ois.readObject();
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
    public List<Message> getMessageByContent(String messageContent) {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (Message) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(msg -> msg != null && msg.getMessageContent().contains(messageContent))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> getMessageByChannel(Channel channel) {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (Message) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(msg -> msg != null && msg.getChannel().equals(channel))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMessage(Message message, String messageContent) {
        Message target = null;
        Path path = resolvePath(message.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (Message) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        Message updatedTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + message.getId() + " not found"));
        updatedTarget.setMessageContent(messageContent);
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(updatedTarget);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMessage(Message message) {
        Path path = resolvePath(message.getId());
        if(Files.notExists(path)){
            throw new NoSuchElementException("Message with id " + message.getId() + " not found");
        }
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
