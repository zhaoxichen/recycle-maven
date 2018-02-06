package com.lianjiu.order.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersCompany;
import com.lianjiu.order.service.OrderCompanyService;
import com.lianjiu.rest.mapper.orders.OrdersCompanyMapper;

/**
 * 加盟商，服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class OrderCompanyServiceImpl implements OrderCompanyService {

	@Autowired
	private OrdersCompanyMapper ordersCompanyMapper;

	@Override
	public LianjiuResult addCompany(OrdersCompany company) {

		if (null == company) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		company.setOrCompanyId(IDUtils.genOrdersId());
		company.setCategoryId(1l);
		company.setOrCompanyCreated(new Date());
		company.setOrCompanyUpdated(new Date());
		// 去数据库添加
		int rowsAffected = ordersCompanyMapper.insertSelective(company);
		return LianjiuResult.ok(rowsAffected);

	}

	@Override
	public LianjiuResult updateCompany(OrdersCompany Company) {
		if (null == Company) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(Company.getOrCompanyId())) {
			return LianjiuResult.build(501, "请指定要更新的企业回收订单id");
		}
		Company.setOrCompanyUpdated(new Date());
		// 去数据库更新
		int rowsAffected = ordersCompanyMapper.updateByPrimaryKeySelective(Company);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteCompany(String CompanyId) {
		if (Util.isEmpty(CompanyId)) {
			return LianjiuResult.build(501, "请指定要删除的企业回收订单id");
		}
		// 去数据库删除
		int rowsAffected = ordersCompanyMapper.deleteByPrimaryKey(CompanyId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getCompanyById(String CompanyId) {
		if (Util.isEmpty(CompanyId)) {
			return LianjiuResult.build(501, "请指定企业回收订单id");
		}
		// 去数据库查询
		OrdersCompany Company = ordersCompanyMapper.selectByPrimaryKey(CompanyId);
		return LianjiuResult.ok(Company);
	}

}
