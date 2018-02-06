package com.lianjiu.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.ProductBulk;
import com.lianjiu.rest.model.BulkVo;
import com.lianjiu.service.product.ProductBulkService;

/**
 * 大宗产品信息
 * 
 * @author huangfu
 *
 */
@Controller
@RequestMapping("/bulk")
public class ProductBulkController {

	@Autowired
	ProductBulkService productBulkService;

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:分页获取所有大宗产品
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getAll", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getAll(int pageNum, int pageSize) {
		System.out.println("分页获取所有大宗产品             。");
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productBulkService.getAll(pageNum, pageSize);
	}
	
	/**
	 * 
	 * huangfu 2018年1月8日 descrption:分页获取大宗产品
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBulk", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getBulkByCid(Long cid, int pageNum, int pageSize) {
		System.out.println("分页获取大宗产品             。");
		if (null == cid) {
			return LianjiuResult.build(401, "cid绑定错误");
		}
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return productBulkService.getBulkByCid(cid, pageNum, pageSize);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:添加大宗产品
	 * 
	 * @param bulk
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addBulk", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addBulk(ProductBulk bulk) {
		System.out.println("添加大宗产品             。");
		if (null == bulk) {
			return LianjiuResult.build(401, "bulk对象绑定错误");
		}
		return productBulkService.addBulk(bulk);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:更新大宗产品
	 * 
	 * @param bulk
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyBulk", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateBulk(BulkVo bulk) {
		if (null == bulk) {
			return LianjiuResult.build(401, "bulk对象绑定错误");
		}
		return productBulkService.updateBulk(bulk);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:移除大宗产品
	 * 
	 * @param bulkId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteBulk(String bulkId) {
		if (Util.isEmpty(bulkId)) {
			return LianjiuResult.build(401, "请传入正确的bulkId");
		}
		return productBulkService.deleteBulk(bulkId);
	}

	/**
	 * 
	 * huangfu 2018年1月8日 descrption:移除大宗产品
	 * 
	 * @param bulkId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyBulkAddress", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyBulkAddress(Long cid,String address) {
		if (Util.isEmpty(address)) {
			return LianjiuResult.build(401, "请传入正确的bulkId");
		}
		return productBulkService.modifyBulkAddress(cid,address);
	}


	/**
	 * 
	 * huangfu 2018年1月8日 descrption:查询大宗产品地址
	 * 
	 * @param cid
	 * @param pageNum
	 * @param pageSize
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getBulkAddress", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getBulkAddress(Long cid) {
		System.out.println("查询大宗产品地址。");
		if (null == cid) {
			return LianjiuResult.build(401, "cid绑定错误");
		}
		return productBulkService.getBulkAddress(cid);
	}
	
}
