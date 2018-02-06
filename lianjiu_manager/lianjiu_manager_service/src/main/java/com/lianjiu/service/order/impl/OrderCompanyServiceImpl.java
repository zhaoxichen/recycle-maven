package com.lianjiu.service.order.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersCompany;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.orders.OrdersCompanyMapper;
import com.lianjiu.service.order.OrderCompanyService;

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
	public LianjiuResult getCompanyAll() {
		// 执行查询
		List<OrdersCompany> companys = ordersCompanyMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(companys);
		return result;
	}

	@Override
	public LianjiuResult getCompanyAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersCompany> companys = ordersCompanyMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersCompany> listCompanys = (Page<OrdersCompany>) companys;
		System.out.println("总页数：" + listCompanys.getPages());
		LianjiuResult result = new LianjiuResult(companys);
		result.setTotal(listCompanys.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getCompanyByCid(Long cid) {
		OrdersCompany company = new OrdersCompany();
		company.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(company);
		// 执行查询
		List<OrdersCompany> companys = ordersCompanyMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(companys);
		return result;
	}

	@Override
	public LianjiuResult getCompanyByCid(Long cid, int pageNum, int pageSize) {
		OrdersCompany company = new OrdersCompany();
		company.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(company);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersCompany> companys = ordersCompanyMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersCompany> listCompanys = (Page<OrdersCompany>) companys;
		System.out.println("总页数：" + listCompanys.getPages());
		LianjiuResult result = new LianjiuResult(companys);
		result.setTotal(listCompanys.getTotal());
		return result;
	}

	@Override
	public LianjiuResult addCompany(OrdersCompany company) {

		if (null == company) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		company.setOrCompanyId(IDUtils.genOrdersId());
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

	@Override
	public LianjiuResult vagueQuery(OrdersCompany ordersCompany, int pageNum, int pageSize, String cratedStart,
			String cratedOver) {
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<OrdersCompany> ordersList = ordersCompanyMapper.vagueQuery(ordersCompany,cratedStart,cratedOver);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<OrdersCompany> list = (Page<OrdersCompany>) ordersList;
		System.out.println("总页数：" + list.getPages());
		LianjiuResult result = new LianjiuResult(ordersList);
		result.setTotal(list.getTotal());
		result.setMsg("ok");
		result.setStatus(200);
		return result;
	}

}
