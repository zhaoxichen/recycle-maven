package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Role {
    private String roleId;

    private String roleName;

    private Date roleCreated;

    private Date roleUpdated;

    private String roleHandler;

    private String roleRemarks;

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

    public Date getRoleCreated() {
        return roleCreated;
    }

    public void setRoleCreated(Date roleCreated) {
        this.roleCreated = roleCreated;
    }

    public Date getRoleUpdated() {
        return roleUpdated;
    }

    public void setRoleUpdated(Date roleUpdated) {
        this.roleUpdated = roleUpdated;
    }

    public String getRoleHandler() {
        return roleHandler;
    }

    public void setRoleHandler(String roleHandler) {
        this.roleHandler = roleHandler == null ? null : roleHandler.trim();
    }

    public String getRoleRemarks() {
        return roleRemarks;
    }

    public void setRoleRemarks(String roleRemarks) {
        this.roleRemarks = roleRemarks == null ? null : roleRemarks.trim();
    }
}