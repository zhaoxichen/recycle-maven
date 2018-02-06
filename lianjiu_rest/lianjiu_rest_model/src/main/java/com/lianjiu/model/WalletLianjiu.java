package com.lianjiu.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class WalletLianjiu {
    private String walletId;

    private String userId;

	private String walletMoney;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date walletCreated;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date walletUpdated;

    private String payment;

    private String walletDrawing;

    private String walletDrawed;

    private String walletTotalcount;
    
    public String getWalletMoney() {
  		return walletMoney;
  	}

  	public void setWalletMoney(String walletMoney) {
  		this.walletMoney = walletMoney;
  	}
  	
  	public String getWalletDrawing() {
  		return walletDrawing;
  	}

  	public void setWalletDrawing(String walletDrawing) {
  		this.walletDrawing = walletDrawing;
  	}

  	public String getWalletDrawed() {
  		return walletDrawed;
  	}

  	public void setWalletDrawed(String walletDrawed) {
  		this.walletDrawed = walletDrawed;
  	}

  	public String getWalletTotalcount() {
  		return walletTotalcount;
  	}

  	public void setWalletTotalcount(String walletTotalcount) {
  		this.walletTotalcount = walletTotalcount;
  	}

	public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId == null ? null : walletId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

  
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getWalletCreated() {
        return walletCreated;
    }

    public void setWalletCreated(Date walletCreated) {
        this.walletCreated = walletCreated;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getWalletUpdated() {
        return walletUpdated;
    }

    public void setWalletUpdated(Date walletUpdated) {
        this.walletUpdated = walletUpdated;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment == null ? null : payment.trim();
    }
}