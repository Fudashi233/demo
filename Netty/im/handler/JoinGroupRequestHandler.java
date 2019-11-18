package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.SessionUtils;
import cn.edu.jxau.im.packet.JoinGroupRequestPacket;
import cn.edu.jxau.im.packet.JoinGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午5:16
 */
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket msg) throws Exception {

        Boolean suc = SessionUtils.joinGroupSession(msg.getGroupId(), ctx.channel());

        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket();
        joinGroupResponsePacket.setGroupId(msg.getGroupId());
        joinGroupResponsePacket.setSuc(suc);
        ctx.channel().writeAndFlush(joinGroupResponsePacket);
    }
}
