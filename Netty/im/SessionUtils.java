package cn.edu.jxau.im;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午2:28
 */
public class SessionUtils {

    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> CHANNEL_GROUP_MAP = new ConcurrentHashMap<>();

    public static void bindSession(Channel channel, Session session) {

        CHANNEL_MAP.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void createGroupSession(String groupId, ChannelGroup channelGroup) {
        CHANNEL_GROUP_MAP.put(groupId, channelGroup);
    }

    public static boolean joinGroupSession(String groupId, Channel channel) {

        ChannelGroup channelGroup = CHANNEL_GROUP_MAP.get(groupId);
        if (Objects.nonNull(channelGroup)) {
            channelGroup.add(channel);
            CHANNEL_GROUP_MAP.put(groupId, channelGroup);
            return true;
        } else {
            return false;
        }
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return CHANNEL_GROUP_MAP.get(groupId);
    }

    public static void unbindSession(Channel channel, Session session) {

        if (hasLogin(channel)) {
            CHANNEL_MAP.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return Objects.nonNull(channel.attr(Attributes.SESSION)) && Objects.nonNull(channel.attr(Attributes.SESSION).get());
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return CHANNEL_MAP.get(userId);
    }

}
