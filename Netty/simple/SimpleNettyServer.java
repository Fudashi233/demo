package cn.edu.jxau.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/10/4
 * Time:下午8:04
 */
public class SimpleNettyServer {

    public static void main(String[] args) throws InterruptedException {


        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitChannel());
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();

            System.out.println("NettyServer start...");
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerEventLoopGroup.shutdownGracefully();
            bossEventLoopGroup.shutdownGracefully();
        }
    }
}

class ServerInitChannel extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                .addLast(new SimpleNettyServerChannel());
    }
}

class SimpleNettyServerChannel extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println(ctx.channel().remoteAddress() + "，msg：" + msg);
        ctx.channel().writeAndFlush("NettyServer：" + UUID.randomUUID().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}