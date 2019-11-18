package cn.edu.jxau.im.console;

import cn.edu.jxau.im.packet.CreateGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:21
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String SPLIT = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.println("CreateGroupConsoleCommand：");
        String userIdListStr = scanner.nextLine();
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();
        packet.setUserIdList(userIdListStr.split(SPLIT));
        channel.writeAndFlush(packet);
    }
}
