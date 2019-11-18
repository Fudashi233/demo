package cn.edu.jxau.im.packet;

import lombok.Data;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午5:55
 */
@Data
public class GroupMessageResponsePacket extends Packet {

    private String groupId;
    private String fromUsername;
    private String fromUserId;
    private String msg;

    @Override
    public Integer getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
