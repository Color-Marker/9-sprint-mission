package services;

import entity.Channel;
import entity.ChannelType;
import entity.ServerRoom;
import entity.User;

import java.util.List;
import java.util.UUID;

public interface ServerRoomService {
    boolean addServerRoom(ServerRoom serverRoom);

    // 의존성 취한 추가
    boolean addChannelToServer(ServerRoom serverRoom, Channel channel);
    boolean addMemberToServer(ServerRoom server, User user);

    List<ServerRoom> getServerByName(String serverName);
    ServerRoom getServerRoomByID(UUID id);

    List<ServerRoom> getAllServerRoom();    // 그냥 모든 서버방 받아오는 경우

    // 서버룸 수정 - 서버이름 수정
    boolean updateServerRoom(ServerRoom serverRoom, String serverName);
    boolean deleteServerRoom(ServerRoom serverRoom); // uuid id를 통해서 찾아서 유저 삭제

}
