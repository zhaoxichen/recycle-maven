package com.lianjiu.model;

public class DeptRole {
    private String deptRoleId;

    private String deptId;

    private String roleId;

    public String getDeptRoleId() {
        return deptRoleId;
    }

    public void setDeptRoleId(String deptRoleId) {
        this.deptRoleId = deptRoleId == null ? null : deptRoleId.trim();
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
}