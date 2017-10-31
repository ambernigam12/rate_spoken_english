package com.cocubes.speech.enums;

public enum PauseTypeEnum {

    ShortSilentPause(1), LongSilentPause(2), ShortFilledPause(4), LongFilledPause(8);
    private final int type;

    PauseTypeEnum(int type) {
        this.type = type;
    }
}
