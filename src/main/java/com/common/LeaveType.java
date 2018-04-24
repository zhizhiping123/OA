package com.common;

/**
 * create by zzping
 */
public enum LeaveType {
    A("事假"),
    B("病假"),
    C("婚假"),
    D("年假"),
    E("产假"),
    F("其他");

private  final String type;

LeaveType(String type){
    this.type=type;
}

    public String getType() {
        return type;
    }
}
