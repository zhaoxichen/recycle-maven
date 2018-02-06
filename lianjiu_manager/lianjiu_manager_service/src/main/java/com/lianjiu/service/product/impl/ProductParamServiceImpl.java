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
import com.lianjiu.model.ProductParamTemplate;
import com.lianjiu.rest.mapper.product.ProductParamTemplateMapper;
import com.lianjiu.service.product.ProductParamService;

@Service
public class ProductParamServiceImpl implements ProductParamService {
	@Autowired
	private ProductParamTemplateMapper productParamTemplateMapper;

	/**
	 * 封装，插入一个参数模版
	 */
	@Override
	public LianjiuResult addProductParam(ProductParamTemplate template) {
		if (null == template) {
			return LianjiuResult.build(500, "传入的对象为空");
		}
		if (Util.isEmpty(template.getPptemplData())) {
			return LianjiuResult.build(501, "接收到的json数据为空");
		}
		template.setPptemplId(IDUtils.genGUIDs());
		template.setPptemplUpdated(new Date());
		template.setPptemplCreated(new Date());
		int rowsAffected = productParamTemplateMapper.insert(template);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult selectParamById(String id) {
		if (Util.isEmpty(id)) {
			return LianjiuResult.build(501, "请指定要查询的模板id");
		}
		ProductParamTemplate template = productParamTemplateMapper.selectById(id);
		return LianjiuResult.ok(template);
	}

	public LianjiuResult selectAll() {
		List<ProductParamTemplate> templates = productParamTemplateMapper.selectAll();
		LianjiuResult result = new LianjiuResult(templates);
		return result;
	}

	@Override
	public LianjiuResult selectByParentAll(int pageNum, int pageSize) {
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<ProductParamTemplate> templates = productParamTemplateMapper.selectAll();
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<ProductParamTemplate> listTemplate = (Page<ProductParamTemplate>) templates;
		System.out.println("总页数：" + listTemplate.getPages());
		LianjiuResult result = new LianjiuResult(templates);
		return result;
	}

	/**
	 * 更新
	 */
	@Override
	public LianjiuResult updateProductParam(ProductParamTemplate template) {
		if (null == template) {
			return LianjiuResult.build(500, "传入的对象为空");
		}
		if (Util.isEmpty(template.getPptemplId())) {
			return LianjiuResult.build(501, "请指定要更新的模板id");
		}
		template.setPptemplUpdated(new Date());
		// 更新到数据库
		int rowsAffected = productParamTemplateMapper.updateByPrimaryKeySelective(template);
		return LianjiuResult.ok(rowsAffected);
	}

	/**
	 * 删除
	 */
	@Override
	public LianjiuResult deleteProductParam(String id) {
		if (Util.isEmpty(id)) {
			return LianjiuResult.build(501, "请指定要删除的模板id");
		}
		int rowsAffected = productParamTemplateMapper.deleteByPrimaryKey(id);
		return LianjiuResult.ok(rowsAffected);
	}

}
