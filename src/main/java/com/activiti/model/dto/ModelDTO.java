package com.activiti.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ModelDTO {

    private String modelId;
    private String name;
    private String key;
    private String desc;

    @JsonFormat(pattern = "yyyy/mm/dd hh:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy/mm/dd hh:mm:ss")
    private Date lastUpdateTime;

    public ModelDTO() {
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
