package com.lianjiu.rest.controller.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.service.product.ProductExcellentService;

/**
 * 精品信息
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/excellent")
public class ProductExcellentController {

	@Autowired
	ProductExcellentService productExcellentService;

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:分页查询所有精品
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getAll/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentAll(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		
		return productExcellentService.getExcellentAll(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:品牌墙
	 * 
	 * @param parentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBrands", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getBrands() {
		System.out.println("查询品牌墙");
		return productExcellentService.getBrands();
	}

	/**
	 * 
	 * zhaoxi 2017年9月25日 descrption:根据品牌查询商品
	 * 
	 * @param parentId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getProductByBrand", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getProductByBrand(@RequestParam String brand, @RequestParam(defaultValue = "1") int orderBy,
			@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
		if(Util.isEmpty(brand)){
			return LianjiuResult.build(401, "请传入正确的brand");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		
		return productExcellentService.getProductByBrand(brand, orderBy, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年9月19日 descrption:获取详情
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getExcellent/{id}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentByCid(@PathVariable String id) {
		if(Util.isEmpty(id)){
			return LianjiuResult.build(401, "请传入正确的id");
		}
		return productExcellentService.getExcellentById(id);
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:获取精品分类名称
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/categoryFilter ", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getExcellentCategory() {
		return productExcellentService.getCategoryByName("优品信息");
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:筛选品牌
	 * 
	 * @param categoryIds
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/BrandFilter", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult searchBrandsByPid(@RequestParam("categoryIds") List<Long> categoryIds) {
		System.err.println("BrandFilter:" + categoryIds);
		// 模拟传入数组
		if (null == categoryIds) {
			return LianjiuResult.build(401, "请选择一个条件");
		}
		return productExcellentService.getBrandsByPid(categoryIds);
	}

	/**
	 * 
	 * zhaoxi 2017年9月30日 descrption:多条件查询精品
	 * 
	 * @param categoryIds
	 * @param brands
	 * @param orderBy
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getProductByFilters", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult searchProductByBrands(@RequestParam("categoryIds") List<Long> categoryIds,
			@RequestParam("brands") List<String> brands, @RequestParam(defaultValue = "0") int orderBy,
			@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
		System.out.println(categoryIds);
		if (null == categoryIds) {
			return LianjiuResult.build(401, "请选择一个条件");
		}
		if (null == brands) {
			return LianjiuResult.build(402, "请选择一个条件");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(403, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(404, "请传入大于0的pageSize");
		}
		return productExcellentService.getProductByBrands(categoryIds, brands, orderBy, pageNum, pageSize);
	}
}
