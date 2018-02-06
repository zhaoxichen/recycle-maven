package com.lianjiu.model;

import java.util.Date;

public class Wechat {
    private String wechatId;

    private String wechatNum;

    private String wechatNickname;

    private String wechatPicture;

    private String userId;

    private Date wechatCreated;

    private Date wechatUpdated;

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId == null ? null : wechatId.trim();
    }

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum == null ? null : wechatNum.trim();
    }

    public String getWechatNickname() {
        return wechatNickname;
    }

    public void setWechatNickname(String wechatNickname) {
        this.wechatNickname = wechatNickname == null ? null : wechatNickname.trim();
    }

    public String getWechatPicture() {
        return wechatPicture;
    }

    public void setWechatPicture(String wechatPicture) {
        this.wechatPicture = wechatPicture == null ? null : wechatPicture.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getWechatCreated() {
        return wechatCreated;
    }

    public void setWechatCreated(Date wechatCreated) {
        this.wechatCreated = wechatCreated;
    }

    public Date getWechatUpdated() {
        return wechatUpdated;
    }

    public void setWechatUpdated(Date wechatUpdated) {
        this.wechatUpdated = wechatUpdated;
    }
}