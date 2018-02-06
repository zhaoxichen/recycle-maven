package com.lianjiu.rest.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.service.IndexService;

/**
 * 首页
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/index")
public class IndexController {
	@Autowired
	private IndexService indexService;

	/**
	 * 
	 * zxy 2017年9月22日 descrption:h5首页
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/lianjiu/{type}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexLianjiu(@PathVariable int type) {
		return indexService.getIndexLianjiu(type);
	}

	/**
	 * 
	 * zxy 2017年9月21日 descrption:买精品首页
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productExcellent/{type}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexExcellent(@PathVariable int type) {
		return indexService.getIndexExcellent(type);
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:卖家电首页
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productHousehold", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexHousehold() {
		return indexService.getIndexHousehold();
	}

	/**
	 * 
	 * zhaoxi 2017年9月21日 descrption:电子信息首页
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productElectronic", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexElectronic() {
		return indexService.getIndexElectronic();
	}

	/**
	 * 
	 * zhaoxi 2017年9月22日 descrption:废品信息首页
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productWaste", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexWaste() {
		return indexService.getIndexWaste();
	}

	/**
	 * 
	 * zhaoxi 2017年9月22日 descrption:维修方案信息
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productRepair", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexRepair() {
		return indexService.getIndexRepair();
	}

	/**
	 * 
	 * zhaoxi 2018年1月8日 descrption:家具信息
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productFurniture", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexFurniture() {
		return indexService.getIndexFurniture();
	}

	/**
	 * 
	 * zhaoxi 2018年1月12日 descrption:大宗回收信息，仓库
	 * 
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productBulk", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult indexBulkWarehouse() {
		return indexService.getIndexBulk();
	}

	/**
	 * 
	 * zhaoxi 2018年1月22日 descrption:大宗回收信息，分类
	 * 
	 * @param categoryId
	 * @param TAKEN
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productBulkCategory", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult indexBulkCategory(Long categoryId, String TAKEN) {
		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(401, "请传入TAKEN");
		}
		return indexService.getIndexBulkCategory(TAKEN, categoryId);
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
		return indexService.getBrands();
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
		if (Util.isEmpty(brand)) {
			return LianjiuResult.build(401, "请传入正确的brand");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return indexService.getProductByBrand(brand, orderBy, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年11月30日 descrption:给前端提供加减乘除算法
	 * 
	 * @param numOne
	 * @param numTwo
	 * @param type
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/calculator", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult calculator(String numOne, String numTwo, @RequestParam(defaultValue = "0") Integer type) {
		if (null == type) {
			return LianjiuResult.build(401, "请传入type=0或1或2或3");
		}
		if (Util.isEmpty(numOne)) {
			return LianjiuResult.build(402, "请传入正确的numOne");
		}
		if (Util.isEmpty(numTwo)) {
			return LianjiuResult.build(403, "请传入正确的numTwo");
		}
		if ("0".equals(numOne)) {
			return LianjiuResult.ok("0");
		}
		BigDecimal bigDecimalOne = new BigDecimal(numOne);
		BigDecimal bigDecimalTwo = new BigDecimal(numTwo);
		BigDecimal bigDecimalResult = null;
		switch (type) {
		case 0:
			bigDecimalResult = bigDecimalOne.add(bigDecimalTwo);
			break;
		case 1:
			bigDecimalResult = bigDecimalOne.subtract(bigDecimalTwo);
			break;
		case 2:
			bigDecimalResult = bigDecimalOne.multiply(bigDecimalTwo);
			break;
		case 3:
			bigDecimalResult = bigDecimalOne.divide(bigDecimalTwo, 3, BigDecimal.ROUND_HALF_UP);
			break;
		default:
			bigDecimalResult = bigDecimalOne.add(bigDecimalTwo);
			break;
		}
		String result = bigDecimalResult.toString();
		if (result.contains(".")) {
			result = bigDecimalResult.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}
		return LianjiuResult.ok(result);
	}

}
