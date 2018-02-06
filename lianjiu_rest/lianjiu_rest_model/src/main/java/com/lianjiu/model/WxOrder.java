package com.lianjiu.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WxOrder {
    private String outTradeNo;

    private String totalFee;

    private String userId;

    private String state;

    private String type;

    private String date;

    private String message;

    private String reMessage;

    private String sign;

    private String nonce_str;
    
	private String user_ip;
	
	private String resource;
	 
    public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee == null ? null : totalFee.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getReMessage() {
        return reMessage;
    }

    public void setReMessage(String reMessage) {
        this.reMessage = reMessage == null ? null : reMessage.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

	@Override
	public String toString() {
		return "WxOrder [outTradeNo=" + outTradeNo + ", totalFee=" + totalFee + ", userId=" + userId + ", state="
				+ state + ", type=" + type + ", date=" + date + ", message=" + message + ", reMessage=" + reMessage
				+ ", sign=" + sign + ", nonce_str=" + nonce_str + ", user_ip=" + user_ip + ", resource=" + resource
				+ "]";
	}
    
}