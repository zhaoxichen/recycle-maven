package com.lianjiu.model;

import java.util.Date;

public class Qq {
    private String qqId;

    private String qqNum;

    private String qqNickname;

    private String qqPicture;

    private String userId;

    private Date qqCreated;

    private Date qqUpdated;

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId == null ? null : qqId.trim();
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum == null ? null : qqNum.trim();
    }

    public String getQqNickname() {
        return qqNickname;
    }

    public void setQqNickname(String qqNickname) {
        this.qqNickname = qqNickname == null ? null : qqNickname.trim();
    }

    public String getQqPicture() {
        return qqPicture;
    }

    public void setQqPicture(String qqPicture) {
        this.qqPicture = qqPicture == null ? null : qqPicture.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getQqCreated() {
        return qqCreated;
    }

    public void setQqCreated(Date qqCreated) {
        this.qqCreated = qqCreated;
    }

    public Date getQqUpdated() {
        return qqUpdated;
    }

    public void setQqUpdated(Date qqUpdated) {
        this.qqUpdated = qqUpdated;
    }
}