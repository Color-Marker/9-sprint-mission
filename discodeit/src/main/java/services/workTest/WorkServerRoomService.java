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
    public void addServerRoom(ServerRoom serverRoom) {
        data.add(serverRoom);
    }

    @Override
    public void addChannelToServer(ServerRoom serverRoom, Channel channel) {
        serverRoom.getChannel().add(channel);
        channelService.addChannel(channel);
    }

    @Override
    public void addMemberToServer(ServerRoom serverRoom, User user) {
        serverRoom.getMember().add(user);
    }

    @Override
    public void delMemberInServer(ServerRoom serverRoom, User user) {
        serverRoom.getMember().removeIf(p->p.equals(user));
    }

    @Override
    public List<ServerRoom> getServerByName(String serverName) {
        return data.stream()
                .filter(s->s.getServerName().equals(serverName))
                .toList();
    }

    @Override
    public ServerRoom getServerRoomByID(UUID id) {
        return data.stream()
                .filter(s->s.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<ServerRoom> getInvitedServer(User user) {
        return getAllServerRoom()
                .stream()
                .filter(s -> s.getMember().stream()
                        .anyMatch(m -> m.getId().equals(user.getId())))
                .toList();

    }

    @Override
    public List<ServerRoom> getOwningServer(User user) {
        return getAllServerRoom()
                .stream()
                .filter(s -> s.getOwner().equals(user))
                .toList();
    }

    @Override
    public List<ServerRoom> getAllServerRoom() {
        return new ArrayList<>(data);
    }

    @Override
    public List<Channel> getAllChannel(ServerRoom serverRoom) {
        return channelService.getAllChannelByServer(serverRoom);
    }

    @Override
    public boolean updateServerRoom(ServerRoom serverRoom, String serverName) {
        return data.stream()
                .filter(s->s.equals(serverRoom))
                .findAny()
                .map(s-> s.setServerName(serverName))
                .orElse(false);
    }

    @Override
    public void deleteServerRoom(ServerRoom serverRoom) {
        for(Channel c: serverRoom.getChannel()) {
            channelService.deleteChannel(c);
        }
        data.removeIf(s -> s.equals(serverRoom));
    }
}
