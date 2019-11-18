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
public class GroupMessageRequestPacket extends Packet {

    private String groupId;

    private String msg;

    public GroupMessageRequestPacket() {

    }

    public GroupMessageRequestPacket(String groupId, String msg) {
        this.groupId = groupId;
        this.msg = msg;
    }

    @Override
    public Integer getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
