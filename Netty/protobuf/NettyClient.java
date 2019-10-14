package cn.edu.jxau.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInit());

        bootstrap.connect("127.0.0.1", 8899).sync();

    }
}

class NettyClientInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(DataInfo.Student.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new NettyClientHandler());

    }
}

class NettyClientHandler extends SimpleChannelInboundHandler<DataInfo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Student msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setAge(17)
                .setName("AAA")
                .setAddress("BBB")
                .build();
        ctx.channel().writeAndFlush(student);
    }
}
