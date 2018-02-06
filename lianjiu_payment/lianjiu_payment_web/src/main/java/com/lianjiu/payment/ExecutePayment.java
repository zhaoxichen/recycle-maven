package com.lianjiu.payment;

/**
 * 支付接口，提供方法：付款，收款，退款
 * 
 * @author zhaoxi
 *
 */
public interface ExecutePayment {

	Integer pay(String money);// 支持传入字符型的数字，返回状态，0不成功，1成功，其他状态码需指明意义

	Integer pay(Integer money);// 支持传入整型的数字

	Integer getPay(String money);// 支持传入字符型的数字，返回状态，0不成功，1成功，其他状态码需指明意义

	Integer getPay(Integer money);// 支持传入整型的数字

	Integer refund(String money);

	Integer refund(Integer money);
}
