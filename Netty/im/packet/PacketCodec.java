package cn.edu.jxau.im.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.Objects;

/**
 * Desc:
 * 魔数（4））+序列化算法（4）+版本号（4）+指令（4）+数据长度（4）+数据
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/3
 * Time:下午3:51
 */
public class PacketCodec {

    private static final Integer MAGIC_NUMBER = 0x12345678;

    public static ByteBuf encode(Packet packet) {

        byte[] data = Serializer.DEFAULT.serialize(packet);
        return wrap(MAGIC_NUMBER, Serializer.DEFAULT.getSerializerAlgorithm(),
                packet.getVersion(), packet.getCommand(), data.length, data);
    }

    public static ByteBuf encode(Packet packet, ByteBuf byteBuf) {

        byte[] data = Serializer.DEFAULT.serialize(packet);
        return wrap(byteBuf, MAGIC_NUMBER, Serializer.DEFAULT.getSerializerAlgorithm(),
                packet.getVersion(), packet.getCommand(), data.length, data);
    }

    private static ByteBuf wrap(int magic, int serializeAlgorithm, int version, int command, int len, byte[] data) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.ioBuffer();
        buf.writeInt(magic);
        buf.writeInt(serializeAlgorithm);
        buf.writeInt(version);
        buf.writeInt(command);
        buf.writeInt(len);
        buf.writeBytes(data);
        return buf;
    }

    private static ByteBuf wrap(ByteBuf buf, int magic, int serializeAlgorithm, int version, int command, int len, byte[] data) {

        buf.writeInt(magic);
        buf.writeInt(serializeAlgorithm);
        buf.writeInt(version);
        buf.writeInt(command);
        buf.writeInt(len);
        buf.writeBytes(data);
        return buf;
    }

    public static Packet decode(ByteBuf byteBuf) {

        int magicNumber = byteBuf.readInt();
        int serializerAlgorithm = byteBuf.readInt();
        int version = byteBuf.readInt();
        int command = byteBuf.readInt();
        int length = byteBuf.readInt();
        byte[] data = new byte[length];
        byteBuf.readBytes(data);
        Class<? extends Packet> klass = getPacketType(command);
        Serializer serializer = getSerializer(serializerAlgorithm);
        return serializer.deserialize(klass, data);
    }

    private static Class<? extends Packet> getPacketType(Integer command) {

        if (Objects.equals(command, Command.LOGIN_REQUEST)) {
            return LoginRequestPacket.class;
        } else if (Objects.equals(command, Command.LOGIN_RESPONSE)) {
            return LoginResponsePacket.class;
        } else if (Objects.equals(command, Command.MESSAGE_REQUEST)) {
            return MessageRequestPacket.class;
        } else if (Objects.equals(command, Command.MESSAGE_RESPONSE)) {
            return MessageResponsePacket.class;
        } else if (Objects.equals(command, Command.CREATE_GROUP_REQUEST)) {
            return CreateGroupRequestPacket.class;
        } else if (Objects.equals(command, Command.CREATE_GROUP_RESPONSE)) {
            return CreateGroupResponsePacket.class;
        } else if (Objects.equals(command, Command.JOIN_GROUP_REQUEST)) {
            return JoinGroupRequestPacket.class;
        } else if (Objects.equals(command, Command.JOIN_GROUP_RESPONSE)) {
            return JoinGroupResponsePacket.class;
        } else if (Objects.equals(command, Command.GROUP_MESSAGE_REQUEST)) {
            return GroupMessageRequestPacket.class;
        } else if (Objects.equals(command, Command.GROUP_MESSAGE_RESPONSE)) {
            return GroupMessageResponsePacket.class;
        } else {
            throw new RuntimeException("不支持的command，command=" + command);
        }
    }

    private static Serializer getSerializer(Integer serialAlgorithm) {

        if (Objects.equals(serialAlgorithm, SerializerAlgorithm.JSON_SERIALIZER)) {
            return new JSONSerializer();
        } else {
            throw new RuntimeException("不支持的serialAlgorithm，serialAlgorithm=" + serialAlgorithm);
        }
    }
}
