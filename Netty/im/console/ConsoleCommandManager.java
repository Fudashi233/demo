package cn.edu.jxau.im.console;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:28
 */
public class ConsoleCommandManager {

    private static final Map<String, ConsoleCommand> CONSOLE_COMMAND_MAP = new HashMap<>();

    static {

        CONSOLE_COMMAND_MAP.put("login", new LoginConsoleCommand());
        CONSOLE_COMMAND_MAP.put("createGroup", new CreateGroupConsoleCommand());
        CONSOLE_COMMAND_MAP.put("sendMsg", new SendMsgConsoleCommand());
        CONSOLE_COMMAND_MAP.put("sendGroupMsg", new SendGroupMsgConsoleCommand());
        CONSOLE_COMMAND_MAP.put("joinGroup", new JoinGroupConsoleCommand());
    }

    public static void exec(Scanner scanner, Channel channel) {

        String command = scanner.nextLine();
        ConsoleCommand consoleCommand = CONSOLE_COMMAND_MAP.get(command);
        if (Objects.isNull(consoleCommand)) {
            System.out.println("无法识别的命令");
        } else {
            consoleCommand.exec(scanner, channel);
        }
    }
}
