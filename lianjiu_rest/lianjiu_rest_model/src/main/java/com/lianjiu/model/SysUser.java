package com.lianjiu.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 员工信息表
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SysUser {
    /**
     * 员工编号
     */
    private String sysuserId;
    /**
     * 密码
     */
    private String sysuserPassword;
    /**
     * 电话
     */
    private Integer sysuserPhone;
    /**
     * 姓名
     */
    private String sysuserName;
    /**
     * 类型(0-分拣员  1-回收车人员)
     */
    private String sysuserType;
    /**
     * 最后登录IP
     */
    private String sysuserLoginIp;
    /**
     * 所属部门
     */
    private String sysuserOfficeid;

    public String getSysuserId() {
        return sysuserId;
    }
    public void setSysuserId(String sysuserId) {
        this.sysuserId = sysuserId;
    }
    public String getSysuserPassword() {
        return sysuserPassword;
    }
    public void setSysuserPassword(String sysuserPassword) {
        this.sysuserPassword = sysuserPassword;
    }
    public Integer getSysuserPhone() {
        return sysuserPhone;
    }
    public void setSysuserPhone(Integer sysuserPhone) {
        this.sysuserPhone = sysuserPhone;
    }
    public String getSysuserName() {
        return sysuserName;
    }
    public void setSysuserName(String sysuserName) {
        this.sysuserName = sysuserName;
    }
    public String getSysuserType() {
        return sysuserType;
    }
    public void setSysuserType(String sysuserType) {
        this.sysuserType = sysuserType;
    }
    public String getSysuserLoginIp() {
        return sysuserLoginIp;
    }
    public void setSysuserLoginIp(String sysuserLoginIp) {
        this.sysuserLoginIp = sysuserLoginIp;
    }
    public String getSysuserOfficeid() {
        return sysuserOfficeid;
    }
    public void setSysuserOfficeid(String sysuserOfficeid) {
        this.sysuserOfficeid = sysuserOfficeid;
    }

}