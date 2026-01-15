package util;

import entity.Channel;
import entity.Message;
import entity.ServerRoom;
import entity.User;
import services.ChannelService;
import services.ServerRoomService;

import java.util.Arrays;
import java.util.List;

public class PrintUtil {
    public static void printTypeNum(String... printString){
        System.out.println("Type number to work");
        System.out.println("-----------------------------------");
        for (int i = 0; i < printString.length; i++) {
            System.out.println((i + 1) + ". " + printString[i]);
        }
        System.out.println("-----------------------------------");
    }

    public static void printMenu(String printString){
        System.out.println("--- " + printString + " ---");
    }

    public static void typeRightNum(){
        System.out.println("Warning: Only type right numbers");
    }

    public static void printBack(String printString){
        System.out.println("Back to " + printString + "...");
    }

    public static void printUserInfo(User user){
        System.out.println("Name: " + user.getDisplayName());
        System.out.println("ID: " + user.getId());
        System.out.println();
    }

    public static void printServerList(List<ServerRoom> serverRoom){
        serverRoom.forEach(s->System.out.println(s.getServerName()));
    }

    public static void printServerMember(ServerRoom server){
        System.out.println("- Server Owner: " + server.getOwner().getDisplayName() + " -");
        System.out.println("- Server members -");
        server.getMember().forEach(p -> System.out.println(p.getDisplayName()));
        System.out.println();
    }

    public static void printServerChannel(ServerRoomService serverRoomService, ServerRoom server){
        System.out.println("- Server channels -");
        serverRoomService.getAllChannel(server)
            .forEach(c -> System.out.println(c.getChannelName()));
        System.out.println();
    }

    public static void printAllMsgInChannel(ChannelService channelService, Channel channel, User user){
        System.out.println("** Messages **");
        List<Message> msgs = channelService.getAllMessage(channel);
        for (int i = 0; i < msgs.size(); i++) {
            Message m = msgs.get(i);
            String direction = m.getSender().getId().equals(user.getId()) ? " <- " : " -> ";
            System.out.println(String.format("%s [%d] %s: %s", direction, i, m.getSender().getDisplayName(), m.getMessageContent()));
        }
    }

    public static void notAllowedWarning(){
        System.out.println("!!! Sorry! Only allowed user can use this! !!!");
    }
    public static void onlyAdminCanWarning(){
        System.out.println("!!! Sorry! Only admin can use this! !!!");
    }
}
