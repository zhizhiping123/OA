package com.dto;

import com.dao.UserRepository;
import com.entity.Leave;
import com.common.LeaveType;
import com.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * create by zzping
 */
public class LeaveDto {
private String userId;

private String username;

private String deptName;

private String deptId;

private String leaveId;

private String describe;

private LeaveType types;

private Integer flag;

private String fromTime;

private String toTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
private Boolean personal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
private String departmentOpinion=null;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
private String hrOpinion=null;

@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
private Date createTime;

@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
private Date updateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
private String processInstanceId=null;

private String processDefinitionId;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
private String taskId=null;
/*@JsonProperty("status")*/

@JsonInclude(JsonInclude.Include.NON_EMPTY)
private String taskName=null;

public static final void dtoToEntity(LeaveDto leaveDto, Leave entity){
    Date createTime=entity.getCreateTime();
    BeanUtils.copyProperties(leaveDto,entity);
    entity.setTypes(leaveDto.getTypes().name());
    if (createTime!=null){
        entity.setCreateTime(createTime);
    }


}

public static final  void entityToDto(Leave entity,LeaveDto leaveDto){

    BeanUtils.copyProperties(entity,leaveDto);
    leaveDto.setTypes(LeaveType.valueOf(entity.getTypes()));


}
public static final  void entityListToDtoList(List<Leave> entityList,List<LeaveDto> leaveDtoList){

    for (Leave leave:entityList){
        LeaveDto leaveDto = new LeaveDto();
        LeaveDto.entityToDto(leave,leaveDto);
        leaveDtoList.add(leaveDto);

    }

}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeavId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public LeaveType getTypes() {
        return types;
    }

    public void setTypes(LeaveType types) {
        this.types = types;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
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

    public Boolean isPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
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

    @Override
    public String toString() {
        return "LeaveDto{" +
                "leaveId='" + leaveId + '\'' +

                ", describe='" + describe + '\'' +
                ", types=" + types +
                ", flag=" + flag +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                ", departmentOpinion='" + departmentOpinion + '\'' +
                ", hrOpinion='" + hrOpinion + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userId='" + userId + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
