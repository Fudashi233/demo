package cn.edu.jxau.im.client;

import cn.edu.jxau.im.LoginUtils;
import cn.edu.jxau.im.SessionUtils;
import cn.edu.jxau.im.console.ConsoleCommandManager;
import cn.edu.jxau.im.console.LoginConsoleCommand;
import cn.edu.jxau.im.handler.*;
import cn.edu.jxau.im.packet.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;
import java.util.UUID;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/3
 * Time:下午4:56
 */
public class Client {

    public static void main(String[] args) {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new GroupMessageResponseHandler());
                        ch.pipeline().addLast(new JoinGroupResponseHandler());
                        ch.pipeline().addLast(new CreateGroupResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        bootstrap.connect("127.0.0.1", 8899).addListener(future -> {
            if (!future.isSuccess()) {
                return;
            }
            Channel channel = ((ChannelFuture) future).channel();
            startConsoleThread(channel);
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                if (SessionUtils.hasLogin(channel)) {
                    ConsoleCommandManager.exec(scanner, channel);
                } else {
                    new LoginConsoleCommand().exec(scanner, channel);
                }
            }
        }).start();
    }
}