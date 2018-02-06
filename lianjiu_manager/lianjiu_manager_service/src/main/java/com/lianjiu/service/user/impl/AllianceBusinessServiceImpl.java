package com.lianjiu.service.user.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBusiness;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.user.AllianceBusinessMapper;
import com.lianjiu.service.user.AllianceBusinessService;

@Service
public class AllianceBusinessServiceImpl implements AllianceBusinessService {

	@Autowired
	AllianceBusinessMapper allianceBusinessMapper;

	/**
	 * 根据abunesId删除加盟商登陆信息
	 */
	@Override
	public LianjiuResult deleteAllianceBusinessById(String allianceBusinessId) {
		if (null == allianceBusinessId) {
			return LianjiuResult.build(500, "请指定要删除的加盟商id");
		}
		// 去数据库删除
		int rowsAffected = allianceBusinessMapper.deleteByPrimaryKey(allianceBusinessId);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 添加加盟商登陆信息
	 */
	@Override
	public LianjiuResult addAllianceBusiness(AllianceBusiness allianceBusiness) {
		if (null == allianceBusiness) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		// 设置ID
		allianceBusiness.setAllianceBusinessId(IDUtils.genABIDs());
		//随机生成密码
		String pwds = IDUtils.genPWDId();
		System.out.println("随机生成的加盟商密码："+pwds );
		//对密码进行MD5加密
		String pwd = MD5Util.md5(pwds);
		allianceBusiness.setAllianceBusinessPassword(pwd);
		// 创建时间
		allianceBusiness.setAllianceBusinessCreated(new Date());
		// 保存到数据库
		int rowsAffected = allianceBusinessMapper.insertSelective(allianceBusiness);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 查询所有的加盟商登陆信息，分页
	 */
	@Override
	public LianjiuResult selectAllBusiness(int pageNum, int pageSize) {
		SearchObjecVo vo = new SearchObjecVo(null);
		PageHelper.startPage(pageNum, pageSize);
		// 根据parent节点查询节点列表
		List<AllianceBusiness> listAllianceBusinessDetails = allianceBusinessMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBusiness> listPageAllianceBusiness = (Page<AllianceBusiness>) listAllianceBusinessDetails;
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(listAllianceBusinessDetails);
		result.setTotal(listPageAllianceBusiness.getTotal());
		return result;
	}

	/**
	 * 根据abunesId查加盟商登陆信息
	 */
	@Override
	public LianjiuResult selectAllanceBusinessById(String allianceBusinessId) {
		if (Util.isEmpty(allianceBusinessId)) {
			return LianjiuResult.build(501, "请指定要查询的商品id");
		}
		AllianceBusiness allianceBusiness = allianceBusinessMapper.selectByPrimaryKey(allianceBusinessId);
		return LianjiuResult.ok(allianceBusiness);
	}

	/**
	 * 查询所有加盟商登陆
	 */
	@Override
	public LianjiuResult selectAllBusiness() {
		List<AllianceBusiness> listAllianceBusinessApplication = allianceBusinessMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(listAllianceBusinessApplication);
		return result;
	}

	/**
	 * 通过加盟商编号查询加盟商登陆信息
	 */
	@Override
	public LianjiuResult getAllBusinessById(String allianceBusinessId) {
		AllianceBusiness allianceBusiness = new AllianceBusiness();
		allianceBusiness.setAllianceBusinessId(allianceBusinessId);
		SearchObjecVo vo = new SearchObjecVo(allianceBusiness);
		List<AllianceBusiness> listAllianceBusiness = allianceBusinessMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(listAllianceBusiness);
		return result;
	}

	/**
	 * 修改更新登陆信息
	 */
	@Override
	public LianjiuResult updateAllBusiness(AllianceBusiness allianceBusiness) {
		if (null == allianceBusiness) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		if (Util.isEmpty(allianceBusiness.getAllianceBusinessId())) {
			return LianjiuResult.build(501, "请指定要更新的加盟商申请id");
		}
		// 存入数据库
		int rowsAffected = allianceBusinessMapper.updateByPrimaryKeySelective(allianceBusiness);
		allianceBusiness.setAllianceBusinessUpdated(new Date());
		return LianjiuResult.ok(rowsAffected);
	}

}
