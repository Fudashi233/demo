package cn.edu.jxau.netty.first;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/10/3
 * Time:下午10:03
 */
public class SimpleNettyServer {

    private static final int PORT = 8899;

    public static void main(String[] args) throws InterruptedException {

        // 1.事件循环组
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();

        try {
            // 2.启动服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();


            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitChannel()); // 3.设置childHandler
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();

            // 4.关闭服务器
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerEventLoopGroup.shutdownGracefully();
            bossEventLoopGroup.shutdownGracefully();
        }
    }
}

class ServerInitChannel extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("HttpServerCodec", new HttpServerCodec()) //针对HTTP编码、HTTP解码的一个封装
                .addLast("HttpServerHandler", new HttpServerHandler());
    }
}

class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {


        if (!(msg instanceof HttpRequest)) {
            return;
        }
        System.out.println("---：" + ((HttpRequest) msg).method());
        System.out.println("---：" + msg.getClass());
        System.out.println("---：" + ctx.channel().remoteAddress()); //lsof -i:8899，查看和8899端口相关的进程
        ByteBuf buf = Unpooled.copiedBuffer("Hello world!", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }


}