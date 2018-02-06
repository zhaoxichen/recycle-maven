package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AllianceBuappRemarks {
	/**
	 * 备注ID
	 */
    private String alBuappRemarksId;
    
    /**
     * 加盟商申请表ID
     */
    private String albnessApplicationId;
    
    /**
     * 操作员名称
     */
    private String adminUsername;
    
    /**
     * 备注创建时间
     */
    private Date alBuappRemarksCreated;
    
    /**
     * 备注更新时间
     */
    private Date alBuappRemarksUpdated;
    
    /**
     * 备注内容
     */
    private String alBuappRemarksContent;

    public String getAlBuappRemarksId() {
        return alBuappRemarksId;
    }

    public void setAlBuappRemarksId(String alBuappRemarksId) {
        this.alBuappRemarksId = alBuappRemarksId == null ? null : alBuappRemarksId.trim();
    }

    public String getAlbnessApplicationId() {
        return albnessApplicationId;
    }

    public void setAlbnessApplicationId(String albnessApplicationId) {
        this.albnessApplicationId = albnessApplicationId == null ? null : albnessApplicationId.trim();
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername == null ? null : adminUsername.trim();
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getAlBuappRemarksCreated() {
        return alBuappRemarksCreated;
    }

    public void setAlBuappRemarksCreated(Date alBuappRemarksCreated) {
        this.alBuappRemarksCreated = alBuappRemarksCreated;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getAlBuappRemarksUpdated() {
        return alBuappRemarksUpdated;
    }

    public void setAlBuappRemarksUpdated(Date alBuappRemarksUpdated) {
        this.alBuappRemarksUpdated = alBuappRemarksUpdated;
    }

    public String getAlBuappRemarksContent() {
        return alBuappRemarksContent;
    }

    public void setAlBuappRemarksContent(String alBuappRemarksContent) {
        this.alBuappRemarksContent = alBuappRemarksContent == null ? null : alBuappRemarksContent.trim();
    }
}