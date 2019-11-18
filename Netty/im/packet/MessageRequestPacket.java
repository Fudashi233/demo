package cn.edu.jxau.im.packet;

import lombok.Data;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/5
 * Time:上午9:12
 */
@Data
public class MessageRequestPacket extends Packet {

    private String message;

    private String toUserId;

    public MessageRequestPacket(String message, String toUserId) {

        this.message = message;
        this.toUserId = toUserId;
    }

    @Override
    public Integer getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
