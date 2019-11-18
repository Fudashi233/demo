package cn.edu.jxau.im.packet;

import lombok.Data;

import java.util.List;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/17
 * Time:下午4:41
 */
@Data
public class CreateGroupResponsePacket extends Packet {

    private Boolean suc;

    private List<String> usernameList;

    private String groupId;

    @Override
    public Integer getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
