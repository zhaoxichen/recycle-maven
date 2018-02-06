package com.lianjiu.model;

import java.util.Date;

public class ProductBulkAddress {
    private String bulkAddressId;

    private Long categoryId;

    private String bulkAddressName;

    private Date bulkAddressCreated;

    private Date bulkAddressUpdated;

    public String getBulkAddressId() {
        return bulkAddressId;
    }

    public void setBulkAddressId(String bulkAddressId) {
        this.bulkAddressId = bulkAddressId == null ? null : bulkAddressId.trim();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getBulkAddressName() {
        return bulkAddressName;
    }

    public void setBulkAddressName(String bulkAddressName) {
        this.bulkAddressName = bulkAddressName == null ? null : bulkAddressName.trim();
    }

    public Date getBulkAddressCreated() {
        return bulkAddressCreated;
    }

    public void setBulkAddressCreated(Date bulkAddressCreated) {
        this.bulkAddressCreated = bulkAddressCreated;
    }

    public Date getBulkAddressUpdated() {
        return bulkAddressUpdated;
    }

    public void setBulkAddressUpdated(Date bulkAddressUpdated) {
        this.bulkAddressUpdated = bulkAddressUpdated;
    }
}