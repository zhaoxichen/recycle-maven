package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersExcellentDetails {
    private String orExceDetailsId;

    private String orExcellentId;

    private String orExceDetailsPrice;

    private String orExceDetailsLianjiupay;

    private String orExceDetailsOtherspay;

    private String orExceDetailsExpressnum;

    private String orExceProductName;

    private Date orExceDetailsCreated;

    private Date orExceDetailsUpdated;

    private String orExceProductParam;

    public String getOrExceDetailsId() {
        return orExceDetailsId;
    }

    public void setOrExceDetailsId(String orExceDetailsId) {
        this.orExceDetailsId = orExceDetailsId == null ? null : orExceDetailsId.trim();
    }

    public String getOrExcellentId() {
        return orExcellentId;
    }

    public void setOrExcellentId(String orExcellentId) {
        this.orExcellentId = orExcellentId == null ? null : orExcellentId.trim();
    }

    public String getOrExceDetailsPrice() {
        return orExceDetailsPrice;
    }

    public void setOrExceDetailsPrice(String orExceDetailsPrice) {
        this.orExceDetailsPrice = orExceDetailsPrice == null ? null : orExceDetailsPrice.trim();
    }

    public String getOrExceDetailsLianjiupay() {
        return orExceDetailsLianjiupay;
    }

    public void setOrExceDetailsLianjiupay(String orExceDetailsLianjiupay) {
        this.orExceDetailsLianjiupay = orExceDetailsLianjiupay == null ? null : orExceDetailsLianjiupay.trim();
    }

    public String getOrExceDetailsOtherspay() {
        return orExceDetailsOtherspay;
    }

    public void setOrExceDetailsOtherspay(String orExceDetailsOtherspay) {
        this.orExceDetailsOtherspay = orExceDetailsOtherspay == null ? null : orExceDetailsOtherspay.trim();
    }

    public String getOrExceDetailsExpressnum() {
        return orExceDetailsExpressnum;
    }

    public void setOrExceDetailsExpressnum(String orExceDetailsExpressnum) {
        this.orExceDetailsExpressnum = orExceDetailsExpressnum == null ? null : orExceDetailsExpressnum.trim();
    }

    public String getOrExceProductName() {
        return orExceProductName;
    }

    public void setOrExceProductName(String orExceProductName) {
        this.orExceProductName = orExceProductName == null ? null : orExceProductName.trim();
    }

    public Date getOrExceDetailsCreated() {
        return orExceDetailsCreated;
    }

    public void setOrExceDetailsCreated(Date orExceDetailsCreated) {
        this.orExceDetailsCreated = orExceDetailsCreated;
    }

    public Date getOrExceDetailsUpdated() {
        return orExceDetailsUpdated;
    }

    public void setOrExceDetailsUpdated(Date orExceDetailsUpdated) {
        this.orExceDetailsUpdated = orExceDetailsUpdated;
    }

    public String getOrExceProductParam() {
        return orExceProductParam;
    }

    public void setOrExceProductParam(String orExceProductParam) {
        this.orExceProductParam = orExceProductParam == null ? null : orExceProductParam.trim();
    }
}