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
import com.lianjiu.model.Waste;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.product.WasteMapper;
import com.lianjiu.service.product.ProductWasteService;

/**
 * 废品信息服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductWasteServiceImpl implements ProductWasteService {
	@Autowired
	private WasteMapper wasteMapper;

	@Override
	public LianjiuResult getWasteAll() {
		List<Waste> wastes = wasteMapper.selectBySearchObjecVo(null);
		LianjiuResult result = new LianjiuResult(wastes);
		return result;
	}

	@Override
	public LianjiuResult getWasteAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<Waste> wastes = wasteMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Waste> listWastes = (Page<Waste>) wastes;
		System.out.println("总页数：" + listWastes.getPages());
		LianjiuResult result = new LianjiuResult(wastes);
		result.setTotal(listWastes.getTotal());
		return result;
	}

	@Override
	public LianjiuResult addWaste(Waste waste) {
		if (null == waste) {
			return LianjiuResult.build(500, "传入的对象为空");
		}
		waste.setWasteId(IDUtils.genGUIDs());
		waste.setWasteCreated(new Date());
		waste.setWasteUpdated(new Date());
		// 插入到数据库
		int rowsAffected = wasteMapper.insertSelective(waste);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updateWaste(Waste waste) {
		if (null == waste) {
			return LianjiuResult.build(500, "传入的对象为空");
		}
		if (Util.isEmpty(waste.getWasteId())) {
			return LianjiuResult.build(501, "请指定要更新的废品id");
		}
		waste.setWasteUpdated(new Date());
		// 更新到数据库
		int rowsAffected = wasteMapper.updateByPrimaryKeySelective(waste);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteWaste(String wasteId) {
		if (Util.isEmpty(wasteId)) {
			return LianjiuResult.build(501, "请指定要删除的废品id");
		}
		// 去数据库删除
		int rowsAffected = wasteMapper.deleteByPrimaryKey(wasteId);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getWasteByCid(Long cid) {
		Waste waste = new Waste();
		waste.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(waste);
		List<Waste> wastes = wasteMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(wastes);
		return result;
	}

	@Override
	public LianjiuResult getWasteByCid(Long cid, int pageNum, int pageSize) {
		Waste waste = new Waste();
		waste.setCategoryId(cid);
		SearchObjecVo vo = new SearchObjecVo(waste);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<Waste> wastes = wasteMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Waste> listWastes = (Page<Waste>) wastes;
		System.out.println("总页数：" + listWastes.getPages());
		LianjiuResult result = new LianjiuResult(wastes);
		result.setTotal(listWastes.getTotal());
		return result;
	}

}
