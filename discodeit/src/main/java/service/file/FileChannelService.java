package service.file;

import entity.*;
import service.ChannelService;
import service.MessageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileChannelService implements ChannelService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    private final MessageService messageService;

    public FileChannelService(MessageService messageService) {
        this.messageService = messageService;
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Channel.class.getSimpleName());
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
    public void addChannel(Channel channel) {
        Path path = resolvePath(channel.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(channel);
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel getChannelById(UUID id) {
        Channel target = null;
        Path path = resolvePath(id);
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (Channel) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + id + " not found"));

    }

    @Override
    public List<Channel> getChannelByName(String name) {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (Channel) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(c -> c != null && c.getChannelName().equals(name))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Channel> getAllChannel() {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (Channel) ois.readObject();
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
    public List<Channel> getAllChannelByServer(ServerRoom server) {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (Channel) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(c -> c != null && c.getServerRoom().getId().equals(server.getId()))
                    .toList();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateChannelName(Channel channel, String channelName) {
        Channel target = getChannelById(channel.getId());
        target.setChannelName(channelName);
        addChannel(target);
    }

    @Override
    public void changeChannelType(Channel channel, ChannelType channelType) {
        Channel target = null;
        Path path = resolvePath(channel.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (Channel) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        Channel updatedTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channel.getId() + " not found"));
        updatedTarget.setChannelType(channelType);
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
    public void sendMessageToChannel(Channel channel, Message message) {
        Channel target = null;
        Path path = resolvePath(channel.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (Channel) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        Channel sendToTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channel.getId() + " not found"));
        sendToTarget.getMessages().add(message);
        messageService.addMessage(message);
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(sendToTarget);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> getAllMessageFromThatUser(Channel channel, User user) {
        Channel target = null;
        Path path = resolvePath(channel.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (Channel) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        List<Message> channelMsg = target.getMessages();
        return channelMsg.stream()
                .filter(m -> m.getSender().getId().equals(user.getId()))
                .toList();
    }

    @Override
    public List<Message> getAllMessage(Channel channel) {
        Channel target = getChannelById(channel.getId());
        return target.getMessages();
    }

    @Override
    public void deleteChannel(Channel channel) {
        Channel target = getChannelById(channel.getId());
        for(Message m : target.getMessages()){
            messageService.deleteMessage(m);
        }
        Path path = resolvePath(channel.getId());
        if(Files.notExists(path)){
            throw new NoSuchElementException("Channel with id " + channel.getId() + " not found");
        }
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMessageInChannel(Channel channel, Message message, String newContent) {
        Channel target = getChannelById(channel.getId());
        boolean check = false;
        for(Message m : target.getMessages()){
            if(m.getId().equals(message.getId())){
                m.setMessageContent(newContent);
                check = true;
                break;
            }
        }
        if(check){
            addChannel(target);
            messageService.updateMessage(message, newContent);
        }
    }
}
