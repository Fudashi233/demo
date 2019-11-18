package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.Session;
import cn.edu.jxau.im.SessionUtils;
import cn.edu.jxau.im.packet.MessageRequestPacket;
import cn.edu.jxau.im.packet.MessageResponsePacket;
import cn.edu.jxau.im.packet.PacketCodec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

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

        Session session = SessionUtils.getSession(ctx.channel());
        MessageResponsePacket messageResponsePacket = buildReplyMessage(session, msg);
        Channel toUserChannel = SessionUtils.getChannel(msg.getToUserId());
        if (Objects.nonNull(toUserChannel) && SessionUtils.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println("[" + msg.getToUserId() + "] 不在线，发送失败!");
        }
    }

    private MessageResponsePacket buildReplyMessage(Session session, MessageRequestPacket messageRequestPacket) {

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUsername(session.getUsername());
        return messageResponsePacket;
    }
}
