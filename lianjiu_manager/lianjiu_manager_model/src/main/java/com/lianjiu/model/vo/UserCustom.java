package com.lianjiu.model.vo;

import com.lianjiu.model.User;

public class UserCustom extends User {
	private String qqNickname;
	private String wechatNickname;

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
}
