package com.lianjiu.rest.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.OrdersItem;
import com.lianjiu.rest.service.product.ProductService;

/**
 * 产品回收，用户只用查看产品，选择产品功能
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 
	 * xyz 2017年8月31日 descrption: 通过分类id获取商品
	 * 
	 * @param cId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBrandBycId/{cId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getBrandBycId(@PathVariable Long cId) {
		if (null == cId) {
			return LianjiuResult.build(401, "cId对象绑定错误");
		}
		return productService.getBrandBycId(cId);
	}

	/**
	 * 
	 * xyz 2017年9月6日 descrption:根据id查询商品
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectProduct/{productId}/id=126", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectProductByid(@PathVariable String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return productService.selectProductByid(productId);
	}

	/**
	 * 
	 * huangfu 2017年9月22日 descrption:通过家电分类的品牌获取具体产品
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getByProductCategoryId/{CategoryId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getByProductCategoryId(@PathVariable long CategoryId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (0 == CategoryId) {
			return LianjiuResult.build(401, "请此传入正确的分类id");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return productService.selectByProductCategoryId(CategoryId, pageNum, pageSize);
	}

	/**
	 * 
	 * xyz 2017年9月22日 descrption:通过产品的参数估算价格 与查询出所有已卖出的记录
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/calculationPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult calculationPrice(@RequestParam(defaultValue = "11111-11111-00000-11111") String TAKEN) {
		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(401, "请传入正确的TAKEN");
		}
		return productService.calculationPrice(TAKEN);
	}

	/**
	 * 
	 * xyz 2017年9月22日 descrption:通过产品的参数估算价格 与查询出所有已卖出的记录
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/calculationJDPrice", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult calculationJDPrice(@RequestParam(defaultValue = "11111-11111-00000-11111") String TAKEN) {
		if (Util.isEmpty(TAKEN)) {
			return LianjiuResult.build(401, "请传入正确的TAKEN");
		}
		return productService.calculationJDPrice(TAKEN);
	}

	/**
	 * 
	 * huangfu 2017年9月22日 descrption:通过产品的参数估算价格 与查询出所有已卖出的记录
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/calculationImmediately", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult calculationImmediately(String productParamData, String productId, String productPrice) {
		if (Util.isEmpty(productParamData)) {
			return LianjiuResult.build(401, "请传入正确的productParamData");
		}
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(402, "请传入正确的productId");
		}
		if (Util.isEmpty(productPrice)) {
			return LianjiuResult.build(403, "请传入正确的productPrice");
		}
		System.err.println("选好的参数：" + productParamData);
		return productService.calculationImmediately(productParamData, productId, productPrice);
	}

	/**
	 * 
	 * xyz 2017年9月22日 descrption:马上评估卖家电
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/calculationJDImmediately", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult calculationJDImmediately(String productParamData, String productId, String productPrice) {
		if (Util.isEmpty(productParamData)) {
			return LianjiuResult.build(401, "请传入正确的productParamData");
		}
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(402, "请传入正确的productId");
		}
		if (Util.isEmpty(productPrice)) {
			return LianjiuResult.build(403, "请传入正确的productPrice");
		}
		System.err.println("选好的参数：" + productParamData);
		System.out.println(productId);
		System.out.println(productPrice);
		return productService.calculationJDImmediately(productParamData, productId, productPrice);
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
		return productService.getBrands();
	}

	/**
	 * 
	 * huangfu 2017年9月27日 descrption:根据商品id返回json模板
	 * 
	 * @param repairId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getProductByProductParamTemplate/{productId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getProductByProductParamTemplate(@PathVariable String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return productService.getProductByProductParamTemplate(productId);
	}

	/**
	 * 
	 * zhaoxi 2017年9月29日 descrption:切换电子产品种类
	 * 
	 * @param categoryId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/electronicSwitch/{categoryId}/{order}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult electronicSwitch(@PathVariable long categoryId, @PathVariable Integer order) {
		if (0 == categoryId) {
			return LianjiuResult.build(401, "请此传入正确的分类id");
		}
		if (null == order) {
			return LianjiuResult.build(402, "order绑定错误");
		}
		return productService.productCategorySwitch(categoryId, order);
	}

	/**
	 * 
	 * xyz 2017年10月9日 descrption:切换品牌
	 * 
	 * @param categoryId
	 *            pageNum pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/brandSwitch/{CategoryId}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult brandSwitch(@PathVariable long CategoryId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (0 == CategoryId) {
			return LianjiuResult.build(401, "请此传入正确的分类id");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return productService.brandSwitch(CategoryId, pageNum, pageSize);
	}

	/**
	 * 
	 * xyz 2017年10月24日 descrption:摄影摄像,智能数码类别切换
	 * 
	 * @param categoryId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/categorySwitch/{CategoryId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult categorySwitch(@PathVariable long CategoryId) {
		if (0 == CategoryId) {
			return LianjiuResult.build(401, "请此传入正确的分类id");
		}
		return productService.categorySwitch(CategoryId);
	}

	/**
	 * 
	 * xyz 2017年10月9日 descrption:近期成交商品详情页
	 * 
	 * @param categoryId
	 *            pageNum pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRecentDealInfo/{orItemsId}/orItemsId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getRecentDealInfo(@PathVariable String orItemsId) {
		if (Util.isEmpty(orItemsId)) {
			return LianjiuResult.build(401, "请传入正确的orItemsId");
		}
		return productService.getRecentDealInfo(orItemsId);
	}

	/**
	 * 
	 * xyz 2017年10月9日 descrption:选择商品加入回收车
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/introductionProduct", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult introductionProduct(OrdersItem productItem, String userId) {
		// 设置勾选状态
		if (null == productItem) {
			return LianjiuResult.build(401, "请传入正确的productItem");
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		productItem.setOrItemsChooseFlag(0);
		productItem.setOrItemsVolume((long) 1);// 体积预存
		return productService.introductionProduct(productItem, userId);
	}

	/**
	 * 
	 * xyz 2017年10月18日 descrption:此接口添加到快递回收车
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addToExpressProductCar", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addToExpressProductCar(OrdersItem productItem, String userId) {
		if (null == productItem) {
			return LianjiuResult.build(401, "请传入正确的productItem");
		}
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(402, "请传入正确的userId");
		}
		// 设置勾选状态
		productItem.setOrItemsChooseFlag(0);
		productItem.setOrItemsVolume((long) 1);// 体积预存
		return productService.addToExpressProductCar(productItem, userId);
	}

	/**
	 * 
	 * xyz 2017年10月12日 descrption:查询产品
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/selectProductFromCar/{userId}/userId", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectProductFromCar(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return productService.selectProductFromCar(userId);
	}

	/**
	 * 
	 * xyz 2017年10月12日 descrption:更新订单状态
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateFaceFaceState", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateFaceFaceState(String orFacefaceId, int orFacefaceStatus) {
		if (Util.isEmpty(orFacefaceId)) {
			return LianjiuResult.build(401, "请传入正确的orFacefaceId");
		}
		return productService.updateFaceFaceState(orFacefaceId, orFacefaceStatus);
	}

	/**
	 * 
	 * xyz 2017年10月17日 descrption:首页产品搜索
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/productSearch/{keyword}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult productSearch(@PathVariable String keyword) {
		if (Util.isEmpty(keyword)) {
			return LianjiuResult.build(401, "请传入正确的keyword");
		}
		return productService.productSearch(keyword);
	}

	/**
	 * 
	 * xyz 2017年10月17日 descrption:搜索页面热卖推荐接口
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/hotProductRecommend", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult hotProductRecommend() {
		return productService.hotProductRecommend();
	}

	/**
	 * 
	 * xyz 2017年10月17日 descrption:发送验证码
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/sendExpressOrderSMS", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult sendExpressOrderSMS(String userId, String phone) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (Util.isEmpty(phone)) {
			return LianjiuResult.build(402, "请传入正确的phone");
		}
		return productService.sendExpressOrderSMS(userId, phone);
	}

}
