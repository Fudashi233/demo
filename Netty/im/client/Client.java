package cn.edu.jxau.im.client;

import cn.edu.jxau.im.LoginUtils;
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
                        ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
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
                if (!LoginUtils.hasLogin(channel)) {
                    continue;
                }
                System.out.println("请输入消息：");
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                messageRequestPacket.setMessage(line);
                channel.writeAndFlush(PacketCodec.encode(messageRequestPacket));
            }
        }).start();
    }

}

class ClientHandler extends ChannelInboundHandlerAdapter {

    //@Override
    //public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    //Packet packet = PacketCodec.decode((ByteBuf) msg);
    //System.out.println("client：" + packet);
    //super.channelRead(ctx, msg);
    //if (packet instanceof LoginResponsePacket) {
    //    LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
    //    if (loginResponsePacket.getSuc()) {
    //        LoginUtils.markAsLogin(ctx.channel());
    //    }
    //} else if (packet instanceof MessageResponsePacket) {
    //    MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
    //    System.out.println(messageResponsePacket.getMessage());
    //} else {
    //    throw new RuntimeException("不支持的消息类型");
    //}
    //}

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUsername("Fudashi");
        loginRequestPacket.setPassword("123qwe");
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        ByteBuf buf = PacketCodec.encode(loginRequestPacket);
        ctx.channel().writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
    }
}

class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        for (int i = 0; i < 1000; i++) {
            MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
            messageRequestPacket.setMessage("你好，今天是：2019年11月10日，天气：晴，祝您生活愉快");
            //ctx.channel().writeAndFlush(PacketCodec.encode(messageRequestPacket));
            ctx.channel().writeAndFlush("你好呀，alalalal");
        }
    }
}
