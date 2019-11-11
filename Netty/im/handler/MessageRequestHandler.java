package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.packet.MessageRequestPacket;
import cn.edu.jxau.im.packet.MessageResponsePacket;
import cn.edu.jxau.im.packet.PacketCodec;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/10
 * Time:下午3:04
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        ctx.channel().writeAndFlush(reply(msg));
    }

    private MessageResponsePacket reply(MessageRequestPacket messageRequestPacket) {

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("【ECHO】" + messageRequestPacket.getMessage());
        return messageResponsePacket;
    }
}
