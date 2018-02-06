package com.lianjiu.service.product.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.ProductBulk;
import com.lianjiu.rest.mapper.product.ProductBulkMapper;
import com.lianjiu.rest.mapper.product.ProductBulkPriceMapper;
import com.lianjiu.rest.model.BulkVo;
import com.lianjiu.service.product.ProductBulkService;

@Service
public class ProductBulkServiceImpl implements ProductBulkService{
	
	
	@Autowired
	ProductBulkMapper productBulkMapper;
	
	@Autowired
	ProductBulkPriceMapper productBulkPriceMapper;
	
	@Autowired
	private static Logger loggerProductBulk = Logger.getLogger("productBulk");

	/**
	 * 添加家具
	 */
	@Override
	public LianjiuResult addBulk(ProductBulk bulk) {
		loggerProductBulk.info("添加家具");
		bulk.setBulkId(IDUtils.genGUIDs());
		bulk.setBulkVolume((byte) 0);
		bulk.setBulkCreated(new Date());
		bulk.setBulkUpdated(new Date());
		try {
			int rowsAffected = productBulkMapper.insert(bulk);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerProductBulk.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	/**
	 * 更新大宗产品
	 */
	@Override
	public LianjiuResult updateBulk(ProductBulk bulk) {
		loggerProductBulk.info("开始更新大宗产品");
		try {
			bulk.setBulkUpdated(new Date());
			int rowsAffected = productBulkMapper.updateByPrimaryKeySelective(bulk);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerProductBulk.info("捕获异常：" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}
	
	@Override
	public LianjiuResult getBulkByCid(Long cid, int pageNum, int pageSize) {
//		ProductBulk p = new ProductBulk();
//		p.setCategoryId(cid);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<ProductBulk> listBulk = productBulkMapper.getBulkByCid(cid);
		Page<ProductBulk> pageBulk = (Page<ProductBulk>) listBulk;
		
		if (pageNum == 1 && 0 == listBulk.size()) {
			loggerProductBulk.info("大宗产品为空");
			return LianjiuResult.build(501, "尚无大宗产品");
		}
		if (0 == listBulk.size()) {
			loggerProductBulk.info("大宗产品为空");
			return LianjiuResult.build(502, "尚无更多大宗产品");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			//Page<ProductBulk> pageBulk = (Page<ProductBulk>) listBulk;
			List<BulkVo> list = new ArrayList<BulkVo>();
			String address = productBulkMapper.getDetailedAddress(cid);
			for(int i = 0;i<listBulk.size();i++){
				String pid = listBulk.get(i).getBulkId();
				BulkVo bulk = new BulkVo();
				bulk.setBulkId(pid);
				bulk.setBulkName(listBulk.get(i).getBulkName());
				bulk.setBulkVolume(listBulk.get(i).getBulkVolume());
				bulk.setCategoryId(listBulk.get(i).getCategoryId());
				bulk.setBulkHandleWay(listBulk.get(i).getBulkHandleWay());
				String price = productBulkPriceMapper.getCurrentPriceByPid(pid);
				if(null != price && price.length()>0){
					bulk.setPriceSingle(price);
				}
				list.add(bulk);
			}
			loggerProductBulk.info("总页数：" + pageBulk.getTotal());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(list);
			result.setTotal(pageBulk.getTotal());
			result.setMsg(address);
			return result;
		}
	}

	/**
	 * 删除大宗产品
	 */
	@Override
	public LianjiuResult deleteBulk(String bulkId) {
		loggerProductBulk.info("开始删除大宗产品");
		try {
			int rowsAffected = productBulkMapper.deleteByPrimaryKey(bulkId);
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerProductBulk.info("捕获异常！" + e.getMessage());
			return LianjiuResult.build(501, "数据异常！");
		}
	}

	@Override
	public LianjiuResult getAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ProductBulk> listBulk = productBulkMapper.getAll();
		
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductBulk> pageBulk = (Page<ProductBulk>) listBulk;
		loggerProductBulk.info("总页数：" + pageBulk.getTotal());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(listBulk);
		result.setTotal(pageBulk.getTotal());
		return result;
	}

	@Override
	public LianjiuResult modifyBulkAddress(Long cid, String address) {
		int count = productBulkMapper.addressCheck(cid);
		if(count > 0){
			productBulkMapper.modifyAddress(cid,address);
		}else{
			productBulkMapper.insertAddress(IDUtils.genGUIDs(),cid,address);
		}
		return LianjiuResult.ok();
//		bulk.setBulkId(IDUtils.genGUIDs());
//		bulk.setBulkCreated(new Date());
//		bulk.setBulkUpdated(new Date());
	}

	@Override
	public LianjiuResult getBulkAddress(Long cid) {
		String address = productBulkMapper.getDetailedAddress(cid);
		loggerProductBulk.info("address:"+address);
		return LianjiuResult.ok(address);
	}


}
