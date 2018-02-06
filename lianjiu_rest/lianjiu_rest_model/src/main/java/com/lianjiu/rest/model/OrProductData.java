package com.lianjiu.rest.model;
/**
 * 加盟商修改价格传入参数
* <p>Title:OrProductData </p>
* <p>Description: </p>
* <p>Company: </p> 
* @author whd
* @date 2017年11月2日 上午10:42:40
 */
public class OrProductData {

	private String orItemsPrice;

	private String orItemsNum;

	private String orItemsId;
	
	private String orItemsAccountPrice;

	public String getOrItemsPrice() {
		return orItemsPrice;
	}

	public void setOrItemsPrice(String orItemsPrice) {
		this.orItemsPrice = orItemsPrice == null ? null : orItemsPrice.trim();
	}

	public String getOrItemsNum() {
		return orItemsNum;
	}

	public void setOrItemsNum(String orItemsNum) {
		this.orItemsNum = orItemsNum;
	}

	public String getOrItemsId() {
		return orItemsId;
	}

	public void setOrItemsId(String orItemsId) {
		this.orItemsId = orItemsId == null ? null : orItemsId.trim();
	}
	
	public String getOrItemsAccountPrice() {
		return orItemsAccountPrice;
	}

	public void setOrItemsAccountPrice(String orItemsAccountPrice) {
		this.orItemsAccountPrice = orItemsAccountPrice;
	}
}
