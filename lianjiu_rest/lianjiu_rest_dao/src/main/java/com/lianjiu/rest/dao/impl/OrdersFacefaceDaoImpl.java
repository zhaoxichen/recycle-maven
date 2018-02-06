package com.lianjiu.rest.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.DistanceUtils;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JpushClientUtil;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.SendEmail;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.model.MessageCenter;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.UserAddress;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.dao.OrdersFacefaceDao;
import com.lianjiu.rest.mapper.MessageCenterMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessMapper;
import com.lianjiu.rest.mapper.user.UserAddressMapper;
import com.lianjiu.rest.model.AllianceDistance;
import com.lianjiu.rest.tool.JedisTool;

/**
 * 上门回收订单dao层，使用配置文件方式注入，按需注入
 * 
 * @author zhaoxi
 *
 */
public class OrdersFacefaceDaoImpl implements OrdersFacefaceDao {
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;

	@Autowired
	private UserAddressMapper userAddressMapper;
	@Autowired
	private AllianceBusinessMapper allianceBusinessMapper;

	@Autowired
	private AllianceBusinessDetailsMapper allianceBusinessDetailsMapper;
	@Autowired
	private MessageCenterMapper messageCenterMapper;
	@Autowired
	private JedisClient jedisClient;

	private static Logger loggerFaceface = Logger.getLogger("faceface");

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 订单放出，推送消息给附近的加盟商
	 */
	@Override
	public LianjiuResult pushFaceOrder(String ordersId) {
		/**
		 * 添加推送给附近加盟商 1、查出已开通接单功能的加盟商
		 */
		loggerFaceface.info("推送给附近加盟商");
		// 取出改订单的经纬度
		UserAddress userAddress = userAddressMapper.getLoLaByOrdersId(ordersId);
		Double oLongitude = Double.valueOf(userAddress.getLongitude());
		Double oLatitude = Double.valueOf(userAddress.getLatitude());
		loggerFaceface.info("订单经度：" + oLongitude + "订单纬度：" + oLatitude);
		// 查所有加盟商地址
		List<String> listAllianceId = allianceBusinessDetailsMapper.getAllId();
		if (null == listAllianceId || 0 == listAllianceId.size()) {
			loggerFaceface.info("附近没有加盟商，则进行派单给指定加盟商");
			dispatchFaceOrderFixed(ordersId);
		}
		Util.printModelDetails(listAllianceId);
		// 推送
		// 从缓存中遍历出加盟商的经纬度
		int i = 0;
		int stutas = 99;
		try {
			String allianceSession = null;
			Double aLongitude = 0.0;
			Double aLatitude = 0.0;
			double distance = 0.0;
			for (String allianceId : listAllianceId) {
				allianceSession = jedisClient.get(GlobalValueJedis.REDIS_ALLIANCE_SESSION_KEY + allianceId);
				if (Util.isEmpty(allianceSession)) {
					loggerFaceface.info(allianceId + "用户不在线");
					continue;
				}
				loggerFaceface.info("加盟商编号：" + allianceId);
				String allianceJson = jedisClient.get("allianceId:" + allianceId);
				if (Util.isEmpty(allianceJson)) {
					loggerFaceface.info("获取加盟商定位缓存信息失败");
					continue;
				}
				UserAddress address = JsonUtils.jsonToPojo(allianceJson, UserAddress.class);
				aLongitude = Double.valueOf(address.getLongitude());
				aLatitude = Double.valueOf(address.getLatitude());
				loggerFaceface.info("加盟商经度：" + aLongitude + "加盟商纬度：" + aLatitude);
				/**
				 * 计算距离
				 */
				distance = DistanceUtils.GetDistance(aLongitude, aLatitude, oLongitude, oLatitude);
				loggerFaceface.info("distance:" + i + "=" + distance);
				if (2 > distance) {
					stutas = JpushClientUtil.sendToRegistrationId(allianceId, "新的上门订单来了", "", "", "");
					if (1 == stutas) {
						loggerFaceface.info("推送给设备别名为：" + allianceId + "成功");
					} else {
						loggerFaceface.info("推送给设备别名为：" + allianceId + "失败");
					}
				}
			}
		} catch (NullPointerException e) {
			loggerFaceface.info("数据异常：" + e.getMessage());
		} catch (Exception e) {
			loggerFaceface.info("数据异常：" + e.getMessage());
		} finally {
			loggerFaceface.info("stutas--->" + stutas);
		}
		return LianjiuResult.ok(ordersId);
	}

