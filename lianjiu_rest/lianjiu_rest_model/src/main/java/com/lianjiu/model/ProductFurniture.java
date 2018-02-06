package com.lianjiu.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductFurniture {
    private String furnitureId;

    private Long categoryId;

    private String furnitureName;

    private String furnitureHandleWay;

    private String furnitureVolume;

    private String furnitureImage;

    private Date furnitureCreated;

    private Date furnitureUpdated;

    public String getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(String furnitureId) {
        this.furnitureId = furnitureId == null ? null : furnitureId.trim();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getFurnitureName() {
        return furnitureName;
    }

    public void setFurnitureName(String furnitureName) {
        this.furnitureName = furnitureName == null ? null : furnitureName.trim();
    }

    public String getFurnitureHandleWay() {
        return furnitureHandleWay;
    }

    public void setFurnitureHandleWay(String furnitureHandleWay) {
        this.furnitureHandleWay = furnitureHandleWay == null ? null : furnitureHandleWay.trim();
    }

    public String getFurnitureVolume() {
        return furnitureVolume;
    }

    public void setFurnitureVolume(String furnitureVolume) {
        this.furnitureVolume = furnitureVolume == null ? null : furnitureVolume.trim();
    }

    public String getFurnitureImage() {
        return furnitureImage;
    }

    public void setFurnitureImage(String furnitureImage) {
        this.furnitureImage = furnitureImage == null ? null : furnitureImage.trim();
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getFurnitureCreated() {
        return furnitureCreated;
    }

    public void setFurnitureCreated(Date furnitureCreated) {
        this.furnitureCreated = furnitureCreated;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getFurnitureUpdated() {
        return furnitureUpdated;
    }

    public void setFurnitureUpdated(Date furnitureUpdated) {
        this.furnitureUpdated = furnitureUpdated;
    }
}