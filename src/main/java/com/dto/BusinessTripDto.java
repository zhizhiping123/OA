package com.dto;

import com.entity.BusinessTrip;
import com.entity.User;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author:EdenJia
 * @Date：create in 9:05 2017/10/9
 * @Describe: 外出实体DTO
 */
public class BusinessTripDto {
    private long id;
    private String businessId;
    private String departmentId; //与部门的关系，手动维护
    private String describe;
    private String fromTime;
    private String  toTime;
    private String fromLocation;
    private String toLocation;
    private String phoneNum;
    private int personNum;
    private int flag = 0; //'标志符 0 不通过，1通过 默认不通过',
    private String departmentOpinion;
    private String hrOpinion;
    private String carType;
 //   private String processInstanceId; //流程实例Id
    private Date createTime;
    private Date updateTime;
    private Set<UserDto> users;

    //下面三个字段只做查询传参的时候用
    private String userId;
    private String username;
    private String deptName;//部门名称

    //流程数据
    private String taskId;
    private String taskName;
    private Date taskCreateTime;
    private String assignee;
    private String taskDefinitionKey;
    /*流程实例*/
    private String processInstanceId;
    /*流程图定义*/
    private String processDefinitionId;
    private boolean suspended;
    private int version;



    public Specification<BusinessTrip> getWhereClause(final BusinessTripDto businessTripDto){
        return new Specification<BusinessTrip>() {
            @Override
            public Predicate toPredicate(Root<BusinessTrip> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();
                if (businessTripDto.getUserId() != null){
                    Join<BusinessTrip,User> userJoin = root.join("users",JoinType.LEFT);
                    predicates.add(criteriaBuilder.equal(userJoin.get("userId"),businessTripDto.getUserId()));
                }
                if (businessTripDto.getUsername() != null){
                    Join<BusinessTrip,User> userJoin = root.join("users",JoinType.LEFT);
                    predicates.add(criteriaBuilder.equal(userJoin.get("username"),businessTripDto.getUsername()));
                }
                if (businessTripDto.getDepartmentId() != null){
                   predicates.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),businessTripDto.getDepartmentId()));
                }
                if (businessTripDto.getFromTime() != null && businessTripDto.getToTime()!=null){
                    predicates.add(criteriaBuilder.between(root.get("fromTime").as(String.class),businessTripDto.getFromTime(),businessTripDto.getToTime()));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                return criteriaQuery.where(predicates.toArray(pre)).getRestriction();
            }
        };
    }


    public long getId() {
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(Date taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    @Override
    public String toString() {
        return "BusinessTripDto{" +
                "id=" + id +
                ", businessId='" + businessId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", describe='" + describe + '\'' +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                ", fromLocation='" + fromLocation + '\'' +
                ", toLocation='" + toLocation + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", personNum=" + personNum +
                ", flag=" + flag +
                ", departmentOpinion='" + departmentOpinion + '\'' +
                ", hrOpinion='" + hrOpinion + '\'' +
                ", carType='" + carType + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", users=" + users +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", deptName='" + deptName + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskCreateTime=" + taskCreateTime +
                ", assignee='" + assignee + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", suspended=" + suspended +
                ", version=" + version +
                '}';
    }
}
