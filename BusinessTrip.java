package com.entity;



import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 10:49 2017/9/29
 * @Describe:外出实体
 */
@Entity
@Table(name = "oa_business_trip")
public class BusinessTrip implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "dept_id",length = 10)
    private String departmentId; //与部门的关系，手动维护

    @Column(name = "descs",length = 50)
    private String describe;


    @Column(name = "from_time")
    private String fromTime;

    @Column(name = "to_time")
    private String  toTime;

    @Column(name = "from_location")
    private String fromLocation;

    @Column(name = "to_location")
    private String toLocation;

    @Column(name = "phone_num",length = 11)
    private String phoneNum;

    @Column(name = "flag")
    private int flag = 0; //'标志符 0 不通过，1通过 默认不通过',

    @Column(name = "dept_opinion",length = 50)
    private String departmentOpinion;

    @Column(name = "hr_opinion",length = 50)
    private String hrOpinion;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "process_instance_id",length = 10)
    private String processInstanceId; //流程实例Id

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    //与用户多对多
    @ManyToMany(cascade ={CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "business_user",joinColumns =
             @JoinColumn(name ="business_id",referencedColumnName = "business_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    )
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public int getFlag() {
        return flag;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public String getFromTime() {
        return fromTime;
    }

}
