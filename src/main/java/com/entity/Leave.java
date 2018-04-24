package com.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 11:12 2017/9/29
 * @Describe:请假实体
 */
@Entity
@Table(name = "oa_leave")
public class Leave implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="leave_id")
    private String leaveId;

    @Column(name = "dept_id",length = 10)
    private String departmentId;

    @Column(name = "descs",length = 50)
    private String describe;

    @Column(name = "types")
    private String types;

    @Column(name = "from_time")
    private String fromTime;

    @Column(name = "to_time")
    private String toTime;

    @Column(name = "flag")
    private Integer flag ;//0:不通过;1通过;2审核状态;3取消申请;4保存状态

    @Column(name = "dept_opinion",length = 50)
    private String departmentOpinion;

    @Column(name = "hr_opinion",length = 50)
    private String hrOpinion;

    @Column(name = "process_instance_id",length = 10)
    private String processInstanceId; //流程实例Id

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    //与用户多对多
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(name = "leave_user",joinColumns =
            @JoinColumn(name ="leave_id",referencedColumnName = "leave_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    )
    private Set<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getDepartmentOpinion() {
        return departmentOpinion;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
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
