package util;

import entity.Channel;
import entity.Message;
import entity.ServerRoom;
import entity.User;
import service.ChannelService;
import service.ServerRoomService;
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
    public static void classificationLine(){
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println();
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

    public static void printServerList(List<ServerRoom> serverRooms){
        for (int i = 0; i < serverRooms.size(); i++) {
            ServerRoom s = serverRooms.get(i);
            System.out.printf("[%d] %s%n", i, s.getServerName());
        }
        System.out.println();
    }

    public static void printServerMember(ServerRoom server){
        System.out.println("- Server Owner: " + server.getOwner().getDisplayName() + " (" +  server.getOwner().getId() + ") -");
        System.out.println("- Server members -");
        server.getMember().forEach(p -> System.out.println(p.getDisplayName() + " (" + p.getId() + ")"));
        System.out.println();
    }

    public static void printServerChannel(ServerRoomService serverRoomService, ServerRoom server){
        System.out.println("- Server channels -");
        List<Channel> channels = serverRoomService.getAllChannel(server);
        for (int i = 0; i < channels.size(); i++) {
            Channel c = channels.get(i);
            System.out.printf("[%d] %s%n", i, c.getChannelName());
        }
        System.out.println();
    }

    public static void printAllMsgInChannel(ChannelService channelService, Channel channel, User user){
        System.out.println("** Messages **");
        List<Message> msgs = channelService.getAllMessage(channel);
        for (int i = 0; i < msgs.size(); i++) {
            Message m = msgs.get(i);
            String direction = m.getSender().getId().equals(user.getId()) ? " <- " : " -> ";
            System.out.printf("%s [%d] %s: %s%n", direction, i, m.getSender().getDisplayName(), m.getMessageContent());
        }
    }

    public static void notAllowedWarning(){
        System.out.println("!!! Sorry! Only allowed user can use this! !!!");
    }
    public static void onlyAdminCanWarning(){
        System.out.println("!!! Sorry! Only admin can use this! !!!");
    }
}
