package cn.edu.jxau.im.packet;

public interface Command {

    Integer LOGIN_REQUEST = 1;

    Integer LOGIN_RESPONSE = 2;

    Integer MESSAGE_REQUEST = 3;

    Integer MESSAGE_RESPONSE = 4;

    Integer CREATE_GROUP_REQUEST = 5;

    Integer CREATE_GROUP_RESPONSE = 6;

    Integer JOIN_GROUP_REQUEST = 7;

    Integer JOIN_GROUP_RESPONSE = 8;

    Integer GROUP_MESSAGE_REQUEST = 9;

    Integer GROUP_MESSAGE_RESPONSE = 10;
}
