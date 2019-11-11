package cn.edu.jxau.im.handler;

import cn.edu.jxau.im.LoginUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/11
 * Time:上午9:34
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("--- AuthHandler ---");
        if (LoginUtils.hasLogin(ctx.channel())) {
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        } else {
            ctx.channel().close();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {

        if (LoginUtils.hasLogin(ctx.channel())) {
            System.out.println("已通过登录验证，删除AuthHandler");
        } else {
            System.out.println("未通过登录验证，强制关闭连接");
        }
    }
}
