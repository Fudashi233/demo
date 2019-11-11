package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.LoginUtils;
import cn.edu.jxau.im.packet.LoginResponsePacket;
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
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
        if (msg.getSuc()) {
            LoginUtils.markAsLogin(ctx.channel());
        }
    }
}
