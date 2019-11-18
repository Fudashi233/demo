package cn.edu.jxau.im.packet;

import lombok.Data;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午5:11
 */
@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Integer getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
