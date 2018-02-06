package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Role {
    private String roleId;

    private String roleName;

    private String roleCode;

    private String rolePassword;

    private Integer roleJob;

    private String roleUp;

    private String roleHandler;

    private Date roleCreated;

    private Date roleUpdated;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    public String getRolePassword() {
        return rolePassword;
    }

    public void setRolePassword(String rolePassword) {
        this.rolePassword = rolePassword == null ? null : rolePassword.trim();
    }

    public Integer getRoleJob() {
        return roleJob;
    }

    public void setRoleJob(Integer roleJob) {
        this.roleJob = roleJob;
    }

    public String getRoleUp() {
        return roleUp;
    }

    public void setRoleUp(String roleUp) {
        this.roleUp = roleUp == null ? null : roleUp.trim();
    }

    public String getRoleHandler() {
        return roleHandler;
    }

    public void setRoleHandler(String roleHandler) {
        this.roleHandler = roleHandler == null ? null : roleHandler.trim();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getRoleCreated() {
        return roleCreated;
    }

    public void setRoleCreated(Date roleCreated) {
        this.roleCreated = roleCreated;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getRoleUpdated() {
        return roleUpdated;
    }

    public void setRoleUpdated(Date roleUpdated) {
        this.roleUpdated = roleUpdated;
    }
}