	/**
	 * 订单放出后，30分钟无人抢单，强制派单给距离最近的加盟商
	 */
	@Override
	public LianjiuResult dispatchFaceOrder(String ordersId) {
		loggerFaceface.info("30分钟无人抢单强制派给距离最近的加盟商");
		/**
		 * 订单详情
		 */
		OrdersFaceface ordersFaceface = ordersFacefaceMapper.getMessage(ordersId);
		if (null == ordersFaceface) {
			loggerFaceface.debug("该订单号有误，订单编号为：" + ordersId);
			// 发个邮件通知工作人员
			SendEmail.sendText(898, "该订单号有误，订单编号为：" + ordersId);
			return LianjiuResult.build(501, "该订单号有误");
		}
		/**
		 * 订单地址的经纬度
		 */
		String addressId = ordersFaceface.getAddressId();
		UserAddress faceAddress = userAddressMapper.getLoLaById(addressId);
		if (null == faceAddress) {
			loggerFaceface.debug("该订单的地址有误,地址编号为：" + addressId);
			// 发个邮件通知工作人员
			SendEmail.sendText(898, "该订单的地址有误,地址编号为：" + addressId);
			return LianjiuResult.build(501, "该订单号有误");
		}
		String faceLong = faceAddress.getLongitude();
		String faceLati = faceAddress.getLatitude();
		if (Util.isEmpty(faceLong) || Util.isEmpty(faceLati)) {
			loggerFaceface.debug("该订单的地址有误,地址编号：" + addressId + "经度:" + faceLong + "纬度:" + faceLati);
			// 发个邮件通知工作人员
			SendEmail.sendText(898, "该订单的地址有误,地址编号：" + addressId + "经度:" + faceLong + "纬度:" + faceLati);
			return LianjiuResult.build(501, "该订单号有误");
		}
		// 转换double
		double faceLongi = Double.parseDouble(faceLong);
		double faceLatit = Double.parseDouble(faceLati);
		// 查出所有加盟商ID用以获取缓存中加盟商的位置信息
		List<String> listAllianceId = allianceBusinessDetailsMapper.getAllOrderId();
		// 加盟商编号跟距离列表
		List<AllianceDistance> list = new ArrayList<AllianceDistance>();
		String latitude = null;
		String longitude = null;
		double latit = 0.0;
		double longi = 0.0;
		double distance = 0.0;
		for (String allianceId : listAllianceId) {
			loggerFaceface.info(allianceId);
			UserAddress address = JedisTool.checkObjectFormRedis(jedisClient, "allianceId:" + allianceId,
					UserAddress.class);
			if (null == address) {
				loggerFaceface.info("空值继续");
				continue;
			}
			latitude = address.getLatitude();
			longitude = address.getLongitude();
			// 转换double
			latit = Double.parseDouble(latitude);
			longi = Double.parseDouble(longitude);
			loggerFaceface.info("订单位置:" + faceLongi + "," + faceLatit + "---加盟商位置：" + longi + "," + latit);
			/**
			 * 计算距离
			 */
			distance = DistanceUtils.GetDistance(faceLongi, faceLatit, longi, latit);
			// 排除掉两公里范围外的加盟商
			if (2 < distance) {
				continue;
			}
			list.add(new AllianceDistance(distance, allianceId));
		}
		/**
		 * 如果附近没有满足条件的加盟商，暂时固定派给指定加盟商
		 */
		if (0 == list.size()) {
			return dispatchFaceOrderFixed(ordersId);
		}
		/**
		 * 根据距离排序获取最近的加盟商信息
		 */
		loggerFaceface.info("排序前：");
		for (AllianceDistance listDist : list) {
			loggerFaceface.info("ID:" + listDist.getAllianceId() + "Distance:" + listDist.getwDistance());
		}
		// 排序
		Collections.sort(list, new Comparator<AllianceDistance>() {
			@Override
			public int compare(AllianceDistance ad1, AllianceDistance ad2) {
				return ad1.getwDistance().compareTo(ad2.getwDistance());
			}
		});
		loggerFaceface.info("排序后");
		AllianceDistance allianceDistance = list.get(0);// 获取最小距离的加盟商
		loggerFaceface.info("ID:" + allianceDistance.getAllianceId() + "Distance:" + allianceDistance.getwDistance());
		/**
		 * 派单给该加盟商，设置该订单关联的加盟商
		 */
		String allianceId = allianceDistance.getAllianceId();
		ordersFaceface.setOrFacefaceAllianceId(allianceId);
		ordersFaceface.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING);// 设置状态为派单上门中
		int count = ordersFacefaceMapper.updateFaceFaceStates(ordersFaceface);
		if (count == 0) {
			loggerFaceface.info("更新出错，派单失败");
			// 发个邮件通知工作人员
			SendEmail.sendText(898, "更新出错，派单失败,订单号为：" + ordersId + "更新加入加盟商编号为:" + allianceId);
			return LianjiuResult.build(502, "派单失败");
		}
		loggerFaceface.info("派单成功");
		// 发送验证码给加盟商
		// 查订单信息
		String location = ordersFaceface.getOrFacefaceLocation();
		Date visitTime = ordersFaceface.getOrFacefaceVisittime();
		return noticeBusubess(location, visitTime, allianceId);
	}

	/**
	 * 
	 * zhaoxi 2018年2月5日 descrption:派单给指定加盟商
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult dispatchFaceOrderFixed(String ordersId) {
		OrdersFaceface ordersFaceface = new OrdersFaceface();
		List<String> allianceIds = allianceBusinessMapper.selectAllianceIdAppoint();
		String allianceId = "";
		if (0 < allianceIds.size()) {
			allianceId = allianceIds.get(0);
		}
		ordersFaceface.setOrFacefaceAllianceId(allianceId);// 派给该加盟商
		ordersFaceface.setOrFacefaceId(ordersId);
		// 设置状态为派单上门中
		ordersFaceface.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING);
		int count = ordersFacefaceMapper.updateFaceFaceStates(ordersFaceface);// 更新订单，标志为派单上门中
		if (count == 0) {
			loggerFaceface.info("更新错误派单失败");
			SendEmail.sendText(897, "更新出错，派单失败,订单号为：" + ordersId + "更新加入加盟商编号为:" + allianceId);
			return LianjiuResult.build(502, "派单失败");
		}
		loggerFaceface.info("派单成功");
		// 查订单信息
		OrdersFaceface orders = ordersFacefaceMapper.getMessage(ordersId);
		String location = orders.getOrFacefaceLocation();
		Date visitTime = orders.getOrFacefaceVisittime();
		/**
		 * 通知加盟商
		 */
		return noticeBusubess(location, visitTime, allianceId);

	}

	/**
	 * 
	 * zhaoxi 2018年2月5日 descrption:通知加盟商，记录推送过的消息
	 * 
	 * @param ordersFaceface
	 * @param allianceId
	 * @return LianjiuResult
	 */
	private LianjiuResult noticeBusubess(String location, Date visitTime, String allianceId) {
		AllianceBusinessDetails businessDetails = allianceBusinessDetailsMapper.selectById(allianceId);
		String str = sdf.format(visitTime);
		String phone = businessDetails.getAbunesPhone();
		loggerFaceface.info("预约上门时间" + str);
		String code = "您好，您分配到了" + location + "的订单," + "上门时间为：" + str + "，请到已抢订单查看详情";
		loggerFaceface.info("加盟手机号码" + phone);
		sendSMS.sendMessage("12", "+86", phone, code);
		loggerFaceface.info("短信发送成功");
		// 将推送消息存到数据库
		MessageCenter messageCenter = new MessageCenter();
		messageCenter.setMessageId(IDUtils.genMessageId());
		messageCenter.setUserId(allianceId);
		messageCenter.setMessageContent(code);
		messageCenter.setMessageStatus((byte) 0);
		// 发送验推送消息证码给加盟商
		messageCenterMapper.insert(messageCenter);
		// 加盟商未完成的订单数+1
		int weekOrder = businessDetails.getAbunesWeekOrder();
		loggerFaceface.info("------7天未完成订单-------" + (weekOrder + 1));
		businessDetails.setAbunesWeekOrder(weekOrder + 1);
		int wCount = allianceBusinessDetailsMapper.updateWeekOrderById(businessDetails);
		if (wCount == 0) {
			loggerFaceface.info("加盟商信息修改失败");
			return LianjiuResult.build(503, "加盟商信息错误");
		} else {
			return LianjiuResult.ok("派单成功");
		}
	}
}
