package services.jcf;

import entity.Channel;
import entity.ServerRoom;
import entity.User;
import services.ChannelService;
import services.ServerRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFServerRoomService implements ServerRoomService {
    private final List<ServerRoom> data;
    private final JCFChannelService jcfChannelService;

    public JCFServerRoomService(JCFChannelService jcfchannelService){
        data = new ArrayList<>() {};
        this.jcfChannelService = jcfchannelService;
    }

    @Override
    public boolean addServerRoom(ServerRoom serverRoom) {
        System.out.println("--- New Server (" +serverRoom.getServerName() + ") added by user(" + serverRoom.getOwner().getDisplayName() +") ---");
        return data.add(serverRoom);
    }

    @Override
    public boolean addChannelToServer(ServerRoom serverRoom, Channel channel) {
        System.out.println("--- In server(" + serverRoom.getServerName() + ") channel (" + channel.getChannelName() +") added ---");
        serverRoom.getChannel().add(channel);
        return jcfChannelService.addChannel(channel);
    }

    @Override
    public boolean addMemberToServer(ServerRoom serverRoom, User user) {
        System.out.println("--- In server(" + serverRoom.getServerName() + ") user(" + user.getDisplayName() + ") joined ---");
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
                System.out.print("--- Server " + s.getServerName() + " chaned name to ");
                s.setServerName(serverName);
                System.out.println(serverName + " ---");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteServerRoom(ServerRoom serverRoom) {
        for(ServerRoom s: data){
            if(s.equals(serverRoom)){
                System.out.println("--- Deleted server " + serverRoom.getServerName() + " ---");
                data.remove(s);
                return true;
            }
        }
        return false;
    }
}
