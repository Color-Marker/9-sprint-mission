package services;

import entity.Channel;
import entity.ChannelType;
import entity.Message;
import entity.User;

import java.util.List;

public interface ChannelService {

    // 채널을 서버에 추가하기
    boolean addChannel(Channel channel);

    // 특정 리더가 만든 채널만 보기
    List<Channel> getChanelFromThatLeader(User user);
    // 모든 채널 보기
    List<Channel> getAllChannel();

    // 채널 이름 바꾸기
    Channel updateChannelName(Channel channel, String channelName);

    // 채널에 멤버 추가하기
    boolean addMemberToChannel(Channel channel, User user);

    // 채널 타입 바꾸기
    boolean changeChannelType(Channel channel, ChannelType channelType);

    // 채널에서 메시지 보내기
    boolean sendMessageToChannel(Channel channel, Message message);

    // 채널에서 특정 유저 메시지만 보기
    List<Message> getAllMessageFromThatUser(Channel channel, User user);

    // 채널 서버에서 삭제하기
    boolean deleteChannel(Channel channel);

}
