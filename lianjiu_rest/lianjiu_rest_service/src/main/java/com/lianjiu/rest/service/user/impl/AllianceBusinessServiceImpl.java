package com.lianjiu.rest.service.user.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.tools.PriceEvaluate;
import com.lianjiu.common.utils.DistanceUtils;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.AllianceBusiness;
import com.lianjiu.model.AllianceBusinessApplication;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.model.Comment;
import com.lianjiu.model.MessageCenter;
import com.lianjiu.model.OrdersFaceface;
import com.lianjiu.model.OrdersFacefaceDetails;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.model.Product;
import com.lianjiu.model.UserAddress;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CommentMapper;
import com.lianjiu.rest.mapper.MessageCenterMapper;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceDetailsMapper;
import com.lianjiu.rest.mapper.orders.OrdersFacefaceMapper;
import com.lianjiu.rest.mapper.orders.OrdersItemMapper;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessApplicationMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessMapper;
import com.lianjiu.rest.mapper.user.UserAddressMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.rest.model.AllianceDistance;
import com.lianjiu.rest.model.Distance;
import com.lianjiu.rest.model.FaceItemVo;
import com.lianjiu.rest.model.OrProductData;
import com.lianjiu.rest.model.OrdersFacefaceFull;
import com.lianjiu.rest.model.OrdersItemInfo;
import com.lianjiu.rest.service.user.AllianceBusinessService;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

@Service
public class AllianceBusinessServiceImpl implements AllianceBusinessService {

	@Autowired
	private AllianceBusinessDetailsMapper allianceBusinessDetailsMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;
	@Autowired
	private AllianceBusinessMapper allianceBusinessMapper;
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private UserWalletRecordMapper walletRecordMapper;
	@Autowired
	private OrdersFacefaceMapper ordersFacefaceMapper;
	@Autowired
	private OrdersItemMapper ordersItemMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private AllianceBusinessDetailsMapper detailsMapper;
	@Autowired
	private UserAddressMapper userAddressMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private OrdersFacefaceDetailsMapper facefaceDetailsMapper;
	@Autowired
	private MessageCenterMapper messageCenterMapper;
	@Autowired
	private AllianceBusinessApplicationMapper appMapper;
	@Autowired
	private UserWalletRecordMapper userWalletRecordMapper;
	@Autowired
	private AllianceBusinessApplicationMapper applicationMapper;
	@Autowired
	private OrdersFacefaceDetailsMapper ordersDetailsMapper;

	private static Logger loggerAlliance = Logger.getLogger("alliance");

	/**
	 * 开通关闭接单功能
	 */
	@Override
	public LianjiuResult updateBusinessById(AllianceBusinessDetails details) {
		loggerAlliance.info("进入开通关闭接单功能");
		if (details.equals(null)) {
			loggerAlliance.info("传入对象为空");
		}
		details.setAbunesUpdated(new Date());
		int count = allianceBusinessDetailsMapper.updateBusinessById(details);
		Integer abunesAcceptOrder = details.getAbunesAcceptOrder();
		String abunesOrder = abunesAcceptOrder.toString();
		if (count > 0) {
			loggerAlliance.info("切换成功");
			return LianjiuResult.build(200, "修改成功", abunesOrder);
		} else {
			{
				loggerAlliance.info("更新数据库失败");
				return LianjiuResult.build(401, "系统繁忙");
			}
		}
	}

	/**
	 * 个人中心信息查询
	 */
	@Override
	public LianjiuResult selectAllianceById(String userId) {
		loggerAlliance.info("开始进入查询个人中心查询");
		Map<String, Object> map = new HashMap<String, Object>();
		// 平均分
		String average = commentMapper.getAverageByUserId(userId);
		loggerAlliance.info("average:" + average);
		if (Util.isEmpty(average)) {
			map.put("average", "0");
		} else {
			map.put("average", average);
		}

		WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(userId);
		int messageNum = messageCenterMapper.getCountByUserId(userId);
		map.put("messageNum", messageNum);
		if (walletLianjiu != null) {
			String balance = walletLianjiu.getWalletMoney();// 余额
			String settlementAmount = userWalletRecordMapper.getAmountByUserId(userId);// 结算金额
			loggerAlliance.info("settlementAmount:" + settlementAmount);
			if (Util.isEmpty(settlementAmount)) {
				map.put("settlementAmount", "0");// 结算金额（目前是回收支付的金额）
			} else {
				map.put("settlementAmount", settlementAmount);// 结算金额（目前是回收支付的金额）
			}
			AllianceBusinessDetails details = allianceBusinessDetailsMapper.selectById(userId);
			if (null != details) {
				Integer abunesAcceptOrder = details.getAbunesAcceptOrder();
				map.put("abunesAcceptOrder", abunesAcceptOrder);
			}
			AllianceBusiness alliance = allianceBusinessMapper.selectAllianceByUserId(userId);
			if (alliance == null) {
				loggerAlliance.info("根据加盟商编号查不到加盟商信息");
				return LianjiuResult.build(501, "用户信息获取失败");
			} else {

				// 链旧钱包余额
				map.put("allianceAsset", balance);
				map.put("alliancename", alliance.getAllianceBusinessName());
				map.put("alliancephoto", alliance.getAllianceBusunessPhoto());
				map.put("alliancephone", alliance.getAllianceBusinessPhone());

				// map.put("grade", 1);
				return LianjiuResult.ok(map);
			}
		} else {
			loggerAlliance.info("异常：没有钱包账户，返回空数据");
			loggerAlliance.info("钱包为空");
			AllianceBusiness alliance = allianceBusinessMapper.selectAllianceByUserId(userId);
			if (alliance == null) {
				return LianjiuResult.build(400, "无记录");
			} else {
				map.put("allianceAsset", 0);
				map.put("settlementAmount", "0");// 结算金额（目前是回收支付的金额）
				map.put("alliancename", alliance.getAllianceBusinessName());
				map.put("alliancephoto", alliance.getAllianceBusunessPhoto());
				map.put("alliancephone", alliance.getAllianceBusinessPhone());
				return LianjiuResult.ok(map);
			}
		}
	}

