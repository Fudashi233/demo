package cn.edu.jxau.im;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

import java.util.Objects;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/5
 * Time:上午8:56
 */
public class LoginUtils {

    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> attr = channel.attr(Attributes.LOGIN);
        return attr != null && Objects.equals(Boolean.TRUE,attr.get());
    }
}
