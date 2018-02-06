package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Dept {
    private String deptId;

    private String deptName;

    private String deptOperated;

    private Date deptCreated;

    private Date deptUpdated;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getDeptOperated() {
        return deptOperated;
    }

    public void setDeptOperated(String deptOperated) {
        this.deptOperated = deptOperated == null ? null : deptOperated.trim();
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getDeptCreated() {
        return deptCreated;
    }

    public void setDeptCreated(Date deptCreated) {
        this.deptCreated = deptCreated;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getDeptUpdated() {
        return deptUpdated;
    }

    public void setDeptUpdated(Date deptUpdated) {
        this.deptUpdated = deptUpdated;
    }
}