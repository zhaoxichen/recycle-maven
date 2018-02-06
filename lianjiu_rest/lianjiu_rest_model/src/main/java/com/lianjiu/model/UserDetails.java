package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 用户信息表
 * 
 * @author jdandian.com
 * @date 2017年09月01日
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserDetails {
	/**
	 * 表格id号 被user表关联
	 */
	private String udetailsId;
	/**
	 * 身份证号码
	 */
	private String udetailsIdCard;
	/**
	 * 用户身份注册地址
	 */
	private String udetailsUserAddress;
	/**
	 * 实名认证时间
	 */
	private Date udetailsRealnameTime;
	/**
	 * 银行卡号
	 */
	private String udetailsCardNo;
	/**
	 * 开户行
	 */
	private String udetailsCardBank;
	/**
	 * 积分
	 */
	private Integer udetailsIntegral;
	/**
	 * 账户金额
	 */
	private Integer udetailsAccountBalance;
	/**
	 * 金额明细(1.上门回收 2.箱体回收 3.快递回收 4.购物支出 5.提现支出)
	 */
	private String udetailsMoneyDetails;
	/**
	 * 注册时间
	 */
	private Date udetailsRegisterTime;
	/**
	 * 登陆设备(1.PC 2.安卓 3.苹果 4.H5)
	 */
	private String udetailsEquipment;
	/**
	 * 版本
	 */
	private String udetailsEdition;
	/**
	 * 推荐人
	 */
	private String udetailsReferrer;
	/**
	 * 头像
	 */
	private String udetailsPhoto;
	/**
	 * 优惠券编号
	 */
	private String couponId;
	/**
	 * 真实姓名
	 */
	private String userName;
	/**
	 * 用户电话号码
	 */
	private String userPhonenum;
	/**
	 * 用户对应的唯一微信openid
	 */
	private String userOpenid;
	/**
	 * 用户实名认证状态
	 */
	private Byte isIdCard;

	/**
	 * h5提现验证码
	 */
	private String userCheck;
	/**
	 * 验证码检测时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date checkTime;
	/**
	 * 是否首单
	 */
	private Integer isFirstOrder;
	/**
	 * 绑定银行卡时间
	 */
	private Date udetailsCardTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUdetailsCardTime() {
		return udetailsCardTime;
	}

	public void setUdetailsCardTime(Date udetailsCardTime) {
		this.udetailsCardTime = udetailsCardTime;
	}

	public Integer getIsFirstOrder() {
		return isFirstOrder;
	}

	public void setIsFirstOrder(Integer isFirstOrder) {
		this.isFirstOrder = isFirstOrder;
	}

	public String getUserCheck() {
		return userCheck;
	}

	public void setUserCheck(String userCheck) {
		this.userCheck = userCheck;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getUserOpenid() {
		return userOpenid;
	}

	public void setUserOpenid(String userOpenid) {
		this.userOpenid = userOpenid;
	}

	public String getUdetailsId() {
		return udetailsId;
	}

	public void setUdetailsId(String udetailsId) {
		this.udetailsId = udetailsId;
	}

	public String getUdetailsIdCard() {
		return udetailsIdCard;
	}

	public void setUdetailsIdCard(String udetailsIdCard) {
		this.udetailsIdCard = udetailsIdCard;
	}

	public String getUdetailsUserAddress() {
		return udetailsUserAddress;
	}

	public void setUdetailsUserAddress(String udetailsUserAddress) {
		this.udetailsUserAddress = udetailsUserAddress;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUdetailsRealnameTime() {
		return udetailsRealnameTime;
	}

	public void setUdetailsRealnameTime(Date udetailsRealnameTime) {
		this.udetailsRealnameTime = udetailsRealnameTime;
	}

	public String getUdetailsCardNo() {
		return udetailsCardNo;
	}

	public void setUdetailsCardNo(String udetailsCardNo) {
		this.udetailsCardNo = udetailsCardNo;
	}

	public String getUdetailsCardBank() {
		return udetailsCardBank;
	}

	public void setUdetailsCardBank(String udetailsCardBank) {
		this.udetailsCardBank = udetailsCardBank;
	}

	public Integer getUdetailsIntegral() {
		return udetailsIntegral;
	}

	public void setUdetailsIntegral(Integer udetailsIntegral) {
		this.udetailsIntegral = udetailsIntegral;
	}

	public Integer getUdetailsAccountBalance() {
		return udetailsAccountBalance;
	}

	public void setUdetailsAccountBalance(Integer udetailsAccountBalance) {
		this.udetailsAccountBalance = udetailsAccountBalance;
	}

	public String getUdetailsMoneyDetails() {
		return udetailsMoneyDetails;
	}

	public void setUdetailsMoneyDetails(String udetailsMoneyDetails) {
		this.udetailsMoneyDetails = udetailsMoneyDetails;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUdetailsRegisterTime() {
		return udetailsRegisterTime;
	}

	public void setUdetailsRegisterTime(Date udetailsRegisterTime) {
		this.udetailsRegisterTime = udetailsRegisterTime;
	}

	public String getUdetailsEquipment() {
		return udetailsEquipment;
	}

	public void setUdetailsEquipment(String udetailsEquipment) {
		this.udetailsEquipment = udetailsEquipment;
	}

	public String getUdetailsEdition() {
		return udetailsEdition;
	}

	public void setUdetailsEdition(String udetailsEdition) {
		this.udetailsEdition = udetailsEdition;
	}

	public String getUdetailsReferrer() {
		return udetailsReferrer;
	}

	public void setUdetailsReferrer(String udetailsReferrer) {
		this.udetailsReferrer = udetailsReferrer;
	}

	public String getUdetailsPhoto() {
		return udetailsPhoto;
	}

	public void setUdetailsPhoto(String udetailsPhoto) {
		this.udetailsPhoto = udetailsPhoto;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhonenum() {
		return userPhonenum;
	}

	public void setUserPhonenum(String userPhonenum) {
		this.userPhonenum = userPhonenum;
	}

	public Byte getIsIdCard() {
		return isIdCard;
	}

	public void setIsIdCard(Byte isIdCard) {
		this.isIdCard = isIdCard;
	}

	@Override
	public String toString() {
		return "UserDetails [udetailsId=" + udetailsId + ", udetailsIdCard=" + udetailsIdCard + ", udetailsUserAddress="
				+ udetailsUserAddress + ", udetailsRealnameTime=" + udetailsRealnameTime + ", udetailsCardNo="
				+ udetailsCardNo + ", udetailsCardBank=" + udetailsCardBank + ", udetailsIntegral=" + udetailsIntegral
				+ ", udetailsAccountBalance=" + udetailsAccountBalance + ", udetailsMoneyDetails="
				+ udetailsMoneyDetails + ", udetailsRegisterTime=" + udetailsRegisterTime + ", udetailsEquipment="
				+ udetailsEquipment + ", udetailsEdition=" + udetailsEdition + ", udetailsReferrer=" + udetailsReferrer
				+ ", udetailsPhoto=" + udetailsPhoto + ", couponId=" + couponId + ", userName=" + userName
				+ ", userPhonenum=" + userPhonenum + ", userOpenid=" + userOpenid + ", isIdCard=" + isIdCard
				+ ", userCheck=" + userCheck + ", checkTime=" + checkTime + "]";
	}

}