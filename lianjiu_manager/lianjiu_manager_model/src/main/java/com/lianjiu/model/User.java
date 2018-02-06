package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 登陆信息
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 昵称
     */
    private String userNickname;
    /**
     * 电话号码
     */
    private String userPhone;
    /**
     * 用户详情编号
     */
    private String userDetailsId;
    /**
     * 用户创建时间
     */
    private Date userCreated;
    /**
     * 用户登陆时间
     */
    private Date userLogined;
   
    private String userPhoto;
    
	public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserNickname() {
        return userNickname;
    }
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getUserDetailsId() {
        return userDetailsId;
    }
    public void setUserDetailsId(String userDetailsId) {
        this.userDetailsId = userDetailsId;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUserCreated() {
		return userCreated;
	}
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setUserCreated(Date userCreated) {
		this.userCreated = userCreated;
	}
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUserLogined() {
		return userLogined;
	}
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setUserLogined(Date userLogined) {
		this.userLogined = userLogined;
	}
    public String getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto == null ? null : userPhoto.trim();
    }
    
}