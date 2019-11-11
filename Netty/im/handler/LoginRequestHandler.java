package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.LoginUtils;
import cn.edu.jxau.im.packet.LoginRequestPacket;
import cn.edu.jxau.im.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/10
 * Time:下午3:04
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {


        LoginResponsePacket loginResponsePacket = login(msg);
        if (loginResponsePacket.getSuc()) {
            LoginUtils.markAsLogin(ctx.channel());
        }
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private LoginResponsePacket login(LoginRequestPacket loginRequestPacket) {

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setMsg("登录成功");
        loginResponsePacket.setSuc(true);

        return loginResponsePacket;
    }
}
