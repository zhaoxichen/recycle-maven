package com.lianjiu.rest.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 单个订单只显示一个item的order VO类
 * 
 * @author zhaoxi
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrdersView {
	private String ordersId;// 订单编号

	private String ordersPrice;// 总价格

	private Byte ordersStatus;// 订单状态

	private Date ordersCreated;// 订单生成时间

	private String orItemsProductId;

	private String orItemsNamePreview;// 产品名称

	private String orItemsPictruePreview;// 产品图片

	private String orItemsParam;// 产品参数

	private String orExceDetailsExpressName;// 快递名称

	private String orExceDetailsExpressNum;// 快递单号

	private String orExceRefundExpress;// 退货快递名称

	private String orExceRefundExpressnum;// 退货快递号

	public String getOrdersId() {
		return ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}

	public String getOrdersPrice() {
		return ordersPrice;
	}

	public void setOrdersPrice(String ordersPrice) {
		this.ordersPrice = ordersPrice;
	}

	public Byte getOrdersStatus() {
		return ordersStatus;
	}

	public void setOrdersStatus(Byte ordersStatus) {
		this.ordersStatus = ordersStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOrdersCreated() {
		return ordersCreated;
	}

	public void setOrdersCreated(Date ordersCreated) {
		this.ordersCreated = ordersCreated;
	}

	public String getOrItemsNamePreview() {
		return orItemsNamePreview;
	}

	public void setOrItemsNamePreview(String orItemsNamePreview) {
		this.orItemsNamePreview = orItemsNamePreview;
	}

	public String getOrItemsPictruePreview() {
		return orItemsPictruePreview;
	}

	public void setOrItemsPictruePreview(String orItemsPictruePreview) {
		this.orItemsPictruePreview = orItemsPictruePreview;
	}

	public String getOrItemsParam() {
		return orItemsParam;
	}

	public void setOrItemsParam(String orItemsParam) {
		this.orItemsParam = orItemsParam;
	}

	public String getOrItemsProductId() {
		return orItemsProductId;
	}

	public void setOrItemsProductId(String orItemsProductId) {
		this.orItemsProductId = orItemsProductId;
	}

	public String getOrExceDetailsExpressName() {
		return orExceDetailsExpressName;
	}

	public void setOrExceDetailsExpressName(String orExceDetailsExpressName) {
		this.orExceDetailsExpressName = orExceDetailsExpressName;
	}

	public String getOrExceDetailsExpressNum() {
		return orExceDetailsExpressNum;
	}

	public void setOrExceDetailsExpressNum(String orExceDetailsExpressNum) {
		this.orExceDetailsExpressNum = orExceDetailsExpressNum;
	}

	public String getOrExceRefundExpress() {
		return orExceRefundExpress;
	}

	public void setOrExceRefundExpress(String orExceRefundExpress) {
		this.orExceRefundExpress = orExceRefundExpress;
	}

	public String getOrExceRefundExpressnum() {
		return orExceRefundExpressnum;
	}

	public void setOrExceRefundExpressnum(String orExceRefundExpressnum) {
		this.orExceRefundExpressnum = orExceRefundExpressnum;
	}
}
