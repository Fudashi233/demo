package cn.edu.jxau.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Objects;

/**
 * Desc:聊天室Server
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/10/4
 * Time:下午10:05
 */
public class ChatServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChatServerInitHandler());
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            workerEventLoopGroup.shutdownGracefully();
            bossEventLoopGroup.shutdownGracefully();
        }
    }
}

class ChatServerInitHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                .addLast(new StringDecoder())
                .addLast(new StringEncoder())
                .addLast(new ChatServerHandler());
    }
}

class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println("channelRead0：" + msg);
        for (Channel channel : CHANNEL_GROUP) {
            if (Objects.equals(channel, ctx.channel())) {
                channel.writeAndFlush("【自己】：" + msg + "\r\n");
            } else {
                channel.writeAndFlush("【" + channel.remoteAddress() + "】：" + msg + "\r\n");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {

        Channel channel = ctx.channel();
        CHANNEL_GROUP.writeAndFlush("【服务器】：" + channel.remoteAddress() + " 已上线\r\n");
        CHANNEL_GROUP.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {

        Channel channel = ctx.channel();
        CHANNEL_GROUP.writeAndFlush("【服务器】：" + channel.remoteAddress() + " 已下线\r\n");
        //CHANNEL_GROUP.remove(channel); //无需手动移除
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线");
    }
}