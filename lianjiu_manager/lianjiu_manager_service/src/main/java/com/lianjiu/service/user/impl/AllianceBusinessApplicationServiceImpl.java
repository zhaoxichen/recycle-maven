package com.lianjiu.service.user.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.Util;
import com.lianjiu.common.utils.sendSMS;
import com.lianjiu.model.AllianceBusiness;
import com.lianjiu.model.AllianceBusinessApplication;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessApplicationMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessMapper;
import com.lianjiu.service.user.AllianceBusinessApplicationService;

@Service
public class AllianceBusinessApplicationServiceImpl implements AllianceBusinessApplicationService {

	@Autowired
	private AllianceBusinessApplicationMapper appMapper;
	@Autowired
	private AllianceBusinessMapper abusMapper;
	@Autowired
	private AllianceBusinessDetailsMapper detailsMapper;
	@Autowired
	private WalletLianjiuMapper walletLianjiuMapper;

	AllianceBusiness allianceBusiness;

	/**
	 * 查所有数据，分页
	 */
	@Override
	public LianjiuResult selectAlliaceApplication(int pageNum, int pageSize) {
		AllianceBusinessApplication allianceBusinessApplication = new AllianceBusinessApplication();
		SearchObjecVo vo = new SearchObjecVo(allianceBusinessApplication);
		PageHelper.startPage(pageNum, pageSize);
		// 根据parent节点查询节点列表
		List<AllianceBusinessApplication> allianceBusinessApplications = appMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBusinessApplication> listAllianceBusinessApplication = (Page<AllianceBusinessApplication>) allianceBusinessApplications;
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(allianceBusinessApplications);
		result.setTotal(listAllianceBusinessApplication.getTotal());
		return result;
	}

	/**
	 * 添加加盟商数据
	 */
	@Override
	public LianjiuResult addAlliaceApplication(AllianceBusinessApplication allianceBusinessApplication) {
		if (null == allianceBusinessApplication) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		// 添加加盟商基本信息
		// 设置id
		allianceBusinessApplication.setAlbnessApplicationId(IDUtils.genABIDs());
		// 设置申请状态默认为0
		allianceBusinessApplication.setAlbnessApplicationStatus("0");
		// 创建时间
		allianceBusinessApplication.setAlbnessApplicationTime(new Date());
		allianceBusinessApplication.setAlbnessApplicationRemarkTime(new Date());
		appMapper.insertSelective(allianceBusinessApplication);

		/*
		 * AllianceBusinessDetails details = new AllianceBusinessDetails();
		 * details.setAbunesId(abunesId); details.setAbunesName(phone);
		 * details.setAbunesPhone(phone); details.setAbunesUpdated(nowTime);
		 * details.setAbunesCreated(nowTime);
		 * details.setAbunesImage(allianceBusinessApplication.
		 * getAlbnessApplicationImage());
		 * details.setAbunesAllianceProvince(allianceBusinessApplication.
		 * getAlbnessApplicationProvince());
		 * details.setAbunesAllianceCity(allianceBusinessApplication.
		 * getAlbnessApplicationCity());
		 * details.setAbunesAllianceDistrict(allianceBusinessApplication.
		 * getAlbnessApplicationDistrict());
		 * details.setAbunesAllianceLocation(allianceBusinessApplication.
		 * getAlbnessApplicationLocation());
		 */

		return LianjiuResult.ok(allianceBusinessApplication);
	}

