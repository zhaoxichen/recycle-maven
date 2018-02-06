package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Power {
    private String powerId;

    private String powerName;

    private String powerUrl;

    private String powerUp;

    private String powerRemarks;

    private Date powerCreated;

    private Date powerUpdated;

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId == null ? null : powerId.trim();
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName == null ? null : powerName.trim();
    }

    public String getPowerUrl() {
        return powerUrl;
    }

    public void setPowerUrl(String powerUrl) {
        this.powerUrl = powerUrl == null ? null : powerUrl.trim();
    }

    public String getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(String powerUp) {
        this.powerUp = powerUp == null ? null : powerUp.trim();
    }

    public String getPowerRemarks() {
        return powerRemarks;
    }

    public void setPowerRemarks(String powerRemarks) {
        this.powerRemarks = powerRemarks == null ? null : powerRemarks.trim();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getPowerCreated() {
        return powerCreated;
    }

    public void setPowerCreated(Date powerCreated) {
        this.powerCreated = powerCreated;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getPowerUpdated() {
        return powerUpdated;
    }

    public void setPowerUpdated(Date powerUpdated) {
        this.powerUpdated = powerUpdated;
    }

	@Override
	public String toString() {
		return "Power [powerId=" + powerId + ", powerName=" + powerName + ", powerUrl=" + powerUrl + ", powerUp="
				+ powerUp + ", powerRemarks=" + powerRemarks + ", powerCreated=" + powerCreated + ", powerUpdated="
				+ powerUpdated + "]";
	}
    
}