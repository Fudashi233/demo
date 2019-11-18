package cn.edu.jxau.im.packet;

import lombok.Data;

import java.util.List;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:22
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    private String[] userIdList;

    @Override
    public Integer getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
