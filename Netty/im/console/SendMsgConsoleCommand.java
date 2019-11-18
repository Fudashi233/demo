package cn.edu.jxau.im.console;

import cn.edu.jxau.im.packet.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:16
 */
public class SendMsgConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.println("SendMsgConsoleCommand：");
        String toUserId = scanner.nextLine();
        String message = scanner.nextLine();
        channel.writeAndFlush(new MessageRequestPacket(message, toUserId));
    }
}
