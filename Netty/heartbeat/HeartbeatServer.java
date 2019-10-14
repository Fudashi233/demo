package cn.edu.jxau.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/10/8
 * Time:上午8:37
 */
public class HeartbeatServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HeartbeatServerInit());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            workerEventLoopGroup.shutdownGracefully();
            bossEventLoopGroup.shutdownGracefully();
        }
    }
}

class HeartbeatServerInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(5, 7, 3, TimeUnit.SECONDS))
                .addLast(new HeartbeatServerHandler());
    }
}

class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) {

        if (event instanceof IdleStateEvent) {
            IdleState idleState = ((IdleStateEvent) event).state();
            if (Objects.equals(idleState, IdleState.ALL_IDLE)) {
                System.out.println("读写空闲");
            } else if (Objects.equals(idleState, IdleState.READER_IDLE)) {
                System.out.println("读空闲");
            } else if (Objects.equals(idleState, IdleState.WRITER_IDLE)) {
                System.out.println("写空闲");
            } else {
                throw new RuntimeException("未知的空闲状态");
            }
            ctx.channel().close();
        }
    }
}
