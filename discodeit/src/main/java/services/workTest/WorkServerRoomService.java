package services.workTest;

import entity.Channel;
import entity.ServerRoom;
import entity.User;
import services.ChannelService;
import services.ServerRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkServerRoomService implements ServerRoomService {
    private final List<ServerRoom> data;
    private final ChannelService channelService;

    public WorkServerRoomService(ChannelService channelService){
        data = new ArrayList<>() {};
        this.channelService = channelService;
    }

    @Override
    public boolean addServerRoom(ServerRoom serverRoom) {
        return data.add(serverRoom);
    }

    @Override
    public boolean addChannelToServer(ServerRoom serverRoom, Channel channel) {
        serverRoom.getChannel().add(channel);
        return channelService.addChannel(channel);
    }

    @Override
    public boolean addMemberToServer(ServerRoom serverRoom, User user) {
        return serverRoom.getMember().add(user);
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
                s.setUpdatedAt();
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
