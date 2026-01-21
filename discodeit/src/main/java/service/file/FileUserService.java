package service.file;

import entity.ServerRoom;
import entity.User;
import service.ServerRoomService;
import service.UserService;

import javax.print.attribute.standard.Severity;
import java.io.*;
import java.nio.channels.ClosedSelectorException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserService implements UserService {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    private final ServerRoomService serverRoomService;

    public FileUserService(ServerRoomService serverRoomService) {
        // 현재 작업 디렉토리의 절대경로를 가져와, User의 클래스 이름인 User를 이용해
        // 현재 절대 경로에 file-data-map이란 폴더를 만들어 거기에 User라는 디렉토리 경로를 만듦
        // 즉, 내프로젝트폴더/file-data-map/User 구성
        this.serverRoomService = serverRoomService;
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName());
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id){
        // id받은거 + extension 붙여줌.
        // id가 amy면 amy.ser이 되는 거.
        // 최종 파일 경로 지정용
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public void addUser(User user) {
        List<User> allUser = getAllUser();
        for(User p: allUser){
            if(p.getDisplayName().equals(user.getDisplayName())) return;
        }
        Path path = resolvePath(user.getId());
        try (
            FileOutputStream fos = new FileOutputStream(path.toFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(user);
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(UUID id) {
        User target = null;
        Path path = resolvePath(id);
        if(Files.exists(path)){
            try(
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (User) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }

    @Override
    public User getUserByName(String displayName) {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ){
                            return (User) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(user -> user != null && user.getDisplayName().equals(displayName))
                    .findAny()
                    .orElse(null);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUser() {
        try(Stream<Path> pathStream = Files.list(DIRECTORY)){
            return pathStream
                    .filter(path->path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                                ){
                            return (User) ois.readObject();
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
    public void updateUserByName(User user, String displayName) {
        List<User> allUser = getAllUser();
        for(User p: allUser){
            if(p.getDisplayName().equals(displayName)) return;
        }
        User target = null;
        Path path = resolvePath(user.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ){
                target = (User) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        User updatedTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("User with id " + user.getId() + " not found"));
        updatedTarget.setDisplayName(displayName);
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
    public void updateUserByEmail(User user, String email) {
        User target = null;
        Path path = resolvePath(user.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (User) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        User updatedTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("User with id " + user.getId() + " not found"));
        updatedTarget.setEmail(email);
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
    public void updateUserByNumber(User user, String phoneNumber) {
        User target = null;
        Path path = resolvePath(user.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (User) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        User updatedTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("User with id " + user.getId() + " not found"));
        updatedTarget.setPhoneNumber(phoneNumber);
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
    public void updateUserByPassword(User user, String password) {
        User target = null;
        Path path = resolvePath(user.getId());
        if(Files.exists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                target = (User) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        User updatedTarget = Optional.ofNullable(target)
                .orElseThrow(() -> new NoSuchElementException("User with id " + user.getId() + " not found"));
        updatedTarget.setPassword(password);
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
    public void deleteUser(User user) {
        List<ServerRoom> owningServerRooms = serverRoomService.getOwningServer(user);
        for(ServerRoom s: owningServerRooms){
            serverRoomService.deleteServerRoom(s);
        }
        List<ServerRoom> memberServerRooms = serverRoomService.getInvitedServer(user);
        for(ServerRoom s: memberServerRooms){
            serverRoomService.delMemberInServer(s, user);
        }

        Path path = resolvePath(user.getId());
        if(Files.notExists(path)){
            throw new NoSuchElementException("User with id " + user.getId() + " not found");
        }
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
