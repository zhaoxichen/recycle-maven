package com.lianjiu.service.product.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductExcellent;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.product.ProductExcellentMapper;
import com.lianjiu.service.product.ProductExcellentService;

/**
 * 精品信息
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductExcellentServiceImpl implements ProductExcellentService {
	@Autowired
	private ProductExcellentMapper productExcellentMapper;

	@Override
	public LianjiuResult getExcellentAll() {
		// 执行查询
		List<ProductExcellent> excellents = productExcellentMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(excellents);
		return result;
	}

	@Override
	public LianjiuResult getExcellentAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<ProductExcellent> excellents = productExcellentMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductExcellent> listExcellents = (Page<ProductExcellent>) excellents;
		System.out.println("总页数：" + listExcellents.getPages());
		LianjiuResult result = new LianjiuResult(excellents);
		result.setTotal(listExcellents.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getExcellentByCid(Long cid) {
		ProductExcellent excellent = new ProductExcellent();
		excellent.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(excellent);
		// 执行查询
		List<ProductExcellent> excellents = productExcellentMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(excellents);
		return result;
	}

	@Override
	public LianjiuResult getExcellentByCid(Long cid, int pageNum, int pageSize) {
		ProductExcellent excellent = new ProductExcellent();
		excellent.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(excellent);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<ProductExcellent> excellents = productExcellentMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductExcellent> listExcellents = (Page<ProductExcellent>) excellents;
		System.out.println("总页数：" + listExcellents.getPages());
		LianjiuResult result = new LianjiuResult(excellents);
		result.setTotal(listExcellents.getTotal());
		return result;
	}

	@Override
	public LianjiuResult addExcellent(ProductExcellent excellent) {

		if (null == excellent) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		excellent.setSoldCount((long) 0);
		excellent.setExcellentId(IDUtils.genGUIDs());
		excellent.setSoldCount((long) 0);
		excellent.setExcellentCreated(new Date());
		excellent.setExcellentUpdated(new Date());
		// 去数据库添加
		int rowsAffected = productExcellentMapper.insert(excellent);
		return LianjiuResult.ok(rowsAffected);

	}

	@Override
	public LianjiuResult updateExcellent(ProductExcellent excellent) {
		if (null == excellent) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(excellent.getExcellentId())) {
			return LianjiuResult.build(501, "请指定要更新的精品id");
		}
		excellent.setExcellentUpdated(new Date());
		// 去数据库更新
		int rowsAffected = productExcellentMapper.updateByPrimaryKeySelective(excellent);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteExcellent(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(501, "请指定要删除的精品id");
		}
		// 去数据库删除
		int rowsAffected = productExcellentMapper.deleteByPrimaryKey(excellentId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getExcellentById(String excellentId) {
		if (Util.isEmpty(excellentId)) {
			return LianjiuResult.build(501, "请指定精品的id");
		}
		// 去数据库查询
		ProductExcellent excellent = productExcellentMapper.selectByPrimaryKey(excellentId);
		return LianjiuResult.ok(excellent);

	}
}
