package cn.edu.jxau.im.console;

import cn.edu.jxau.im.packet.LoginRequestPacket;
import io.netty.channel.Channel;

import java.io.Console;
import java.util.Scanner;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:26
 */
public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.println("LoginConsoleCommand：");
        String username = scanner.nextLine();
        channel.writeAndFlush(new LoginRequestPacket(username));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
