package cn.edu.jxau.im.packet;

import lombok.Data;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/3
 * Time:下午3:24
 */
@Data
public abstract class Packet {

    private Integer version = 1;

    public abstract Integer getCommand();
}
