package service.basic;

import entity.Channel;
import entity.ServerRoom;
import entity.User;
import repository.ServerRepository;
import service.ChannelService;
import service.ServerRoomService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicServerService implements ServerRoomService {
    private final ServerRepository serverRepository;
    private final ChannelService channelService;

    public BasicServerService(ServerRepository serverRepository, ChannelService channelService) {
        this.serverRepository = serverRepository;
        this.channelService = channelService;
    }

    @Override
    public void addServerRoom(ServerRoom serverRoom) {
        serverRepository.save(serverRoom);
    }

    @Override
    public void addChannelToServer(ServerRoom serverRoom, Channel channel) {
        ServerRoom target = serverRepository.findById(serverRoom.getId());
        if(target == null){
            return;
        }
        target.getChannel().add(channel);
        serverRepository.save(target);
    }

    @Override
    public void addMemberToServer(ServerRoom server, User user) {
        ServerRoom target = serverRepository.findById(server.getId());
        if(target == null){
            return;
        }
        target.getMember().add(user);
        serverRepository.save(target);
    }

    @Override
    public void delMemberInServer(ServerRoom server, User user) {
        ServerRoom target = serverRepository.findById(server.getId());
        if(target == null){
            return;
        }
        target.getMember().remove(user);
        serverRepository.save(target);
    }

    @Override
    public List<ServerRoom> getServerByName(String serverName) {
        return serverRepository.findAll().stream()
                .filter(s->s.getServerName().equals(serverName))
                .toList();
    }

    @Override
    public ServerRoom getServerRoomByID(UUID id) {
        return serverRepository.findById(id);
    }

    @Override
    public List<ServerRoom> getInvitedServer(User user) {
        return serverRepository.findAll().stream()
                .filter(server -> server.getMember().stream()
                        .anyMatch(member -> member.getId().equals(user.getId())))
                .toList();
    }

    @Override
    public List<ServerRoom> getOwningServer(User user) {
        return serverRepository.findAll().stream()
                .filter(s->s.getOwner().getId().equals(user.getId()))
                .toList();
    }

    @Override
    public List<ServerRoom> getAllServerRoom() {
        return serverRepository.findAll();
    }

    @Override
    public List<Channel> getAllChannel(ServerRoom serverRoom) {
        return channelService.getAllChannelByServer(serverRoom);
    }

    @Override
    public boolean updateServerRoom(ServerRoom serverRoom, String serverName) {
        ServerRoom target = serverRepository.findById(serverRoom.getId());
        if(target == null){
            throw new NoSuchElementException("Can't find target");
        }
        target.setServerName(serverName);
        return serverRepository.save(target) != null;
    }

    @Override
    public void deleteServerRoom(ServerRoom serverRoom) {
        List<Channel> target = channelService.getAllChannelByServer(serverRoom);
        for(Channel c: target){
            channelService.deleteChannel(c);
        }
        serverRepository.deleteById(serverRoom.getId());
    }
}
