package com.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 11:23 2017/9/29
 * @Describe:报餐实体
 */
@Entity
@Table(name = "oa_eating")
public class Eating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="eating_id")
    private String eatingId;

    @Column(name = "dept_id",length = 10)
    private String departmentId;

    @Column(name = "lunch")
    private int lunch;

    @Column(name = "dinner")
    private int dinner;

    @Column(name = "night_snack")
    private int nightSnack;

    @Column(name = "breakfast")
    private int breakfast;

    @Column(name = "hr_opinion")
    private String hrOpinion;

    @Column(name = "flag")
    private int flag; // '标志符 0 不通过，1通过 默认不通过'

    @Column(name = "process_instance_id",length = 10)
    private String processInstanceId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date creatTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTime")
    private Date updateTime;

    //用餐与用户是多对多
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "eating_user",joinColumns =
             @JoinColumn(name ="eating_id",referencedColumnName = "eating_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    )
    private Set<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEatingId() {
        return eatingId;
    }

    public void setEatingId(String eatingId) {
        this.eatingId = eatingId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getLunch() {
        return lunch;
    }

    public void setLunch(int lunch) {
        this.lunch = lunch;
    }

    public int getDinner() {
        return dinner;
    }

    public void setDinner(int dinner) {
        this.dinner = dinner;
    }

    public int getNightSnack() {
        return nightSnack;
    }

    public void setNightSnack(int nightSnack) {
        this.nightSnack = nightSnack;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    public String getHrOpinion() {
        return hrOpinion;
    }

    public void setHrOpinion(String hrOpinion) {
        this.hrOpinion = hrOpinion;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
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
