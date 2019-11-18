package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.packet.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/10
 * Time:下午3:04
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) throws Exception {
        System.out.printf("【单聊】【%s（%s）】%s\n", msg.getFromUsername(), msg.getFromUserId(), msg.getMessage());
    }
}
