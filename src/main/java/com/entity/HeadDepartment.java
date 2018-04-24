package com.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author:EdenJia
 * @Date：create in 14:38 2017/9/29
 * @Describe: 总部实体
 */

@Entity
@Table(name = "oa_head_department")
public class HeadDepartment  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "head_dept_id")
    private String headDepartmentId;

    @Column(name = "head_dept_name",length = 10)
    private String headDepartmentName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeadDepartmentId() {
        return headDepartmentId;
    }

    public void setHeadDepartmentId(String headDepartmentId) {
        this.headDepartmentId = headDepartmentId;
    }

    public String getHeadDepartmentName() {
        return headDepartmentName;
    }

    public void setHeadDepartmentName(String headDepartmentName) {
        this.headDepartmentName = headDepartmentName;
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

}