	/**
	 * 修改、更新数据
	 */
	@Override
	public LianjiuResult updateAlliaceApplication(AllianceBusinessApplication allianceBusinessApplication) {
		if (null == allianceBusinessApplication) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		if (Util.isEmpty(allianceBusinessApplication.getAlbnessApplicationId())) {
			return LianjiuResult.build(501, "请指定要更新的加盟商申请id");
		}
		// 后台审核加盟商通过时，创建加盟商账号密码(账号为登陆表的联系方式，密码为6随机数)
		AllianceBusiness allianceBusiness = new AllianceBusiness();
		String phone = allianceBusinessApplication.getAlbnessApplicationPhone();
		String name = allianceBusinessApplication.getAlbnessApplicationName();
		String passwords = IDUtils.genPWDId();
		String password = MD5Util.md5(passwords);
		String abunesId = IDUtils.genABIDs();
		String photo = GlobalValueUtil.LIANJIU_LOG;
		Date nowTime = new Date();
		// 添加加盟商登陆信息
		allianceBusiness.setAllianceBusinessId(abunesId);
		allianceBusiness.setAllianceBusinessPhone(phone);
		allianceBusiness.setAllianceBusunessPhoto(photo);
		allianceBusiness.setAllianceBusinessName(phone);
		allianceBusiness.setAllianceBusinessPassword(password);
		allianceBusiness.setAllianceBusinessCreated(nowTime);
		allianceBusiness.setAllianceBusinessUpdated(nowTime);
		allianceBusiness.setAllianceBusinessDetailsId(IDUtils.genUserDetIDs());
		// 发送加盟商用户信息密码给用户
		System.out.println("name:" + name);
		System.out.println("电话号码:" + phone);
		System.out.println("密码:" + passwords);
		sendSMS.sendMessage("5", "+86", phone, passwords);
		// 存入数据库
		abusMapper.insertSelective(allianceBusiness);
		AllianceBusinessApplication appAliance = appMapper
				.selectByPrimaryKey(allianceBusinessApplication.getAlbnessApplicationId());
		if (appAliance == null || "".equals(appAliance)) {
			return LianjiuResult.build(400, "对象为空");
		} else {

			// 根据加盟商ID查出用户信息ID
			// String detailsId =
			// detailsMapper.getAppIdById(appAliance.getAlbnessApplicationId());
			AllianceBusinessDetails details = new AllianceBusinessDetails();
			// AllianceBusiness allianceBusiness =
			// abusMapper.selectByPrimaryKey(appAliance.getAlbnessApplicationId());
			String image = "http://image.lianjiuhuishou.com/images/2017/11/01/1509502256848996.png";
			details.setAbunesId(abunesId);
			details.setAbunesAppId(allianceBusinessApplication.getAlbnessApplicationId());
			details.setAbunesName(name);
			details.setAbunesPhone(phone);
			details.setAbunesImage(image);
			details.setAbunesUpdated(nowTime);
			details.setAbunesCreated(nowTime);
			details.setAbunesImage(allianceBusinessApplication.getAlbnessApplicationImage());
			details.setAbunesAllianceProvince(appAliance.getAlbnessApplicationProvince());
			details.setAbunesAllianceCity(appAliance.getAlbnessApplicationCity());
			details.setAbunesAllianceDistrict(appAliance.getAlbnessApplicationDistrict());
			details.setAbunesAllianceLocation(appAliance.getAlbnessApplicationLocation());
			detailsMapper.insertSelective(details);
			// 创建钱包
			String walletId = IDUtils.genWalletLianjiuId();
			WalletLianjiu walletLianjiu = new WalletLianjiu();
			walletLianjiu.setUserId(abunesId);
			walletLianjiu.setWalletId(walletId);
			walletLianjiu.setWalletCreated(new Date());
			walletLianjiu.setWalletUpdated(new Date());
			walletLianjiuMapper.insertWallet(walletLianjiu);
			// 将申请表数据存入数据库
			allianceBusinessApplication.setAlbnessApplicationRemarkTime(new Date());
			allianceBusinessApplication.setAbunesDetailsId(abunesId);
			int rowsAffected = appMapper.updateByPrimaryKeySelective(allianceBusinessApplication);
			return LianjiuResult.ok(rowsAffected);
		}
	}

	/**
	 * 查询所有加盟商申请信息
	 */
	@Override
	public LianjiuResult selectAlliaceApplication() {
		List<AllianceBusinessApplication> listAllianceBusinessApplication = appMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(listAllianceBusinessApplication);
		return result;
	}

	/**
	 * 查询当前加盟上的申请信息
	 */
	@Override
	public LianjiuResult getBusinessDetailsByCid(String albnessApplicationId) {
		AllianceBusinessApplication allianceBusinessApplication = new AllianceBusinessApplication();
		allianceBusinessApplication.setAlbnessApplicationId(albnessApplicationId);
		SearchObjecVo vo = new SearchObjecVo(allianceBusinessApplication);
		List<AllianceBusinessApplication> listAllianceBusinessApplication = appMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listAllianceBusinessApplication);
		return result;
	}

	/**
	 * 根据ID删除加盟商申请表信息
	 */
	@Override
	public LianjiuResult deleteAllianceBusinessApplicationById(String albnessApplicationId) {
		if (Util.isEmpty(albnessApplicationId)) {
			return LianjiuResult.build(501, "请指定要删除的加盟商id");
		}
		// 去数据库删除
		int rowsAffected = appMapper.deleteByPrimaryKey(albnessApplicationId);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 根据CID查询申请表信息
	 */
	@Override
	public LianjiuResult getAllianceAppByCid(long categoryId, int pageNum, int pageSize) {
		AllianceBusinessApplication a = new AllianceBusinessApplication();
		a.setCategoryId(categoryId);
		SearchObjecVo vo = new SearchObjecVo(a);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<AllianceBusinessApplication> listabApp = appMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBusinessApplication> listPageApp = (Page<AllianceBusinessApplication>) listabApp;
		System.out.println("总页数：" + listPageApp.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(listabApp);
		result.setTotal(listPageApp.getTotal());
		return result;
	}

	@Override
	public LianjiuResult vagueQuery(AllianceBusinessApplication allianceBusinessApplication, int pageNum, int pageSize,
			String cratedStart, String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<AllianceBusinessApplication> ordersList = appMapper.vagueQuery(allianceBusinessApplication, cratedStart,
				cratedOver);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBusinessApplication> list = (Page<AllianceBusinessApplication>) ordersList;
		System.out.println("总页数：" + list.getPages());
		for (AllianceBusinessApplication xx : ordersList) {
			System.out.println("开始打印：");
			Util.printModelDetails(xx);
		}
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}
}
