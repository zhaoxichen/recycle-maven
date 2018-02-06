package com.lianjiu.model.vo;

import com.lianjiu.model.User;

public class UserCustom extends User {
	private String qqNickname;
	private String wechatNickname;
	private String udetailsReferrer;

	public String getQqNickname() {
		return qqNickname;
	}

	public void setQqNickname(String qqNickname) {
		this.qqNickname = qqNickname;
	}

	public String getWechatNickname() {
		return wechatNickname;
	}

	public void setWechatNickname(String wechatNickname) {
		this.wechatNickname = wechatNickname;
	}

	public String getUdetailsReferrer() {
		return udetailsReferrer;
	}

	public void setUdetailsReferrer(String udetailsReferrer) {
		this.udetailsReferrer = udetailsReferrer;
	}
	
}
