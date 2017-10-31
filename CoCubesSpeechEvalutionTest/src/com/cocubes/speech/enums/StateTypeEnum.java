/*
 * /*region CoCubes Copyright Details
 *
 *  // /////////////////////////////////////////////////////////////////////////////////////////////////////
 *  //
 *  //
 *  //  All rights reserved by CoCubes.com
 *  //
 *  //
 *  //  (c) Copyright 2008-2017 CoCubes Technologies Pvt. Ltd.,
 *  //    http://www.cocubes.com/
 *  //
 *  //
 *  // ////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 */
package com.cocubes.speech.enums;

public enum StateTypeEnum {
    Transition((byte) 1), Start((byte) 2), Final((byte) 4);
    private final byte type;

    StateTypeEnum(byte type) {
        this.type = type;
    }
}
