package com.dto;

public class LeaveQueryDto {
    private String deptId;//部门Id
    private String userId;//工号
    private String username;//姓名
    private String fromTime;//开始日期
    private String toTime;//结束日期

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "LeaveQueryDto{" +
                "deptId='" + deptId + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                '}';
    }
}
