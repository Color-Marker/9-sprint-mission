package services;

import entity.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // 채널을 서버에 추가하기
    boolean addChannel(Channel channel);

    Channel getChannelById(UUID id);

    // 이름으로 채널 찾기
    List<Channel> getChannelByName(String name);

    // 모든 채널 보기
    List<Channel> getAllChannel();
    List<Channel> getAllChannelByServer(ServerRoom server);
    // 채널 이름 바꾸기
    boolean updateChannelName(Channel channel, String channelName);

    // 채널 타입 바꾸기
    boolean changeChannelType(Channel channel, ChannelType channelType);

    // 채널에서 메시지 보내기 - 사실 상 의존성
    boolean sendMessageToChannel(Channel channel, Message message);

    // 채널에서 특정 유저 메시지만 보기
    List<Message> getAllMessageFromThatUser(Channel channel, User user);
    List<Message> getAllMessage(Channel channel);
    // 채널 삭제하기
    boolean deleteChannel(Channel channel);

}
