package com.lianjiu.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductFurniturePrice {
    private String priceId;

    private String priceSingle;

    private String priceSingleAlliance;

    private String productId;

    private Integer priceUnit;

    private Date priceCreated;

    private Date priceUpdated;

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId == null ? null : priceId.trim();
    }

    public String getPriceSingle() {
        return priceSingle;
    }

    public void setPriceSingle(String priceSingle) {
        this.priceSingle = priceSingle == null ? null : priceSingle.trim();
    }

    public String getPriceSingleAlliance() {
        return priceSingleAlliance;
    }

    public void setPriceSingleAlliance(String priceSingleAlliance) {
        this.priceSingleAlliance = priceSingleAlliance == null ? null : priceSingleAlliance.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public Integer getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Integer priceUnit) {
        this.priceUnit = priceUnit;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getPriceCreated() {
        return priceCreated;
    }

    public void setPriceCreated(Date priceCreated) {
        this.priceCreated = priceCreated;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getPriceUpdated() {
        return priceUpdated;
    }

    public void setPriceUpdated(Date priceUpdated) {
        this.priceUpdated = priceUpdated;
    }
}