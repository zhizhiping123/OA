package com.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 11:20 2017/9/29
 * @Describe:加班实体
 */

@Entity
@Table(name = "oa_overtime")
public class OverTime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "overtime_id")
    private String overTimeId;

    @Column(name = "dept_id",length = 10)
    private String departmentId;

    @Column(name = "descs",length = 50)
    private String describe;

    @Column(name = "from_time")
    private String fromTime;

    @Column(name = "to_time")
    private String  toTime;

    @Column(name = "flag")
    private int flag = 0; //'标志符 0 不通过，1通过 默认不通过',

    @Column(name = "dept_opinion",length = 50)
    private String departmentOpinion;

    @Column(name = "hr_opinion",length = 50)
    private String hrOpinion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "real_time")
    private Date realTime;

    @Column(name = "process_instance_id",length = 10)
    private String processInstanceId; //流程实例Id

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    //与用户多对多
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "overtime_user",joinColumns =
            @JoinColumn(name ="overtime_id",referencedColumnName = "overtime_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    )
    private Set<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOverTimeId() {
        return overTimeId;
    }

    public void setOverTimeId(String overTimeId) {
        this.overTimeId = overTimeId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getDepartmentOpinion() {
        return departmentOpinion;
    }

    public void setDepartmentOpinion(String departmentOpinion) {
        this.departmentOpinion = departmentOpinion;
    }

    public String getHrOpinion() {
        return hrOpinion;
    }

    public void setHrOpinion(String hrOpinion) {
        this.hrOpinion = hrOpinion;
    }

    public Date getRealTime() {
        return realTime;
    }

    public void setRealTime(Date realTime) {
        this.realTime = realTime;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
