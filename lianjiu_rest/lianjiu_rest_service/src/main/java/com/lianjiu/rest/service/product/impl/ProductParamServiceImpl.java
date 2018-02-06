package com.lianjiu.rest.service.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductParamTemplate;
import com.lianjiu.rest.mapper.product.ProductParamTemplateMapper;
import com.lianjiu.rest.service.product.ProductParamService;

@Service
public class ProductParamServiceImpl implements ProductParamService {
	@Autowired
	private ProductParamTemplateMapper productParamTemplateMapper;

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
	

}
