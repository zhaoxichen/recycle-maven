package com.lianjiu.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RolePower {
    private String rolePowerId;

    private String roleId;

    private String powerId;

    public String getRolePowerId() {
        return rolePowerId;
    }

    public void setRolePowerId(String rolePowerId) {
        this.rolePowerId = rolePowerId == null ? null : rolePowerId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId == null ? null : powerId.trim();
    }
}