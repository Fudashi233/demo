package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.LoginUtils;
import cn.edu.jxau.im.Session;
import cn.edu.jxau.im.SessionUtils;
import cn.edu.jxau.im.packet.LoginRequestPacket;
import cn.edu.jxau.im.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;
import java.util.UUID;

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

        Session session = buildSession(msg);
        LoginResponsePacket loginResponsePacket = login(session);
        if (loginResponsePacket.getSuc()) {
            SessionUtils.bindSession(ctx.channel(), session);
            System.out.println("登录成功，session=" + session);
        }
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private Session buildSession(LoginRequestPacket msg) {

        Session session = new Session();
        session.setUserId(UUID.randomUUID().toString());
        session.setUsername(msg.getUsername());
        return session;
    }

    private LoginResponsePacket login(Session session) {

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        if (Objects.nonNull(session)) {
            loginResponsePacket.setMsg("登录成功，userId=" + session.getUserId());
            loginResponsePacket.setSuc(true);
            loginResponsePacket.setUserId(session.getUserId());
            loginResponsePacket.setUsername(session.getUsername());
        } else {
            loginResponsePacket.setMsg("登录失败");
            loginResponsePacket.setSuc(false);
        }
        return loginResponsePacket;
    }
}