	/**
	 * 查询用户评论
	 */
	@Override
	public LianjiuResult selectCommentById(String userId, Integer pageNum, Integer pageSize) {
		loggerAlliance.info("进入用户评论查询");
		// 设置分页信息
		// List<Integer> list = new ArrayList<Integer>();
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<Comment> comments = commentMapper.selectByUserId(userId);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Comment> listComment = (Page<Comment>) comments;
		loggerAlliance.info("总页数：" + listComment.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(comments);
		result.setTotal(listComment.getTotal());
		return result;

	}

	/**
	 * 收益明细
	 */
	@Override
	public LianjiuResult selectUserWalletById(String userId, Integer pageNum, Integer pageSize) {
		loggerAlliance.info("进入用户评论查询");
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<UserWalletRecord> walletRecords = walletRecordMapper.selectByUserId(userId);
		if (0 > walletRecords.size()) {
			if (1 < pageNum) {
				return LianjiuResult.build(501, "暂无更多收益信息");
			}
			loggerAlliance.info("还未产生利益信息");
			return LianjiuResult.build(502, "您还未产生收益信息");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<UserWalletRecord> listWalletRecords = (Page<UserWalletRecord>) walletRecords;
			loggerAlliance.info("总页数：" + listWalletRecords.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(walletRecords);
			result.setTotal(listWalletRecords.getTotal());
			return result;
		}
	}

	/**
	 * 上门订单：获取状态属于上门中的订单，再获取订单位置，最后判断2公里内的订单
	 */

	@Override
	public LianjiuResult selectFaceOrderByIds(Integer pageNum, Integer pageSize, String latitude, String longitude,
			String allianceId) {
		loggerAlliance.info("进入查询上门订单");
		// 判断该加盟商是否开通接单功能
		AllianceBusinessDetails details = allianceBusinessDetailsMapper.selectById(allianceId);
		if (null == details) {
			loggerAlliance.info("数据库数据错乱，有可能该加盟商的信息表被误删");
			return LianjiuResult.build(501, "您的账号存在异常，请重新登陆");
		}
		Integer acceptOrder = details.getAbunesAcceptOrder();
		if (acceptOrder == 0) {
			loggerAlliance.info("未开通接单功能");
			return LianjiuResult.build(502, "未开通接单功能");
		}
		// 将加盟商详细位置存入redis
		UserAddress map = new UserAddress();
		map.setUserAddressId(allianceId);
		map.setLatitude(latitude);
		map.setLongitude(longitude);
		jedisClient.set("allianceId:" + allianceId, JsonUtils.objectToJson(map));
		jedisClient.expire("allianceId:" + allianceId, 1800);
		loggerAlliance.info("剩余时间：" + jedisClient.ttl("allianceId:" + allianceId));
		// 查上门订单的经纬度
		PageHelper.startPage(pageNum, pageSize);
		List<FaceItemVo> list = new ArrayList<FaceItemVo>();
		List<OrdersFaceface> listFace = ordersFacefaceMapper.getOrdersFaceLL();
		int i = 0;
		for (OrdersFaceface ordersFaceface : listFace) {
			if (null == ordersFaceface.getLongitude()) {
				loggerAlliance.info("订单获取失败");
				return LianjiuResult.build(503, "订单错误");
			}
			if (null == ordersFaceface.getLatitude()) {
				loggerAlliance.info("订单获取失败");
				return LianjiuResult.build(504, "订单错误");
			}
			FaceItemVo orFaceItemVo = new FaceItemVo();
			i++;
			loggerAlliance.info("加盟商：经度:" + longitude + "纬度：" + latitude);
			loggerAlliance.info("订单" + i);
			double oLongitude = Double.parseDouble(ordersFaceface.getLongitude());
			double oLatitude = Double.parseDouble(ordersFaceface.getLatitude());
			loggerAlliance.info("经度：" + oLongitude);
			loggerAlliance.info("-----------------");
			loggerAlliance.info("纬度：" + oLatitude);
			double aLongitude = Double.parseDouble(longitude);
			double aLatitude = Double.parseDouble(latitude);
			double distance = DistanceUtils.GetDistance(aLongitude, aLatitude, oLongitude, oLatitude);
			loggerAlliance.info("distance:" + i + "=" + distance);
			double straight = Double.parseDouble(details.getStraight());// 加盟接单直线距离
			if (distance <= straight) {
				ordersFaceface.setLatitude(null);
				ordersFaceface.setLongitude(null);
				orFaceItemVo.setOrFacefaceId(ordersFaceface.getOrFacefaceId());
				orFaceItemVo.setOrFacefaceProvince(ordersFaceface.getOrFacefaceProvince());
				orFaceItemVo.setOrFacefaceCity(ordersFaceface.getOrFacefaceCity());
				orFaceItemVo.setOrFacefaceDistrict(ordersFaceface.getOrFacefaceDistrict());
				orFaceItemVo.setOrFacefaceStatus(ordersFaceface.getOrFacefaceStatus());
				orFaceItemVo.setOrFacefaceUpdated(ordersFaceface.getOrFacefaceUpdated());
				orFaceItemVo.setOrFacefaceVisittime(ordersFaceface.getOrFacefaceVisittime());
				BigDecimal bg = new BigDecimal(distance);
				double distancer = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				orFaceItemVo.setDistance(distancer);
				loggerAlliance.info(i + "商品ID----" + ordersFaceface.getOrFacefaceId());
				List<String> item = ordersItemMapper.selectFromByOrderId(ordersFaceface.getOrFacefaceId());
				if (null != item && item.size() > 0) {
					orFaceItemVo.setorItemsStemFrom(item);
				}
				list.add(orFaceItemVo);
				loggerAlliance.info("加VO:" + i + "次");
			}
		}
		for (FaceItemVo ordersFaceface : list) {
			loggerAlliance.info("ordersFaceface:");
			Util.printModelDetails(ordersFaceface);
		}
		// return LianjiuResult.ok(list);
		loggerAlliance.info("总页数：" + list.size());
		LianjiuResult result = new LianjiuResult(list);
		result.setTotal(list.size());
		loggerAlliance.info(pageNum + "--------------------" + pageSize);
		if (pageNum * pageSize > list.size()) {
			if (pageNum * pageSize - pageSize >= list.size()) {
				if (1 < pageNum) {
					return LianjiuResult.build(505, "该范围内尚无更多可抢订单");
				}
				return LianjiuResult.build(506, "该范围内尚无可抢订单");
			}
			return LianjiuResult.ok(list.subList(pageNum * pageSize - pageSize, list.size()));
		}
		return LianjiuResult.ok(list.subList(pageNum * pageSize - pageSize, pageNum * pageSize));

	}

	/**
	 * 上门订单：获取该加盟商附近的订单位置信息，从而获取附近订单(废弃)
	 */
	@Override
	public LianjiuResult selectFaceOrderById(Integer pageNum, Integer pageSize, String latitude, String longitude,
			String allianceId) {
		loggerAlliance.info("进入查询上门订单");
		// 判断该加盟商是否开通接单功能
		AllianceBusinessDetails details = allianceBusinessDetailsMapper.selectById(allianceId);
		if (null == details) {
			loggerAlliance.info("数据库数据错乱，有可能该加盟商的信息表被误删");
			return LianjiuResult.build(502, "数据库数据错乱");
		}
		Integer acceptOrder = details.getAbunesAcceptOrder();
		if (acceptOrder == 0) {
			return LianjiuResult.build(400, "未开通接单功能");
		}
		// 将加盟商详细位置存入redis
		UserAddress map = new UserAddress();
		map.setUserAddressId(allianceId);
		map.setLatitude(latitude);
		map.setLongitude(longitude);
		jedisClient.set("allianceId:" + allianceId, JsonUtils.objectToJson(map));
		jedisClient.expire("allianceId:" + allianceId, 691200);
		loggerAlliance.info("剩余时间：" + jedisClient.ttl("allianceId:" + allianceId));

		// 获取所有用户地址经纬度,地址ID
		List<UserAddress> listUAddress = userAddressMapper.selectLongLati();
		List<Distance> lists = new ArrayList<Distance>();
		// 经纬度转换
		double aLatitude = Double.valueOf(latitude).doubleValue();
		double aLongitude = Double.valueOf(longitude).doubleValue();
		// 地址列表
		for (UserAddress userAddressList : listUAddress) {
			String longit = userAddressList.getLongitude();
			String lati = userAddressList.getLatitude();
			// 经纬度转换
			double uLatitude = Double.parseDouble(longit);
			double uLongitude = Double.parseDouble(lati);
			loggerAlliance.info("-----------------------------------------------");
			loggerAlliance.info("uLatitude:" + uLatitude + "uLongitude:" + uLongitude);
			loggerAlliance.info("alatitude" + latitude + "alongitude" + longitude);
			// 用户(订单)跟加盟商的距离
			double distance = DistanceUtils.GetDistance(uLongitude, uLatitude, aLongitude, aLatitude);
			// String distances = distance + "";
			Distance de = new Distance();
			de.setDostance(distance);
			de.setUserAddressId(userAddressList.getUserAddressId());
			Util.printModelDetails(de);
			lists.add(de);
		}
		List<FaceItemVo> list = new ArrayList<FaceItemVo>();
		List<OrdersFacefaceFull> ordersFaceface = null;
		// 遍历距离
		for (Distance de : lists) {
			double distance = de.getDostance();
			loggerAlliance.info("距离：" + distance);
			// double distance = Double.parseDouble(distances);
			// 两公里范围内的订单详情
			if (distance >= 2) {
				// 根据地址ID查询订单
				ordersFaceface = ordersFacefaceMapper.selectByAddressId(de.getUserAddressId());
				if (ordersFaceface.size() != 0) {
					for (OrdersFacefaceFull ordersFaceface2 : ordersFaceface) {
						// 判断该订单的创建更新时间
						/**
						 * 在21:00-9:00用户下的上门回收订单将于第二天早上9:00分配进入抢单/派单中。9:00-21:00用户下的上门回收订单都是当天给加盟商抢单。
						 */
						Date orFacefaceUpdated = ordersFaceface2.getOrFacefaceUpdated();
						String orFacefaceId = ordersFaceface2.getOrFacefaceId();// 订单编号
						Date orFacefaceVisittime = ordersFaceface2.getOrFacefaceVisittime();// 上门时间
						String orFacefaceProvince = ordersFaceface2.getOrFacefaceProvince();
						String orFacefaceCity = ordersFaceface2.getOrFacefaceCity();
						String orFacefaceDistrict = ordersFaceface2.getOrFacefaceDistrict();
						Byte orFacefaceStatus = ordersFaceface2.getOrFacefaceStatus();// 回收状态
						//
						FaceItemVo orFaceItemVo = new FaceItemVo();
						orFaceItemVo.setOrFacefaceId(orFacefaceId);
						orFaceItemVo.setOrFacefaceVisittime(orFacefaceVisittime);
						orFaceItemVo.setOrFacefaceProvince(orFacefaceProvince);
						orFaceItemVo.setOrFacefaceCity(orFacefaceCity);
						orFaceItemVo.setOrFacefaceDistrict(orFacefaceDistrict);
						orFaceItemVo.setOrFacefaceStatus(orFacefaceStatus);
						orFaceItemVo.setOrFacefaceUpdated(orFacefaceUpdated);
						Util.printModelDetails(orFaceItemVo);

						Date now = new Date();
						loggerAlliance.info(now);
						loggerAlliance.info(orFacefaceVisittime);
						List<String> item = ordersItemMapper.selectFromByOrderId(ordersFaceface2.getOrFacefaceId());
						if (null != item && item.size() > 0) {
							orFaceItemVo.setorItemsStemFrom(item);
						}
						list.add(orFaceItemVo);
					}
				}
			}
		}
		loggerAlliance.info("总页数：" + list.size());
		LianjiuResult result = new LianjiuResult(list);
		result.setTotal(list.size());
		loggerAlliance.info(pageNum + "--------------------" + pageSize);
		if (pageNum * pageSize > list.size()) {
			if (pageNum * pageSize - pageSize >= list.size()) {
				return LianjiuResult.build(503, "该范围内尚无可抢订单");
			}
			return LianjiuResult.ok(list.subList(pageNum * pageSize - pageSize, list.size()));
		}
		return LianjiuResult.ok(list.subList(pageNum * pageSize - pageSize, pageNum * pageSize));
	}

	// 已抢订单(待上门，已上门，取消)
	@Override
	public LianjiuResult alreadyFaceOrder(Integer pageNum, Integer pageSize, String orFacefaceAllianceId,
			Integer orFacefaceStatus) {
		loggerAlliance.info("开始查询已抢订单");
		List<Byte> statusList = new ArrayList<>();
		switch (orFacefaceStatus) {
		case 0:// 取消
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_CANCEL);
			break;
		case 1:// 派单中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_DISPATCH);
			break;
		case 2:// 上门中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING);// 派单上门中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB);// 抢单上门中
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_MODIFY);// 派单上门中修改价格
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_AGREE);// 派单上门中确认价格
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_MODIFY);// 抢单上门中修改价格
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_AGREE);// 抢单上门中确认价格
			break;
		case 3:// 已结算
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_FINISH);
			statusList.add(GlobalValueUtil.ORDER_FACEFACE_EVALUATE_YES);
			break;
		default:
			break;
		}

		// 查订单
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);

		List<OrdersFaceface> listFaceOrder = ordersFacefaceMapper.selectOrdersByallianceId(orFacefaceAllianceId,
				statusList);
		if (pageNum == 1 && listFaceOrder.size() == 0) {
			return LianjiuResult.build(501, "尚无该订单");
		}
		if (listFaceOrder.size() == 0) {
			loggerAlliance.info("还没有订单");
			return LianjiuResult.build(502, "尚无更多订单");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<OrdersFaceface> listOrderFace = (Page<OrdersFaceface>) listFaceOrder;
			loggerAlliance.info("总页数：" + listOrderFace.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(listFaceOrder);
			result.setTotal(listOrderFace.getTotal());
			return LianjiuResult.ok(listFaceOrder);
		}
	}

	/**
	 * 加盟商派单
	 */
	@Override
	public LianjiuResult dispatchFaceOrder(String orderFaceId) {
		loggerAlliance.info("强制派单");
		// 订单经纬度
		UserAddress faceAddress = userAddressMapper.getAddressByFaceId(orderFaceId);
		if (null == faceAddress) {
			loggerAlliance.info("订单查询失败");
			return LianjiuResult.build(501, "该订单号有误");
		}
		String faceLong = faceAddress.getLongitude();
		String faceLati = faceAddress.getLatitude();
		// 转换double
		double faceLongi = Double.parseDouble(faceLong);
		double faceLatit = Double.parseDouble(faceLati);
		// 查出所有开通接单功能的加盟商ID用以获取缓存中加盟商的位置信息
		List<String> listAllianceId = allianceBusinessDetailsMapper.getAllId();
		if (0 > listAllianceId.size()) {
			loggerAlliance.info("附近加盟商定位地址查询失败,则派给回收车");
			// 回收车上门--暂时固定派给指定加盟商
			OrdersFaceface ordersFaceface = new OrdersFaceface();
			List<String> allianceIds = allianceBusinessMapper.selectAllianceIdAppoint();
			String allianceId = "";
			if (0 < allianceIds.size()) {
				allianceId = allianceIds.get(0);
				loggerAlliance.info("指定派给加盟商：" + allianceId);
			}
			ordersFaceface.setOrFacefaceAllianceId(allianceId);
			ordersFaceface.setOrFacefaceId(orderFaceId);
			// 设置状态为派单上门中
			ordersFaceface.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING);
			int count = ordersFacefaceMapper.updateFaceFaceStates(ordersFaceface);
			if (count == 0) {
				loggerAlliance.info("回收车上门失败");
				return LianjiuResult.build(400, "回收车上门失败");
			}
			loggerAlliance.info("回收车上门成功");
			return LianjiuResult.build(200, "回收车上门成功");
		}
		loggerAlliance.info("即将查询");
		int i = 0;
		// 加盟商编号跟距离列表
		List<AllianceDistance> list = new ArrayList<AllianceDistance>();
		for (String allianceId : listAllianceId) {
			loggerAlliance.info(allianceId);
			i++;
			String address = jedisClient.get("allianceId:" + allianceId);
			if (Util.isEmpty(address)) {
				loggerAlliance.info("空值继续");
				continue;
			}
			UserAddress allianceAdd = JsonUtils.jsonToPojo(address, UserAddress.class);
			String latitude = allianceAdd.getLatitude();
			String longitude = allianceAdd.getLongitude();
			// 转换double
			double latit = Double.parseDouble(latitude);
			double longi = Double.parseDouble(longitude);
			loggerAlliance.info("订单位置:" + faceLongi + "--" + faceLatit + "++加盟商位置：" + longi + "--" + latit);
			// 距离跟加盟商编号列表
			double distance = DistanceUtils.GetDistance(faceLongi, faceLatit, longi, latit);
			AllianceDistance allianceDistance = new AllianceDistance();
			allianceDistance.setAllianceId(allianceId);
			allianceDistance.setwDistance(distance);
			list.add(allianceDistance);
			loggerAlliance.info("list" + i + ":" + list);

		}
		for (int j = 0; j < list.size(); j++) {
			double distance = list.get(j).getwDistance();
			loggerAlliance.info("distance:" + distance);
		}
		// 根据距离排序获取最近的加盟商信息
		loggerAlliance.info("排序前：");
		for (AllianceDistance listDist : list) {
			loggerAlliance.info("ID:" + listDist.getAllianceId() + "Distance:" + listDist.getwDistance());
		}
		// 排序
		Collections.sort(list, new Comparator<AllianceDistance>() {
			@Override
			public int compare(AllianceDistance ad1, AllianceDistance ad2) {
				return ad1.getwDistance().compareTo(ad2.getwDistance());
			}
		});
		loggerAlliance.info("排序后");
		// 遍历
		for (AllianceDistance listDistAfter : list) {
			loggerAlliance.info("ID:" + listDistAfter.getAllianceId() + "Distance:" + listDistAfter.getwDistance());
			OrdersFaceface ordersFaceface = new OrdersFaceface();
			if (listDistAfter.getwDistance() <= 2) {
				// 派单给该加盟商
				// 设置该订单关联的加盟商
				String allianceId = listDistAfter.getAllianceId();
				ordersFaceface.setOrFacefaceAllianceId(allianceId);
				ordersFaceface.setOrFacefaceId(orderFaceId);
				// 设置状态为派单上门中
				ordersFaceface.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING);
				int count = ordersFacefaceMapper.updateFaceFaceStates(ordersFaceface);
				if (count == 0) {
					loggerAlliance.info("更新订单出错派单失败");
					return LianjiuResult.build(400, "派单失败");
				}
			}
		}
		loggerAlliance.info("派单成功");
		return LianjiuResult.build(200, "派单成功");

	}

	/**
	 * 抢单
	 */
	@Override
	public LianjiuResult modifyFaceOrder(String orFacefaceAllianceId, String orFacefaceId) {
		loggerAlliance.info("开始抢单");
		// 判断加盟商三个月内是否存在10个用户取消的订单
		int cancelCount = ordersFacefaceMapper.getCancelCountByAllianceId(orFacefaceAllianceId);
		if (100 <= cancelCount) {
			loggerAlliance.info("加盟商三个月内存在10个用户取消的单");
			return LianjiuResult.build(501, "您属于特殊时期，暂时不能抢单，若有疑问请联系链旧客服。");
		}
		// 判断加盟商是否有三个或者三个以上的订单还没有上门，大于等于三个则不允许抢单
		Integer countStatues = ordersFacefaceMapper.getOrdersStatusByAcliacneId(orFacefaceAllianceId);
		loggerAlliance.info("数量" + countStatues);

		if (200 <= countStatues) {
			loggerAlliance.info("加盟商存在三个未完成vhengde单，暂时不能抢单");
			return LianjiuResult.build(502, "您存在3个未完成的订单，暂时不能抢单");
		}

		// 查当前订单状态，如果订单状态不等于1则提示该订单已被抢
		Integer statues = ordersFacefaceMapper.getOrdersStatusByOrdersId(orFacefaceId);
		if (1 != statues) {
			loggerAlliance.info("当前订单状态为：" + statues + ",订单已被抢");
			return LianjiuResult.build(503, "该订单已被抢了！请刷新上门订单");
		}
		// 加盟商信息
		AllianceBusinessDetails details = allianceBusinessDetailsMapper.selectById(orFacefaceAllianceId);
		Util.printModelDetails(details);
		if (null == details) {
			loggerAlliance.info("加盟商账户异常，查不到该加盟商信息");
			return LianjiuResult.build(504, "加盟商账户异常");
		}
		OrdersFaceface faceface = ordersFacefaceMapper.getMessage(orFacefaceId);
		if (null == faceface) {
			loggerAlliance.info("查询订单信息失败");
			return LianjiuResult.build(505, "抢单失败");
		}
		// 接单状态
		Integer accept = details.getAbunesAcceptOrder();
		// 判断是否开启接单功能
		if (1 == accept) {
			String location = faceface.getOrFacefaceLocation();
			Date visitTime = faceface.getOrFacefaceVisittime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// java.util.Date date=new java.util.Date();
			String str = sdf.format(visitTime);
			loggerAlliance.info(str);
			// 设置该订单关联的加盟商
			// OrdersFaceface ordersFaceface = new OrdersFaceface();
			faceface.setOrFacefaceAllianceId(orFacefaceAllianceId);
			faceface.setOrFacefaceId(orFacefaceId);
			// 设置状态为抢单上门中
			faceface.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB);
			int count = ordersFacefaceMapper.updateByPrimaryKeySelective(faceface);
			if (0 != count) {
				// 抢单成功推送消息
				String code = "您好，您分配到了" + location + "的订单," + "上门时间为：" + str + "，请到已抢订单查看详情";
				loggerAlliance.info("加盟商电话号码" + details.getAbunesPhone());
				sendSMS.sendMessage("12", "+86", details.getAbunesPhone(), code);
				// 将推送消息存到数据库
				MessageCenter messageCenter = new MessageCenter();
				messageCenter.setMessageId(IDUtils.genMessageId());
				messageCenter.setUserId(orFacefaceAllianceId);
				messageCenter.setMessageContent(code);
				messageCenter.setMessageCretaed(new Date());
				messageCenter.setMessageUpdated(new Date());
				messageCenter.setMessageStatus((byte) 0);
				Util.printModelDetails("messageCenter" + messageCenter);
				messageCenterMapper.insert(messageCenter);
				// 加盟商未完成的订单数+1
				details.setAbunesId(orFacefaceAllianceId);
				int weekOrder = details.getAbunesWeekOrder();
				loggerAlliance.info("-------------" + (weekOrder + 1));
				details.setAbunesWeekOrder(weekOrder + 1);
				int wCount = allianceBusinessDetailsMapper.updateByPrimaryKeySelective(details);
				if (wCount == 0) {
					loggerAlliance.info("更新加盟商信息出错，抢单失败");
					return LianjiuResult.build(505, "抢单失败");
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Plush", code);
				map.put("status", count);
				loggerAlliance.info(map);
				// 订单已被抢，清除定时器
				HttpClientUtil
						.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderFaceface/orderDispatchDel/" + orFacefaceId);
				return LianjiuResult.ok(map);
			} else {
				loggerAlliance.info("订单号不存在");
				return LianjiuResult.build(506, "订单号不存在");
			}
		} else {
			loggerAlliance.info("尚未开通接单功能，请到个人中心开通接单功能");
			return LianjiuResult.build(507, "尚未开通接单功能，请到个人中心开通接单功能");
		}
	}

	/**
	 * 回收明细单
	 */
	@Override
	public LianjiuResult getOrders(String orOrdersId) {
		if (null == orOrdersId) {
			return LianjiuResult.build(501, "请指定正确的数据参数");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		List<OrdersItemInfo> listOrdersItem = ordersItemMapper.selectByOrderId(orOrdersId);
		List<Double> list = new ArrayList<Double>();
		for (OrdersItemInfo ordersItem : listOrdersItem) {
			String num = ordersItem.getOrItemsNum();
			double nums = Double.parseDouble(num);
			// 商品单价
			String price = ordersItem.getOrItemsPrice();
			double prices = Double.parseDouble(price);

			double sum = nums * prices;
			list.add(sum);
		}
		// 总价
		String totalPrices = ordersDetailsMapper.slectPriceByOrderNo(orOrdersId);
		map.put("totalPrices", totalPrices);
		map.put("listOrdersItem", listOrdersItem);
		return LianjiuResult.ok(map);
	}

	/**
	 * 修改价格
	 */
	@Override
	public LianjiuResult modifyOrders(OrdersItem ordersItem) {
		loggerAlliance.info("开始修改价格");
		if (ordersItem == null) {
			return LianjiuResult.build(400, "传入的对象不能为空");
		}
		List<OrdersItem> orders = ordersItemMapper.updateByOrderId(ordersItem);
		return LianjiuResult.ok(orders);
	}

	/**
	 * 质检明细(查卖家电具体详情)
	 */
	@Override
	public LianjiuResult selectProductByid(String orItemsId) {

		if (null == orItemsId) {
			return LianjiuResult.build(501, "请指定正确的数据参数");
		}
		OrdersItemInfo ordersItem = ordersItemMapper.qualityCheckingFaceface(orItemsId);
		if (null == ordersItem) {
			loggerAlliance.info("查询详情失败");
			return LianjiuResult.build(502, "查询错误");
		}
		return LianjiuResult.ok(ordersItem);
		/*
		 * if (Util.isEmpty(productId)) { return LianjiuResult.build(501,
		 * "请指定要查询的商品id"); } Product product =
		 * productMapper.selectByPrimaryKey(productId); return
		 * LianjiuResult.ok(product);
		 */
	}

	/**
	 * 展示重新估价(查卖家电具体详情)
	 */
	@Override
	public LianjiuResult selectReappraisal(String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的商品id");
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (null == product) {
			loggerAlliance.info("查询商品详情出错");
			return LianjiuResult.build(502, "估价失败");
		}
		return LianjiuResult.ok(product);
	}

	/**
	 * 退出登陆
	 */
	@Override
	public LianjiuResult Logout() {
		loggerAlliance.info("退出登陆");
		// 清除redis中的
		return LianjiuResult.build(200, "退出成功");
	}

	/**
	 * 查询是否开通接单功能
	 */
	@Override
	public LianjiuResult selectById(String allianceId) {
		if (Util.isEmpty(allianceId)) {
			return LianjiuResult.build(400, "请传入加盟商ID");
		}
		AllianceBusinessDetails details = detailsMapper.selectById(allianceId);
		if (null == details) {
			return LianjiuResult.build(501, "请传入正确的加盟商ID");
		}
		Integer acceptOrder = details.getAbunesAcceptOrder();
		if (null == acceptOrder) {
			return LianjiuResult.build(502, "没有开通加单权限");
		}
		String accept = acceptOrder.toString();

		return LianjiuResult.ok(accept);
	}

	/**
	 * 确认修改，更新总价orderfacefaceDetails表
	 */

	@Override
	public LianjiuResult modifyTotalPrice(List<String> idList, List<String> numList, List<String> priceList,
			String orFfDetailsRetrPrice, String ordersId) {
		loggerAlliance.info("开始修改价格");
		if (Util.isEmpty(orFfDetailsRetrPrice)) {
			return LianjiuResult.build(501, "请传入总价");
		}
		if (orFfDetailsRetrPrice.contains("-")) {
			return LianjiuResult.build(502, "价格为负数了" + orFfDetailsRetrPrice);
		}
		if (null == idList) {
			return LianjiuResult.build(503, "请传入ordersItemId集合");
		}
		if (null == priceList) {
			return LianjiuResult.build(504, "请传入单价集合");
		}
		if (null == numList) {
			return LianjiuResult.build(505, "请传入数量集合");
		}
		if (idList.size() != numList.size() || idList.size() != priceList.size()
				|| numList.size() != priceList.size()) {
			return LianjiuResult.build(506, "列表传值长度不一致");
		}
		List<OrProductData> list = new ArrayList<OrProductData>();
		List<OrProductData> updateItemToMysql = new ArrayList<OrProductData>();
		// 实体类
		OrProductData orProductData = new OrProductData();
		// 每个list长度一致
		int count = idList.size();
		BigDecimal orderPrice = new BigDecimal("0");
		BigDecimal itemsPriceBigDecimal = null;
		BigDecimal itemNumBigDecimal = null;
		BigDecimal itemsPriceTotalBigDecimal = null;
		for (int i = 0; i < count; i++) {
			orProductData.setOrItemsId(idList.get(i));
			orProductData.setOrItemsNum(numList.get(i));
			orProductData.setOrItemsPrice(priceList.get(i));
			loggerAlliance.info("开始打印orProductData" + i);
			Util.printModelDetails(orProductData);
			loggerAlliance.info("结束打印orProductData" + i);
			// list添加一组数据
			list.add(orProductData);
			String orItemsId = list.get(i).getOrItemsId();
			loggerAlliance.info("orItemsId" + orItemsId);
			String orItemsNum = list.get(i).getOrItemsNum();
			loggerAlliance.info("orItemsNum" + orItemsNum);
			String orItemsPrice = list.get(i).getOrItemsPrice();
			loggerAlliance.info("orItemsPrice" + orItemsPrice);
			/**
			 * 计算
			 */
			itemsPriceBigDecimal = new BigDecimal(orItemsPrice); // 单价
			itemNumBigDecimal = new BigDecimal(orItemsNum); // 数量
			itemsPriceTotalBigDecimal = itemsPriceBigDecimal.multiply(itemNumBigDecimal);// 相乘
			orProductData.setOrItemsAccountPrice(itemsPriceTotalBigDecimal.toString());// 一条商品记录总价
			orderPrice = orderPrice.add(itemsPriceTotalBigDecimal);// 累加为订单的回收总价
			// 更新一组数据
			updateItemToMysql.add(orProductData);
		}
		loggerAlliance.info("orFfDetailsRetrPrice:" + orFfDetailsRetrPrice);
		BigDecimal bigDecimal = new BigDecimal(orFfDetailsRetrPrice);
		orFfDetailsRetrPrice = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		String totalPrices = orderPrice.toString();
		loggerAlliance.info("totalPrices:" + totalPrices);
		bigDecimal = new BigDecimal(totalPrices);
		totalPrices = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

		if (!totalPrices.equals(orFfDetailsRetrPrice)) {
			LianjiuResult result = new LianjiuResult(totalPrices);
			result.setStatus(507);
			result.setMsg("计算价格出错，与真实价格不匹配");
			return result;
		}
		loggerAlliance.info("-----------------");
		Util.printModelDetails(orProductData);
		// 设置修改后的总价
		OrdersFacefaceDetails details = new OrdersFacefaceDetails();
		details.setOrFacefaceId(ordersId);
		details.setOrFfDetailsUpdated(new Date());

		details.setOrFfDetailsRetrPrice(orFfDetailsRetrPrice);
		// 查修改价格之前的状态
		Integer status = ordersFacefaceMapper.getOrdersStatusByOrdersId(ordersId);
		// 派单中
		loggerAlliance.info("status:" + status);
		if (GlobalValueUtil.ORDER_FACEFACE_VISITING.intValue() == status) {
			details.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING_DISPATCH_MODIFY);
			loggerAlliance.info("派单上门中修改价格");
		}
		// 抢单上门中
		if (GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB.intValue() == status) {
			details.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_MODIFY);
			loggerAlliance.info("抢单上门中修改价格");
		}
		// details.setOrFacefaceStatus(GlobalValueUtil.ORDER_FACEFACE_VISITING_GRAB_MODIFY);
		int counts = facefaceDetailsMapper.modifyVisitPrice(details);
		if (counts == 0) {
			return LianjiuResult.build(400, "修改失败");
		}
		/**
		 * 2017-12-21 暂时这样处理
		 */
		for (OrProductData orProductData2 : updateItemToMysql) {
			counts = ordersItemMapper.modifyNum(orProductData2);
		}
		loggerAlliance.info("更新数据counts:" + counts);

		// 启动超时监控,定时器在定时器系统中开启
		String rep = HttpClientUtil.doPost(GlobalValueUtil.TIMER_BASE_URL + "orderFaceface/orderConfirm/" + ordersId);
		if (Util.notEmpty(rep)) {
			LianjiuResult repObj = JsonUtils.jsonToPojo(rep, LianjiuResult.class);
			if (null != repObj) {
				Util.printModelDetails(repObj);
			}
		}
		return LianjiuResult.build(200, "修改成功");

	}

	/**
	 * 获取消息中心列表
	 */
	@Override
	public LianjiuResult messageCenter(String allianceId, Integer pageSize, Integer pageNum) {
		if (Util.isEmpty(allianceId)) {
			loggerAlliance.info("加盟商ID未传入");
			return LianjiuResult.build(400, "请传入加盟商ID");
		}
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<MessageCenter> list = messageCenterMapper.getContByUserId(allianceId);
		if (pageNum == 1 && list.size() == 0) {
			return LianjiuResult.build(500, "您暂时还没有消息");
		}
		if (list.size() == 0) {
			return LianjiuResult.build(500, "您暂时没有更多的消息！");
		}
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<MessageCenter> listMessage = (Page<MessageCenter>) list;
		loggerAlliance.info("总页数：" + listMessage.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(listMessage);
		result.setTotal(listMessage.getTotal());
		return LianjiuResult.ok(listMessage);
	}

	/**
	 * 更新消息阅读状态
	 */
	@Override
	public LianjiuResult modifyMessageCenterStatus(String messageId) {
		loggerAlliance.info("更新消息阅读状态");
		if (Util.isEmpty(messageId)) {
			loggerAlliance.info("未传入消息编号");
			return LianjiuResult.build(400, "请传入消息编号");
		}
		int count = messageCenterMapper.modifyStatus(messageId);
		if (count == 0) {
			return LianjiuResult.build(400, "系统繁忙");
		}
		return LianjiuResult.build(200, "修改成功");
	}

	/**
	 * 删除单条消息
	 */
	@Override
	public LianjiuResult deleMessageCenter(String messageId) {
		loggerAlliance.info("删除");
		if (Util.isEmpty(messageId)) {
			return LianjiuResult.build(400, "请传入消息编号");
		}
		int count = messageCenterMapper.deleteByPrimaryKey(messageId);
		if (count == 0) {
			return LianjiuResult.build(400, "系统繁忙");
		}
		return LianjiuResult.build(200, "删除成功");
	}

	/**
	 * 重新估价
	 */
	@Override
	public LianjiuResult reappraisal(String orItemsId, String productId, String paramJson, String itemsPrice) {
		loggerAlliance.info("开始重新估价");
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(400, "请传入productId");
		}
		if (Util.isEmpty(paramJson)) {
			return LianjiuResult.build(400, "请传入模板");
		}
		/**
		 * ios未能解决格式化json的办法
		 */
		try {
			paramJson = JSONArray.fromObject(paramJson).toString();
		} catch (JSONException e) {
			loggerAlliance.info("error  productParamData数据异常!");
		}
		// String itemsPrice =
		// orderItemMapper.selectPriceByProductId(productId);
		if (Util.isEmpty(itemsPrice)) {
			return LianjiuResult.build(400, "产品ID传参错误");
		}
		String priceModify = null;
		try {
			priceModify = PriceEvaluate.executeHousehold(paramJson, itemsPrice);
		} catch (Exception e) {
			priceModify = "暂无估价";
		}

		// 修改item的验机价格,和模版
		OrdersItem ordersItem = new OrdersItem();
		ordersItem.setOrItemsId(orItemsId);
		ordersItem.setOrItemsAccountPrice(priceModify);
		ordersItem.setOrItemsParamModify(paramJson);
		ordersItem.setOrItemsUpdated(new Date());
		try {
			ordersItemMapper.updateByPrimaryKeySelective(ordersItem);
		} catch (RuntimeException e) {
			loggerAlliance.info(e.getMessage());
			loggerAlliance.info(e.getCause());
			return LianjiuResult.build(501, "更新失败");
		}
		return LianjiuResult.ok(priceModify);
	}

	/**
	 * 加盟商申请
	 */
	@Override
	public LianjiuResult addAlliaceApplication(AllianceBusinessApplication allianceBusinessApplication) {
		// 添加加盟商基本信息
		String alliancePhone = allianceBusinessApplication.getAlbnessApplicationPhone();
		// 判断加盟商电话号码是否已经申请过
		String appId = applicationMapper.getIdByPhone(alliancePhone);
		if (Util.notEmpty(appId)) {
			return LianjiuResult.build(501, "您该手机号码已经申请过加盟商");
		}
		// 设置id
		String abAppId = IDUtils.genABIDs();
		allianceBusinessApplication.setAlbnessApplicationId(abAppId);
		// 设置申请状态默认为0
		allianceBusinessApplication.setAlbnessApplicationStatus("0");
		allianceBusinessApplication.setAlbnessApplicationTime(new Date());
		allianceBusinessApplication.setAlbnessApplicationRemarkTime(new Date());
		try {
			appMapper.insert(allianceBusinessApplication);
		} catch (RuntimeException e) {
			loggerAlliance.info(e.getMessage());
			loggerAlliance.info(e.getCause());
			return LianjiuResult.build(501, "提交失败，请重新提交");
		}
		return LianjiuResult.ok(abAppId);
	}

	/**
	 * 检查订单是否已经加锁(加锁)
	 */
	@Override
	public LianjiuResult lock(String orderFaceId) {
		/**
		 * 检查订单是否已经加锁
		 */
		String synOrdersId = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_WITH_A_SINGLE + orderFaceId);// 问锁
		if (null != synOrdersId) {
			return LianjiuResult.build(501, "用户正在加单，不可支付");
		} else {
			jedisClient.set(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_PAY_SYN + orderFaceId, orderFaceId);// 加锁
		}
		return LianjiuResult.ok(orderFaceId);
	}

	/**
	 * 解锁订单
	 */
	@Override
	public LianjiuResult unLock(String orderFaceId) {
		/**
		 * 订单存在加单的状况，解锁
		 */
		jedisClient.del(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_PAY_SYN + orderFaceId);// 解锁
		return LianjiuResult.ok(orderFaceId);
	}

	/**
	 * 
	 * zhaoxi 2017年12月26日 descrption:查锁
	 * 
	 * @param orderFaceId
	 * @return LianjiuResult
	 */
	@Override
	public LianjiuResult checkLock(String orderFaceId) {
		String synOrdersId = jedisClient.get(GlobalValueJedis.ORDERS_FACEFACE_COUNT_DOWN_PAY_SYN + orderFaceId);// 查锁
		if (Util.isEmpty(synOrdersId)) {
			return LianjiuResult.ok(0);
		} else {
			return LianjiuResult.ok(1);
		}
	}

	/**
	 * 更新加盟商定位
	 */
	@Override
	public LianjiuResult updateSite(String allianceId, String latitude, String longitude) {
		// 30分钟更新加盟商位置
		// 将加盟商详细位置存入redis
		UserAddress address = new UserAddress();
		address.setUserAddressId(allianceId);
		address.setLatitude(latitude);
		address.setLongitude(longitude);
		jedisClient.set("allianceId:" + allianceId, JsonUtils.objectToJson(address));// 更新加盟商此时的位置
		jedisClient.expire("allianceId:" + allianceId, 18000);// 用户关闭客户端五小时内仍然接受派单
		return LianjiuResult.ok("更新成功");
	}

}
