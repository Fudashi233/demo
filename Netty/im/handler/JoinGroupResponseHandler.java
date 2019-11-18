package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.packet.JoinGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午5:16
 */
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket msg) throws Exception {

        if (Objects.equals(Boolean.TRUE, msg.getSuc())) {
            System.out.println("群组加入成功：" + msg);
        } else {
            System.out.println("群组加入失败");
        }
    }
}
