package com.lianjiu.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Category;
import com.lianjiu.service.CategoryService;

/**
 * 商品分类表现层
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/category")
public class CatagoryController {

	@Autowired
	private CategoryService categoryService;
	
	private static Logger logger1 = Logger.getLogger("visit");
	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption: 添加分类
	 * 
	 * @param parentId
	 * @param name
	 * @param retrieveType
	 * @param templateId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addCategory(Category category) {
		if(null == category){
			return LianjiuResult.build(401, "category绑定错误");
		}
		Util.printModelDetails(category);
		return categoryService.insertCategory(category);
	}

	/**
	 * 
	 * zhaoxi 2017年8月24日 descrption:查询同一级的分类列表
	 * 
	 * @param pid
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCategorys/{pid}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectByParentId(@PathVariable Long pid) {
		if(null == pid){
			return LianjiuResult.build(401, "pid绑定错误");
		}
		System.out.println("查询分类");
		logger1.debug("查询分类");
		LianjiuResult result = categoryService.selectByParentId(pid);
		System.out.println(result);
		return result;
	}

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption:递归查询当前分类的所有子分类
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getCategorys/{id}/childrens", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult selectAllByParentId(@PathVariable Long id) {
		if(null == id){
			return LianjiuResult.build(401, "id绑定错误");
		}
		System.out.println("查询分类");
		logger1.debug("查询分类");
		LianjiuResult result = categoryService.selectDescendantsByParentId(id);
		System.out.println(result);
		return result;
	}

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption:删除一个分类
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeCategory/{categoryId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult removeCategory(@PathVariable Long categoryId) {
		if(null == categoryId){
			return LianjiuResult.build(401, "categoryId绑定错误");
		}
		LianjiuResult result = categoryService.delectCategoryById(categoryId);
		return result;
	}

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption:更新当前分类
	 * 
	 * @param category
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateCategory(Category category) {
		if(null == category){
			return LianjiuResult.build(401, "category绑定错误");
		}
		Util.printModelDetails(category);
		return categoryService.updateCategory(category);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:根据id查询商品分类
	 * 
	 * @param categoryId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectCategory/{categoryId}/id=126", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectCategory(@PathVariable Long categoryId) {
		if(null == categoryId){
			return LianjiuResult.build(401, "categoryId绑定错误");
		}
		LianjiuResult result = categoryService.selectCategoryById(categoryId);
		return result;
	}
}
