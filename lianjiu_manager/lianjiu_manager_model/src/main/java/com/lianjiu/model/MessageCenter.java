package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MessageCenter {
    private String messageId;

    private String userId;

    private String messageContent;

    private Byte messageStatus;

    private String categoryId;

    private Date messageCretaed;

    private Date messageUpdated;

    private String remarks;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent.trim();
    }

    public Byte getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Byte messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getMessageCretaed() {
        return messageCretaed;
    }

    public void setMessageCretaed(Date messageCretaed) {
        this.messageCretaed = messageCretaed;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getMessageUpdated() {
        return messageUpdated;
    }

    public void setMessageUpdated(Date messageUpdated) {
        this.messageUpdated = messageUpdated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}