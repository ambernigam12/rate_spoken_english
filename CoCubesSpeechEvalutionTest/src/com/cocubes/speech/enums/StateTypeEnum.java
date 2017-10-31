package com.cocubes.speech.enums;

public enum StateTypeEnum {
    Transition((byte) 1), Start((byte) 2), Final((byte) 4);
    private final byte type;

    StateTypeEnum(byte type) {
        this.type = type;
    }
}
