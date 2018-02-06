package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Power {
    private String powerId;

    private String powerName;

    private String powerUrl;

    private Date powerCreated;

    private Date powerUpdated;

    private String powerRemarks;

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

    public Date getPowerCreated() {
        return powerCreated;
    }

    public void setPowerCreated(Date powerCreated) {
        this.powerCreated = powerCreated;
    }

    public Date getPowerUpdated() {
        return powerUpdated;
    }

    public void setPowerUpdated(Date powerUpdated) {
        this.powerUpdated = powerUpdated;
    }

    public String getPowerRemarks() {
        return powerRemarks;
    }

    public void setPowerRemarks(String powerRemarks) {
        this.powerRemarks = powerRemarks == null ? null : powerRemarks.trim();
    }
}