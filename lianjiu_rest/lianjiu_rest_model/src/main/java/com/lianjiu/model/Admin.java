package com.lianjiu.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 登陆信息
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Admin {
    /**
     * 用户编号
     */
    private String adminId;
    /**
     * 权限
     */
    private Integer adminPriority;
    /**
     * 用户名称
     */
    private String adminUsername;
    /**
     * 用户密码
     */
    private String adminPassword;
    /**
     * 昵称
     */
    private String adminNickname;
    /**
     * 用户电话号码
     */
    private String adminPhone;
    /**
     * 外键，用户详情
     */
    private String adminDid;

    public String getAdminId() {
        return adminId;
    }
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
    public Integer getAdminPriority() {
        return adminPriority;
    }
    public void setAdminPriority(Integer adminPriority) {
        this.adminPriority = adminPriority;
    }
    public String getAdminUsername() {
        return adminUsername;
    }
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }
    public String getAdminPassword() {
        return adminPassword;
    }
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
    public String getAdminNickname() {
        return adminNickname;
    }
    public void setAdminNickname(String adminNickname) {
        this.adminNickname = adminNickname;
    }
    public String getAdminPhone() {
        return adminPhone;
    }
    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }
    public String getAdminDid() {
        return adminDid;
    }
    public void setAdminDid(String adminDid) {
        this.adminDid = adminDid;
    }

}