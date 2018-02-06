package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * 加盟商登陆信息
 * @author jdandian.com
 * @date 2017年09月06日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AllianceBusiness {
    /**
     * 加盟商编号
     */
    private String allianceBusinessId;
    /**
     * 加盟商名称
     */
    private String allianceBusinessName;
    /**
     * 加盟商密码
     */
    private String allianceBusinessPassword;
    /**
     * 昵称
     */
    private String allianceBusinessNickname;
    /**
     * 用戶头像
     */
    private String allianceBusunessPhoto;
    /**
     * 电话号码
     */
    private String allianceBusinessPhone;
    /**
     * 加盟商详情编号
     */
    private String allianceBusinessDetailsId;
    /**
     * 加盟商创建时间
     */
    private Date allianceBusinessCreated;
    /**
     * 加盟商更新时间
     */
    private Date allianceBusinessUpdated;
    /**
     * 加盟商登陆时间
     */
    private Date allianceBusinessLogined;
    /**
     * 加盟商对应设备编号
     */
    private String allianceServiceToken;
    
	public String getAllianceBusinessId() {
		return allianceBusinessId;
	}
	public String setAllianceBusinessId(String allianceBusinessId) {
		return this.allianceBusinessId = allianceBusinessId;
	}
	public String getAllianceBusinessName() {
		return allianceBusinessName;
	}
	public void setAllianceBusinessName(String allianceBusinessName) {
		this.allianceBusinessName = allianceBusinessName;
	}
	public String getAllianceBusinessPassword() {
		return allianceBusinessPassword;
	}
	public void setAllianceBusinessPassword(String allianceBusinessPassword) {
		this.allianceBusinessPassword = allianceBusinessPassword;
	}
	public String getAllianceBusinessNickname() {
		return allianceBusinessNickname;
	}
	public void setAllianceBusinessNickname(String allianceBusinessNickname) {
		this.allianceBusinessNickname = allianceBusinessNickname;
	}
    public String getAllianceBusunessPhoto() {
        return allianceBusunessPhoto;
    }

    public void setAllianceBusunessPhoto(String allianceBusunessPhoto) {
        this.allianceBusunessPhoto = allianceBusunessPhoto == null ? null : allianceBusunessPhoto.trim();
    }
	public String getAllianceBusinessPhone() {
		return allianceBusinessPhone;
	}
	public void setAllianceBusinessPhone(String allianceBusinessPhone) {
		this.allianceBusinessPhone = allianceBusinessPhone;
	}
	public String getAllianceBusinessDetailsId() {
		return allianceBusinessDetailsId;
	}
	public void setAllianceBusinessDetailsId(String allianceBusinessDetailsId) {
		this.allianceBusinessDetailsId = allianceBusinessDetailsId;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAllianceBusinessCreated() {
		return allianceBusinessCreated;
	}
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setAllianceBusinessCreated(Date allianceBusinessCreated) {
		this.allianceBusinessCreated = allianceBusinessCreated;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAllianceBusinessUpdated() {
		return allianceBusinessUpdated;
	}
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setAllianceBusinessUpdated(Date allianceBusinessUpdated) {
		this.allianceBusinessUpdated = allianceBusinessUpdated;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAllianceBusinessLogined() {
		return allianceBusinessLogined;
	}
	 @DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setAllianceBusinessLogined(Date allianceBusinessLogined) {
		this.allianceBusinessLogined = allianceBusinessLogined;
	}
	public String getAllianceServiceToken() {
			return allianceServiceToken;
	}
	public void setAllianceServiceToken(String allianceServiceToken) {
			this.allianceServiceToken = allianceServiceToken;
	}
	
    
}