package com.lianjiu.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lianjiu.model.Product;;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ItemInfo extends Product {

	public String[] getImages() {
		String image = this.getProductImage();
		if (image != null) {
			String[] images = image.split(",");
			return images;
		}
		return null;
	}
}
