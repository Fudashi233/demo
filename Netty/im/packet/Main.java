package cn.edu.jxau.im.packet;

import io.netty.buffer.ByteBuf;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/3
 * Time:下午3:48
 */
public class Main {

    public static void main(String[] args) {

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket("");
        loginRequestPacket.setUsername("Fudashi233");

        PacketCodec packetCodec = new PacketCodec();
        ByteBuf byteBuf = packetCodec.encode(loginRequestPacket);

        Packet packet = packetCodec.decode(byteBuf);
        loginRequestPacket = (LoginRequestPacket) packet;
        System.out.println(loginRequestPacket);
    }
}
