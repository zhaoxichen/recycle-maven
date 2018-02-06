package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.pojo.ProductPriceGroup;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Product;
import com.lianjiu.service.product.ProductService;

/**
 * 商品管理表现层
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
	 * zhaoxi 2017年8月31日 descrption: 通过分类id获取商品
	 * 
	 * @param cId
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getProduct/{cId}/{pageNum}/{pageSize}")
	@ResponseBody
	public LianjiuResult selectProductByPid(@PathVariable long cId, @PathVariable int pageNum,
			@PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productService.selectProductByPid(cId, pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:添加商品
	 * 
	 * @param product
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addProduct", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addProduct(Product product) {
		if (null == product) {
			return LianjiuResult.build(401, "product对象绑定错误");
		}
		if (Util.isEmpty(product.getProductId())) {
			return LianjiuResult.build(402, "请传入产品id");
		}
		if (Util.isEmpty(product.getProductPrice())) {
			return LianjiuResult.build(403, "请传入产品价格");
		}
		if (Util.isEmpty(product.getProductName())) {
			return LianjiuResult.build(404, "请传入产品名称");
		}
		if (Util.isEmpty(product.getProductMasterPicture())) {
			return LianjiuResult.build(405, "请传入产品主图");
		}
		if (Util.isEmpty(product.getProductParamData())) {
			return LianjiuResult.build(406, "请传入产品参数");
		}
		if (null == product.getProductVolume()) {
			product.setProductVolume((long) 1);
		}
		return productService.addProduct(product);
	}

	/**
	 * 
	 * zhaoxi 2017年8月29日 descrption:更新商品
	 * 
	 * @param product
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateProduct(Product product) {
		if (null == product) {
			return LianjiuResult.build(401, "product对象绑定错误");
		}
		if (Util.isEmpty(product.getProductId())) {
			return LianjiuResult.build(402, "请传入产品id");
		}
		if (Util.isEmpty(product.getProductPrice())) {
			return LianjiuResult.build(403, "请传入产品价格");
		}
		if (Util.isEmpty(product.getProductName())) {
			return LianjiuResult.build(404, "请传入产品名称");
		}
		if (Util.isEmpty(product.getProductMasterPicture())) {
			return LianjiuResult.build(405, "请传入产品主图");
		}
		if (Util.isEmpty(product.getProductParamData())) {
			return LianjiuResult.build(406, "请传入产品参数");
		}
		return productService.updateProduct(product);
	}

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption:删除商品
	 * 
	 * @param id
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deleteProduct/{id}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteProductByid(@PathVariable String id) {
		if (Util.isEmpty(id)) {
			return LianjiuResult.build(401, "请传入正确的id");
		}
		return productService.deleteProductByid(id);
	}

	/**
	 * 
	 * zhaoxi 2017年9月6日 descrption:根据id查询商品
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
	 * zhaoxi 2017年10月21日 descrption:添加
	 * 
	 * @param priceGroup
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addPriceGroup", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addPriceGroup(ProductPriceGroup priceGroup) {
		if (null == priceGroup) {
			return LianjiuResult.build(401, "priceGroup对象绑定错误");
		}
		Util.printModelDetails(priceGroup);
		return productService.addPriceGroup(priceGroup);
	}

	/**
	 * 
	 * zhaoxi 2017年10月21日 descrption:更新
	 * 
	 * @param priceGroup
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updatePriceGroup", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updatePriceGroup(ProductPriceGroup priceGroup) {
		if (null == priceGroup) {
			return LianjiuResult.build(401, "priceGroup对象绑定错误");
		}
		return productService.updatePriceGroup(priceGroup);
	}

	/**
	 * 
	 * zhaoxi 2017年10月21日 descrption:删除价格
	 * 
	 * @param priceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/deletePriceGroup/{priceId}", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deletePriceGroup(@PathVariable String priceId) {
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(401, "请传入正确的commentId");
		}
		return productService.deletePriceGroupById(priceId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月21日 descrption:获取当前产品下的所有价格组合
	 * 
	 * @param productId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPriceGroupList/{productId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getPriceGroupList(@PathVariable String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return productService.getPriceGroupByPid(productId);
	}

	/**
	 * 
	 * zhaoxi 2017年10月21日 descrption:获取一个价格组合
	 * 
	 * @param priceId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getPriceGroup/{priceId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getPriceGroup(@PathVariable String priceId) {
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(401, "请传入正确的productId");
		}
		return productService.getPriceGroupById(priceId);
	}
}
