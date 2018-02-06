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
import com.lianjiu.model.WastePrice;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.product.WastePriceMapper;
import com.lianjiu.service.product.ProductWastePriceService;

/**
 * 废品价格
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductWastePriceServiceImpl implements ProductWastePriceService {
	@Autowired
	private WastePriceMapper wastePriceMapper;
	@Autowired
	JedisClient jedisClient;

	@Override
	public LianjiuResult getPriceAll() {
		List<WastePrice> wastePrices = wastePriceMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		LianjiuResult result = new LianjiuResult(wastePrices);
		return result;
	}

	@Override
	public LianjiuResult getPriceAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<WastePrice> wastePrices = wastePriceMapper.selectBySearchObjecVo(null);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<WastePrice> listWastePrices = (Page<WastePrice>) wastePrices;
		System.out.println("总页数：" + listWastePrices.getPages());
		LianjiuResult result = new LianjiuResult(wastePrices);
		result.setTotal(listWastePrices.getTotal());
		return result;

	}

	@Override
	public LianjiuResult getPriceByWasteId(String wasteId) {
		WastePrice wastePrice = new WastePrice();
		wastePrice.setWasteId(wasteId);
		SearchObjecVo vo = new SearchObjecVo(wastePrice);
		List<WastePrice> wastePrices = wastePriceMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(wastePrices);
		return result;
	}

	@Override
	public LianjiuResult getPriceByWasteId(String wasteId, int pageNum, int pageSize) {
		WastePrice wastePrice = new WastePrice();
		wastePrice.setWasteId(wasteId);
		SearchObjecVo vo = new SearchObjecVo(wastePrice);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<WastePrice> wastePrices = wastePriceMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<WastePrice> listWastePrices = (Page<WastePrice>) wastePrices;
		System.out.println("总页数：" + listWastePrices.getPages());
		LianjiuResult result = new LianjiuResult(wastePrices);
		result.setTotal(listWastePrices.getTotal());
		return result;
	}

	@Override
	public LianjiuResult addPrice(WastePrice wastePrice) {
		if (null == wastePrice) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		wastePrice.setwPriceId(IDUtils.genGUIDs());
		wastePrice.setwPriceCreated(new Date());
		wastePrice.setwPriceUpdated(new Date());
		// 去数据库更新
		int rowsAffected = wastePriceMapper.insert(wastePrice);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult updatePrice(WastePrice wastePrice) {
		if (null == wastePrice) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		if (Util.isEmpty(wastePrice.getWasteId())) {
			return LianjiuResult.build(501, "请指定要更新的废品价格id");
		}
		wastePrice.setwPriceUpdated(new Date());
		// 去数据库更新
		int rowsAffected = wastePriceMapper.updateByPrimaryKeySelective(wastePrice);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deletePrice(String wPriceId) {
		if (Util.isEmpty(wPriceId)) {
			return LianjiuResult.build(501, "请指定要删除的废品价格id");
		}
		// 去数据库删除
		int rowsAffected = wastePriceMapper.deleteByPrimaryKey(wPriceId);
		return LianjiuResult.ok(rowsAffected);
	}

}
