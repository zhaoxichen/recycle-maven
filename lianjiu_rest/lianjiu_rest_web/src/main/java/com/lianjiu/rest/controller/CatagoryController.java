package com.lianjiu.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.rest.service.CategoryService;

/**
 * 分类表 只有查的功能
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/category")
public class CatagoryController {

	@Autowired
	private CategoryService categoryService;

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
			return LianjiuResult.build(401, "pid绑定错误");
		}
		System.out.println("查询分类");
		LianjiuResult result = categoryService.selectDescendantsByParentId(id);
		System.out.println(result);
		return result;
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
			return LianjiuResult.build(401, "pid绑定错误");
		}
		LianjiuResult result = categoryService.selectCategoryById(categoryId);
		return result;
	}

	/**
	 * 
	 * zhaoxi 2017年9月19日 descrption:品牌墙
	 * 
	 * @param parentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBrands/{parentId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getBrands(@PathVariable Long parentId) {
		if(null == parentId){
			return LianjiuResult.build(401, "pid绑定错误");
		}
		System.out.println("查询品牌墙");
		LianjiuResult result = categoryService.selectDescendantsByParentId(parentId);
		return result;
	}
}
