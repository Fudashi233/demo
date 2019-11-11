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
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Integer getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
