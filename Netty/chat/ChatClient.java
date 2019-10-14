package cn.edu.jxau.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Desc:聊天室Client
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/10/4
 * Time:下午10:05
 */
public class ChatClient {

    public static void main(String[] args) throws InterruptedException, IOException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitHandler());
            Channel channel = bootstrap.connect("127.0.0.1", 8899).sync().channel();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                channel.writeAndFlush(bufferedReader.readLine() + "\r\n");
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}

class ChatClientInitHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()))
                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                .addLast(new ChatClientHandler());
    }
}

class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
