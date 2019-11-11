package cn.edu.jxau.im.server;

import cn.edu.jxau.im.handler.*;
import cn.edu.jxau.im.packet.MessageRequestPacket;
import cn.edu.jxau.im.packet.Packet;
import cn.edu.jxau.im.packet.PacketCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/4
 * Time:上午9:29
 */
public class Server {

    public static void main(String[] args) {

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new AuthHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        serverBootstrap.bind(8899);
    }
}

class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Packet packet = PacketCodec.decode((ByteBuf) msg);
        System.out.println(((MessageRequestPacket) packet).getMessage());
        //if (packet instanceof LoginRequestPacket) {
        //    LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        //    LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
        //    if (valid(loginRequestPacket)) {
        //        loginResponsePacket.setMsg("登录成功");
        //        loginResponsePacket.setSuc(true);
        //    } else {
        //        loginResponsePacket.setMsg("登录失败");
        //        loginResponsePacket.setSuc(false);
        //    }
        //    ctx.channel().writeAndFlush(PacketCodec.encode(loginResponsePacket));
        //} else if (packet instanceof MessageRequestPacket) {
        //    MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
        //    MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        //    messageResponsePacket.setMessage("【ECHO】" + messageRequestPacket.getMessage());
        //    ctx.channel().writeAndFlush(PacketCodec.encode(messageResponsePacket));
        //} else {
        //    throw new RuntimeException("不支持的消息类型");
        //}
    }

    //private boolean valid(LoginRequestPacket loginRequestPacket) {
    //    return true;
    //}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
    }
}