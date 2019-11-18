package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.Session;
import cn.edu.jxau.im.SessionUtils;
import cn.edu.jxau.im.packet.GroupMessageRequestPacket;
import cn.edu.jxau.im.packet.GroupMessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.Objects;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午5:58
 */
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket msg) throws Exception {

        String message = msg.getMsg();
        String groupId = msg.getGroupId();
        Session session = SessionUtils.getSession(ctx.channel());

        GroupMessageResponsePacket groupMessageResponsePacket = new GroupMessageResponsePacket();
        groupMessageResponsePacket.setFromUserId(session.getUserId());
        groupMessageResponsePacket.setFromUsername(session.getUsername());
        groupMessageResponsePacket.setGroupId(groupId);
        groupMessageResponsePacket.setMsg(message);
        ChannelGroup channelGroup = SessionUtils.getChannelGroup(groupId);
        if (Objects.nonNull(channelGroup)) {
            channelGroup.writeAndFlush(groupMessageResponsePacket);
        }
    }
}
