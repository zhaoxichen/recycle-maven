package com.lianjiu.service.user.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.MD5Util;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.RecyclingWarehouseman;
import com.lianjiu.rest.mapper.user.RecyclingWarehousemanMapper;
import com.lianjiu.service.user.WarehousemanService;

@Service
public class WarehousemanServiceImpl implements WarehousemanService {
	@Autowired
	private RecyclingWarehousemanMapper warehousemanMapper;

	private static Logger loggerWarehouseman = Logger.getLogger("warehouseman");

	/**
	 * 获取所有大宗仓管员
	 */
	@Override
	public LianjiuResult getWarehousemanAll(int pageNum, int pageSize) {
		loggerWarehouseman.info("开始分页查询大宗仓管员");
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		List<RecyclingWarehouseman> listWarehouseman = warehousemanMapper.getAll();
		if (pageNum == 1 && 0 == listWarehouseman.size()) {
			loggerWarehouseman.info("仓管员为空");
			return LianjiuResult.build(501, "尚无仓管员");
		}
		if (0 == listWarehouseman.size()) {
			loggerWarehouseman.info("仓管员为空");
			return LianjiuResult.build(502, "尚无更多仓管员");
		} else {
			// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
			Page<RecyclingWarehouseman> pageFurniture = (Page<RecyclingWarehouseman>) listWarehouseman;
			loggerWarehouseman.info("总页数：" + pageFurniture.getPages());
			// 封装成相应数据对象
			LianjiuResult result = new LianjiuResult(listWarehouseman);
			result.setTotal(pageFurniture.getTotal());
			return LianjiuResult.ok(listWarehouseman);
		}

	}

	/**
	 * 根据编号获取大宗仓管员
	 */
	@Override
	public LianjiuResult getWarehousemanById(String recyclerId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 生成一个大宗登陆账号
	 */
	@Override
	public LianjiuResult addWarehouseman(RecyclingWarehouseman warehouseman) {
		loggerWarehouseman.info("开始添加大宗仓管员登陆账号");
		// 查当前号码是否存在数据库
		String recyclerId = warehousemanMapper.getIdByPhone(warehouseman.getRecyclerPhone());
		if (Util.notEmpty(recyclerId)) {
			loggerWarehouseman.info("电话号码已经存在");
			return LianjiuResult.build(501, "电话号码已存在");
		}
		/*
		 * // 查当前号码是否存在数据库 String id =
		 * warehousemanMapper.getIdByName(warehouseman.getRecyclerName()); if
		 * (Util.notEmpty(id)) { loggerWarehouseman.info("该用户名号码已经存在"); return
		 * LianjiuResult.build(502, "用户名已存在"); }
		 */
		RecyclingWarehouseman rWarehouseman = new RecyclingWarehouseman();
		String pwd = warehouseman.getRecyclerPassword();
		String password = MD5Util.md5(pwd);
		rWarehouseman.setRecyclerId(IDUtils.genRecyclerId());
		rWarehouseman.setRecyclerPassword(password);
		rWarehouseman.setCategoryId("0");
		rWarehouseman.setRecyclerType(warehouseman.getRecyclerType());
		rWarehouseman.setRecyclerName(warehouseman.getRecyclerName());
		rWarehouseman.setRecyclerPhone(warehouseman.getRecyclerPhone());
		rWarehouseman.setRecyclerOperator(warehouseman.getRecyclerOperator());
		int rowsAffected = warehousemanMapper.insert(rWarehouseman);
		if (0 >= rowsAffected) {
			loggerWarehouseman.info("插入用户数据出错：" + rWarehouseman);
			return LianjiuResult.build(503, "创建失败");
		}
		rWarehouseman.setRecyclerPassword(pwd);
		loggerWarehouseman.info("创建账号成功：" + rWarehouseman);
		return LianjiuResult.ok(rWarehouseman);
	}

	/**
	 * 更新修改仓管员
	 */
	@Override
	public LianjiuResult modifyWarehouseman(RecyclingWarehouseman warehouseman) {
		loggerWarehouseman.info("开始修改仓管员");
		RecyclingWarehouseman rWarehouseman = warehousemanMapper.selectByPrimaryKey(warehouseman.getRecyclerId());
		if (null == rWarehouseman || "".equals(rWarehouseman)) {
			loggerWarehouseman.info("修改失败，id查不到用户信息");
			return LianjiuResult.build(501, "修改失败");
		}
		String oPhone = rWarehouseman.getRecyclerPhone();
		// 查当前号码是否存在数据库
		String recyclerPhone = warehouseman.getRecyclerPhone();
		if (!recyclerPhone.equals(oPhone)) {
			int count = warehousemanMapper.getCountByOphone(recyclerPhone);
			if (0 < count) {
				loggerWarehouseman.info("电话号码重复");
				return LianjiuResult.build(502, "该电话号码已绑定其他账号");
			}
			rWarehouseman.setRecyclerPhone(recyclerPhone);
		}
		// 查当前号码是否存在数据库
		rWarehouseman.setRecyclerId(warehouseman.getRecyclerId());
		rWarehouseman.setCategoryId("0");
		rWarehouseman.setRecyclerType(warehouseman.getRecyclerType());
		rWarehouseman.setRecyclerName(warehouseman.getRecyclerName());
		rWarehouseman.setRecyclerPhone(warehouseman.getRecyclerPhone());
		rWarehouseman.setRecyclerOperator(warehouseman.getRecyclerOperator());
		int rowsAffected = warehousemanMapper.updateByPrimaryKeySelective(rWarehouseman);
		if (0 >= rowsAffected) {
			loggerWarehouseman.info("更新用户数据出错：" + rWarehouseman);
			return LianjiuResult.build(505, "修改失败");
		}
		loggerWarehouseman.info("更新成功：" + rWarehouseman);
		return LianjiuResult.ok(rWarehouseman);
	}

	/**
	 * 删除账号登陆
	 */
	@Override
	public LianjiuResult removeWarehouseman(String recyclerId) {
		loggerWarehouseman.info("开始删除");
		try {
			int rowsAffected = warehousemanMapper.modifyIsExsit(recyclerId);
			if (0 == rowsAffected) {
				loggerWarehouseman.info("删除失败");
				return LianjiuResult.build(501, "删除失败");
			}
			loggerWarehouseman.info("删除成功");
			return LianjiuResult.ok(rowsAffected);
		} catch (RuntimeException e) {
			loggerWarehouseman.info("数据异常：" + e.getMessage());
			return LianjiuResult.build(502, "数据异常！");
		}
	}

}
