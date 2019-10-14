package cn.edu.jxau.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;


/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/10/14
 * Time:上午9:16
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossLoopGroup, workerLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerInit());

        serverBootstrap.bind(8899).sync();
    }
}

class NettyServerInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(DataInfo.Student.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new NettyServerHandler());
    }
}

class NettyServerHandler extends SimpleChannelInboundHandler<DataInfo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Student msg) throws Exception {

        System.out.println(msg);
    }
}