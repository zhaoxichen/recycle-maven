package com.lianjiu.service.product.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.pojo.ProductPriceGroup;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Product;
import com.lianjiu.model.ProductParamTemplate;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.product.ProductMapper;
import com.lianjiu.rest.mapper.product.ProductParamTemplateMapper;
import com.lianjiu.rest.mapper.product.ProductPriceGroupMapper;
import com.lianjiu.service.product.ProductService;

/**
 * 商品管理服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductMapper productMapper;
	@Autowired
	ProductParamTemplateMapper productParamTemplateMapper;
	@Autowired
	ProductPriceGroupMapper productPriceGroupMapper;
	@Autowired
	JedisClient jedisClient;

	@Override
	public LianjiuResult selectProductByPid(long cId, int pageNum, int pageSize) {
		Product p = new Product();
		p.setCategoryId(cId);
		SearchObjecVo vo = new SearchObjecVo(p);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<Product> products = productMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<Product> listProduct = (Page<Product>) products;
		System.out.println("总页数：" + listProduct.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(products);
		result.setTotal(listProduct.getTotal());
		return result;
	}

	@Override
	public LianjiuResult addProduct(Product product) {
		// 设置id
		product.setProductId(IDUtils.genGUIDs());
		product.setProductStatus(1);// 默认可售
		// 创建时间
		product.setProductCreated(new Date());
		product.setProductUpdated(new Date());
		// 存入数据库
		int rowsAffected = productMapper.insert(product);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult getParamTemplateFromCategory(String cid) {
		ProductParamTemplate paramTemplate = productParamTemplateMapper.selectById(cid);
		return LianjiuResult.ok(paramTemplate);
	}

	/**
	 * 产品更新了
	 */
	@Override
	public LianjiuResult updateProduct(Product product) {
		if (Util.isEmpty(product.getProductId())) {
			return LianjiuResult.build(501, "请指定要更新的商品id");
		}
		product.setProductStatus(1);// 默认可售
		// 存入数据库
		product.setProductUpdated(new Date());
		int rowsAffected = productMapper.updateByPrimaryKeySelective(product);
		// 告知该产品的价格已经更新
		jedisClient.set(GlobalValueJedis.PRODUCT_PRICE_UPDATE + product.getProductId(), product.getProductPrice());
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult deleteProductByid(String id) {
		if (Util.isEmpty(id)) {
			return LianjiuResult.build(501, "请指定要删除的商品id");
		}
		// 去数据库删除
		int rowsAffected = productMapper.deleteByPrimaryKey(id);
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult selectProductByid(String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "请指定要查询的商品id");
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		return LianjiuResult.ok(product);
	}

	@Override
	public LianjiuResult addPriceGroup(ProductPriceGroup priceGroup) {
		String priceId = IDUtils.genProductPriceId();
		priceGroup.setPriceId(priceId);
		priceGroup.setPriceCreated(new Date());
		priceGroup.setPriceUpdated(new Date());
		productPriceGroupMapper.insertSelective(priceGroup);
		return LianjiuResult.ok(priceId);
	}

	@Override
	public LianjiuResult updatePriceGroup(ProductPriceGroup priceGroup) {
		String priceId = priceGroup.getPriceId();
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(501, "主键id不能为空");
		}
		priceGroup.setPriceUpdated(new Date());
		productPriceGroupMapper.updateByPrimaryKeySelective(priceGroup);
		return LianjiuResult.ok(priceId);
	}

	@Override
	public LianjiuResult deletePriceGroupById(String priceId) {
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(501, "主键id不能为空");
		}
		int rowAffect = productPriceGroupMapper.deleteByPrimaryKey(priceId);
		return LianjiuResult.ok(rowAffect);
	}

	@Override
	public LianjiuResult getPriceGroupByPid(String productId) {
		if (Util.isEmpty(productId)) {
			return LianjiuResult.build(501, "产品id不能为空");
		}
		List<ProductPriceGroup> productPriceGroups = productPriceGroupMapper.selectByPid(productId);
		return LianjiuResult.ok(productPriceGroups);
	}

	@Override
	public LianjiuResult getPriceGroupById(String priceId) {
		if (Util.isEmpty(priceId)) {
			return LianjiuResult.build(501, "主键id不能为空");
		}
		ProductPriceGroup productPriceGroup = productPriceGroupMapper.selectByPrimaryKey(priceId);
		return LianjiuResult.ok(productPriceGroup);
	}

}
