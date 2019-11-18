package cn.edu.jxau.im.packet;

import lombok.Data;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/3
 * Time:下午3:39
 */
@Data
public class LoginRequestPacket extends Packet {

    private String username;

    public LoginRequestPacket() {

    }

    public LoginRequestPacket(String username) {
        this.username = username;
    }

    @Override
    public Integer getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
