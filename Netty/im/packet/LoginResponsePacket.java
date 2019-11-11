package cn.edu.jxau.im.packet;

import lombok.Data;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/4
 * Time:上午9:32
 */
@Data
public class LoginResponsePacket extends Packet {

    private Boolean suc;

    private String msg;

    @Override
    public Integer getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
