package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.packet.GroupMessageResponsePacket;
import cn.edu.jxau.im.packet.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午5:58
 */
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket msg) throws Exception {
        System.out.printf("【群组】【%s（%s）】%s\n", msg.getFromUsername(), msg.getFromUserId(), msg.getMsg());
    }
}
