package com.common;

/**
 * create  by zzping
 */
public enum  LeaveStatus {
    UNPASS(0,"未通过"),//未通过
    PASS(1,"通过"),//通过
    CHECK(2,"审核中"),//审核中
    NEW(3,"新增");//新增
    private int status;
    private String msg;

    private LeaveStatus(int status, String msg){
       this.status=status;
       this.msg=msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
