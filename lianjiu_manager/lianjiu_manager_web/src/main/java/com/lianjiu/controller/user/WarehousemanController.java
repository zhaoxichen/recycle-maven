package com.lianjiu.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.RecyclingWarehouseman;
import com.lianjiu.service.user.WarehousemanService;

/**
 * 大宗仓管员管理表现层
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/warehouseman")
public class WarehousemanController {
	@Autowired
	private WarehousemanService warehousemanService;

	/**
	 * 
	 * hongda 2017年8月24日 descrption:获取所有的用户信息
	 * 
	 * @param pageBegin
	 * @param pageTotal
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/warehousemanAll", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getWarehousemanAll(int pageNum, int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return warehousemanService.getWarehousemanAll(pageNum, pageSize);
	}

	/**
	 * 
	 * hongda 2017年11月27日 descrption:通过主键查询
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getWarehousemanById", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getWarehousemanById(String recyclerId) {
		if (Util.isEmpty(recyclerId)) {
			return LianjiuResult.build(401, "请传入recyclerId");
		}
		return warehousemanService.getWarehousemanById(recyclerId);
	}

	/**
	 * 
	 * hongda 2017年11月27日 descrption:添加生成一个大宗仓管员账号
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addWarehouseman", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addWarehouseman(RecyclingWarehouseman warehouseman) {
		if (null == warehouseman) {
			return LianjiuResult.build(401, "请传入warehouseman对象");
		}
		if (null == warehouseman.getRecyclerName() || "".equals(warehouseman.getRecyclerName())) {
			return LianjiuResult.build(402, "请传入用户名");
		}
		if (null == warehouseman.getRecyclerPhone() || "".equals(warehouseman.getRecyclerPhone())) {
			return LianjiuResult.build(403, "请传入电话号码");
		}
		if (null == warehouseman.getRecyclerPassword() || "".equals(warehouseman.getRecyclerPassword())) {
			return LianjiuResult.build(404, "请传入密码");
		}
		if (null == warehouseman.getRecyclerType() || "".equals(warehouseman.getRecyclerType())) {
			return LianjiuResult.build(405, "请传入类型");
		}
		if (null == warehouseman.getRecyclerOperator() || "".equals(warehouseman.getRecyclerOperator())) {
			return LianjiuResult.build(405, "请传入操作员");
		}
		try {
			boolean lean = Util.isMobile(warehouseman.getRecyclerPhone());
			if (true == lean) {
				return warehousemanService.addWarehouseman(warehouseman);
			}
			return LianjiuResult.build(406, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * hongda 2017年11月27日 descrption:修改大宗仓管员账号
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyWarehouseman", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyWarehouseman(RecyclingWarehouseman warehouseman) {
		if (null == warehouseman) {
			return LianjiuResult.build(401, "请传入warehouseman对象");
		}
		if (null == warehouseman.getRecyclerId() || "".equals(warehouseman.getRecyclerId())) {
			return LianjiuResult.build(402, "用户编号");
		}
		if (null == warehouseman.getRecyclerName() || "".equals(warehouseman.getRecyclerName())) {
			return LianjiuResult.build(403, "请传入用户名");
		}
		if (null == warehouseman.getRecyclerPhone() || "".equals(warehouseman.getRecyclerPhone())) {
			return LianjiuResult.build(404, "请传入电话号码");
		}
		if (null == warehouseman.getRecyclerType() || "".equals(warehouseman.getRecyclerType())) {
			return LianjiuResult.build(406, "请传入类型");
		}
		if (null == warehouseman.getRecyclerOperator() || "".equals(warehouseman.getRecyclerOperator())) {
			return LianjiuResult.build(406, "请传入操作员");
		}
		try {
			boolean lean = Util.isMobile(warehouseman.getRecyclerPhone());
			if (true == lean) {
				return warehousemanService.modifyWarehouseman(warehouseman);
			}
			return LianjiuResult.build(406, "请输入正确的电话号码");
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	/**
	 * 
	 * hongda 2017年11月27日 descrption:添加生成一个大宗仓管员账号
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeWarehouseman", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult removeWarehouseman(String recyclerId) {
		if (Util.isEmpty(recyclerId)) {
			return LianjiuResult.build(401, "请传入用户编号");
		}
		return warehousemanService.removeWarehouseman(recyclerId);
	}
}
