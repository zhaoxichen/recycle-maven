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
import com.lianjiu.model.ProductRepair;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.product.ProductRepairMapper;
import com.lianjiu.service.product.ProductRepairService;

/**
 * 维修方案服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductRepairServiceImpl implements ProductRepairService {
	@Autowired
	private ProductRepairMapper productRepairMapper;

	@Override
	public LianjiuResult getRepairAll() {
		// 执行查询
		List<ProductRepair> repairs = productRepairMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(repairs);
		return result;
	}

	@Override
	public LianjiuResult getRepairAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<ProductRepair> repairs = productRepairMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductRepair> listRepairs = (Page<ProductRepair>) repairs;
		System.out.println("总页数：" + listRepairs.getPages());
		LianjiuResult result = new LianjiuResult(repairs);
		result.setTotal(listRepairs.getTotal());
		return result;
	}

	@Override
	public LianjiuResult getRepairByCid(Long cid) {
		ProductRepair repair = new ProductRepair();
		repair.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(repair);
		// 执行查询
		List<ProductRepair> repairs = productRepairMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(repairs);
		return result;
	}

	@Override
	public LianjiuResult getRepairByCid(Long cid, int pageNum, int pageSize) {
		ProductRepair repair = new ProductRepair();
		repair.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(repair);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 执行查询
		List<ProductRepair> repairs = productRepairMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductRepair> listRepairs = (Page<ProductRepair>) repairs;
		System.out.println("总页数：" + listRepairs.getPages());
		LianjiuResult result = new LianjiuResult(repairs);
		result.setTotal(listRepairs.getTotal());
		return result;
	}

	@Override
	public LianjiuResult addRepair(ProductRepair repair) {

		if (null == repair) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		repair.setRepairId(IDUtils.genGUIDs());
		repair.setRepairCreated(new Date());
		repair.setRepairUpdated(new Date());
		// 去数据库添加
		int rowsAffected = productRepairMapper.insert(repair);
		return LianjiuResult.ok(rowsAffected);

	}

	@Override
	public LianjiuResult updateRepair(ProductRepair repair) {
		if (null == repair) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(repair.getRepairId())) {
			return LianjiuResult.build(501, "请指定要更新的维修品id");
		}
		repair.setRepairUpdated(new Date());
		// 去数据库更新
		int rowsAffected = productRepairMapper.updateByPrimaryKeySelective(repair);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteRepair(String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(501, "请指定要删除的维修品id");
		}
		// 去数据库删除
		int rowsAffected = productRepairMapper.deleteByPrimaryKey(repairId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getRepairById(String repairId) {
		if (Util.isEmpty(repairId)) {
			return LianjiuResult.build(501, "请指定维修品id");
		}
		// 去数据库查询
		ProductRepair repair = productRepairMapper.selectByPrimaryKey(repairId);
		return LianjiuResult.ok(repair);
	}

}
