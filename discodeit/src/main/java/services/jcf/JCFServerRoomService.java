package services.jcf;

import entity.ServerRoom;
import entity.User;
import services.ServerRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFServerRoomService implements ServerRoomService {
    private final List<ServerRoom> data;
    public JCFServerRoomService(){
        data = new ArrayList<>() {};
    }

    @Override
    public boolean addServerRoom(ServerRoom serverRoom) {
        return data.add(serverRoom);
    }

    @Override
    public List<ServerRoom> getServerByName(String serverName) {
        List<ServerRoom> buffer = new ArrayList<>();
        for(ServerRoom s: data){
            if(s.getServerName().equals(serverName)){
                buffer.add(s);
            }
        }
        return buffer;
    }

    @Override
    public ServerRoom getServerRoomByID(UUID id) {
        for(ServerRoom s: data){
            if(s.getId().equals(id)){
                return s;
            }
        }
        return null;
    }

    @Override
    public List<ServerRoom> getAllServerRoom() {
        return data;
    }

    @Override
    public boolean updateServerRoom(ServerRoom serverRoom, String serverName) {
        for(ServerRoom s: data){
            if(s.equals(serverRoom)){
                s.setServerName(serverName);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteServerRoom(ServerRoom serverRoom) {
        for(ServerRoom s: data){
            if(s.equals(serverRoom)){
                data.remove(s);
                return true;
            }
        }
        return false;
    }
}
