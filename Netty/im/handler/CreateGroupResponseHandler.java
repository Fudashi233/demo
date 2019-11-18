package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.packet.CreateGroupRequestPacket;
import cn.edu.jxau.im.packet.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:40
 */

public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket msg) throws Exception {

        if (msg.getSuc()) {
            System.out.println("群组创建成功：" + msg);
        } else {
            System.out.println("群组创建失败");
        }

    }
}
