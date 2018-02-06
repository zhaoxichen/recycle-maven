package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductParamTemplate;
import com.lianjiu.service.product.ProductParamService;

@Controller
@RequestMapping("/param")
public class ProductParamController {
	@Autowired
	private ProductParamService productParamService;

	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:添加一个模版
	 * 
	 * @param paramJson
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addTemplate", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addTemplate(ProductParamTemplate template) {
		if(null == template){
			return LianjiuResult.build(401, "template对象绑定错误");
		}
		Util.printModelDetails(template);
		return productParamService.addProductParam(template);
	}

	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:获取一个模版
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getTemplate/{id}/id=126", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getTemplateById(@PathVariable String id) {
		if(Util.isEmpty(id)){
			return LianjiuResult.build(401, "请传入正确的id");
		}
		return productParamService.selectParamById(id);
	}

	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:分页查询模板信息
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getTemplate/{pageNum}/{pageSize}/all", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getTemplateAllByParent(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productParamService.selectByParentAll(pageNum, pageSize);
	}
	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:查询所有模板信息
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getTemplate/all")
	@ResponseBody
	public LianjiuResult getTemplateAll() {
		return productParamService.selectAll();
	}

	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:更新一个模版
	 * 
	 * @param paramJson
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateTemplate", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateTemplate(ProductParamTemplate template) {
		if(null == template){
			return LianjiuResult.build(401, "template对象绑定错误");
		}
		Util.printModelDetails(template);
		return productParamService.updateProductParam(template);
	}

	/**
	 * 
	 * zhaoxi 2017年8月30日 descrption:删除一个模版，根据id
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeTemplateById", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult removeTemplateById(String pptemplId) {
		if(Util.isEmpty(pptemplId)){
			return LianjiuResult.build(401, "请传入正确的pptemplId");
		}
		return productParamService.deleteProductParam(pptemplId);
	}
}
