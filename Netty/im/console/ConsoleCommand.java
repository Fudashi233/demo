package cn.edu.jxau.im.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:16
 */
public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
