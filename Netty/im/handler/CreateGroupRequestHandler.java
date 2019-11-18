package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.Session;
import cn.edu.jxau.im.SessionUtils;
import cn.edu.jxau.im.packet.CreateGroupRequestPacket;
import cn.edu.jxau.im.packet.CreateGroupResponsePacket;
import cn.edu.jxau.im.packet.LoginRequestPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:40
 */
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) throws Exception {

        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        List<String> usernameList = new ArrayList<>();
        for (String userId : msg.getUserIdList()) {
            Channel channel = SessionUtils.getChannel(userId);
            if (Objects.nonNull(channel)) {
                channelGroup.add(channel);
                usernameList.add(SessionUtils.getSession(channel).getUsername());
            }
        }
        String groupId = UUID.randomUUID().toString();
        SessionUtils.createGroupSession(groupId, channelGroup);
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuc(true);
        createGroupResponsePacket.setGroupId(groupId);
        createGroupResponsePacket.setUsernameList(usernameList);
        channelGroup.writeAndFlush(createGroupResponsePacket);
    }
}
