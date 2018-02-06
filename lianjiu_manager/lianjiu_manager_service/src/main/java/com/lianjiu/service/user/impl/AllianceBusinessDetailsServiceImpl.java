package com.lianjiu.service.user.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBusinessDetails;
import com.lianjiu.model.UserWalletRecord;
import com.lianjiu.model.WalletLianjiu;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.WalletLianjiuMapper;
import com.lianjiu.rest.mapper.user.AllianceBusinessDetailsMapper;
import com.lianjiu.rest.mapper.user.UserWalletRecordMapper;
import com.lianjiu.service.user.AllianceBusinessDetailsService;

@Service
public class AllianceBusinessDetailsServiceImpl implements AllianceBusinessDetailsService {

	@Autowired
	AllianceBusinessDetailsMapper allianceBusinessDetailsMapper;
	
	@Autowired
	WalletLianjiuMapper walletLianjiuMapper;
	
	@Autowired
	UserWalletRecordMapper userWalletRecordMapper;

	/**
	 * 查询所有加盟商信息
	 */
	@Override
	public LianjiuResult getBusinessDetails() {
		List<AllianceBusinessDetails> listAllianceBusinessDetails = allianceBusinessDetailsMapper
				.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(listAllianceBusinessDetails);
		return result;
	}

	/**
	 * 分页查询所有加盟商信息
	 */
	@Override
	public LianjiuResult selectAllBusinessDetails(int pageNum, int pageSize) {
		AllianceBusinessDetails allianceBusinessDetails = new AllianceBusinessDetails();
		SearchObjecVo vo = new SearchObjecVo(allianceBusinessDetails);
		PageHelper.startPage(pageNum, pageSize);
		// 根据parent节点查询节点列表
		List<AllianceBusinessDetails> listAllianceBusinessDetails = allianceBusinessDetailsMapper
				.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBusinessDetails> listPageAllianceBusinessDetails = (Page<AllianceBusinessDetails>) listAllianceBusinessDetails;
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(listAllianceBusinessDetails);
		result.setTotal(listPageAllianceBusinessDetails.getTotal());
		return result;
	}

	/**
	 * 添加加盟商信息
	 */
	@Override
	public LianjiuResult addAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails) {
		if (null == allianceBusinessDetails) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		// 设置ID
		allianceBusinessDetails.setAbunesId(IDUtils.genABIDs());
		allianceBusinessDetails.setAbunesCreated(new Date());
		// 默认不接单
		allianceBusinessDetails.setAbunesAcceptOrder(0);
		// 存入数据库
		int rowsAffected = allianceBusinessDetailsMapper.insertSelective(allianceBusinessDetails);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 根据ID删除加盟商信息
	 */
	@Override
	public LianjiuResult deleteAllianceBusinessDetailsById(String abunesId) {
		if (Util.isEmpty(abunesId)) {
			return LianjiuResult.build(501, "请指定要删除的加盟商id");
		}
		// 去数据库删除
		int rowsAffected = allianceBusinessDetailsMapper.deleteByPrimaryKey(abunesId);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 更新加盟商信息
	 */
	@Override
	public LianjiuResult updateAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails) {
		if (null == allianceBusinessDetails) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		if (Util.isEmpty(allianceBusinessDetails.getAbunesId())) {
			return LianjiuResult.build(501, "请指定要更新的加盟商申请id");
		}
		// 存入数据库
		int rowsAffected = allianceBusinessDetailsMapper.updateByPrimaryKeySelective(allianceBusinessDetails);
		// 记录加盟商更新时间
		allianceBusinessDetails.setAbunesUpdated(new Date());
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 查询当前加盟商信息
	 */
	@Override
	public LianjiuResult getBusinessDetailsById(String abunesId) {
		AllianceBusinessDetails allianceBusinessDetails = new AllianceBusinessDetails();
		allianceBusinessDetails.setAbunesId(abunesId);
		SearchObjecVo vo = new SearchObjecVo(allianceBusinessDetails);
		List<AllianceBusinessDetails> listAllianceBusinessDetails = allianceBusinessDetailsMapper
				.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listAllianceBusinessDetails);
		return result;
	}

	/**
	 * 根据类ID查询所有加盟商信息
	 */

	@Override
	public LianjiuResult getBusinessDetailsByCid(long categoryId, int pageNum, int pageSize) {
		AllianceBusinessDetails a = new AllianceBusinessDetails();
		//a.setCategoryId(categoryId);
		SearchObjecVo vo = new SearchObjecVo(a);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<AllianceBusinessDetails> listabDetails = allianceBusinessDetailsMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBusinessDetails> listPageProduct = (Page<AllianceBusinessDetails>) listabDetails;
		System.out.println("总页数：" + listPageProduct.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(listabDetails);
		result.setTotal(listPageProduct.getTotal());
		return result;
	}

	/**
	 * 操作（开通取消接单资格
	 */
	@Override
	public LianjiuResult modifyAccept(String abunesId, int type) {
		AllianceBusinessDetails details = new AllianceBusinessDetails();
		details.setAbunesId(abunesId);
		// 0为取消资格
		if (type == 0) {
			// 更新加盟商接单状态为2
			details.setAbunesAcceptOrder(GlobalValueUtil.ABUNES_ACCEPT_NOT_ALLOW);
			int rowsAffected = allianceBusinessDetailsMapper.modifyByAbunesId(details);
			if (1 > rowsAffected) {
				return LianjiuResult.build(500, "修改失败");
			}
			return LianjiuResult.ok(rowsAffected);
		}
		// 1为开通资格(不接单)
		if (type == 1) {
			// 更新加盟商接单状态为1
			details.setAbunesAcceptOrder(GlobalValueUtil.ABUNES_ACCEPT_OFF);
			int rowsAffected = allianceBusinessDetailsMapper.modifyByAbunesId(details);
			if (1 > rowsAffected) {
				return LianjiuResult.build(500, "修改失败");
			}
			return LianjiuResult.ok(rowsAffected);
		}
		return null;
	}

	// 查钱包明细
	@Override
	public LianjiuResult selectAssetDetails(String abunesId) {
		// 根据用户id获取其钱包 
		WalletLianjiu walletLianjiu = walletLianjiuMapper.selectWalletByUserId(abunesId);
		// 根据用户ID查账单
		List<UserWalletRecord> list = userWalletRecordMapper.selectRecordByUserId(abunesId);
		if (walletLianjiu != null) {
			String userAsset = walletLianjiu.getWalletMoney();
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("userAsset", userAsset);
			map.put("list", list);
			return LianjiuResult.ok(map);
		} else {
			// 没有账单
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userAsset", 0);
			map.put("recordName", list);
			return LianjiuResult.ok(map);
		}
	}

	@Override
	public LianjiuResult vagueQuery(AllianceBusinessDetails allianceBusinessDetails, int pageNum, int pageSize,
			String cratedStart, String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<AllianceBusinessDetails> abList = allianceBusinessDetailsMapper.vagueQuery(allianceBusinessDetails,cratedStart,cratedOver);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBusinessDetails> list = (Page<AllianceBusinessDetails>) abList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(abList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

}
