package com.lianjiu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.GlobalValueJedis;
import com.lianjiu.common.utils.JsonUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Category;
import com.lianjiu.model.vo.CategoryGroud;
import com.lianjiu.rest.dao.JedisClient;
import com.lianjiu.rest.mapper.CategoryMapper;
import com.lianjiu.service.CategoryService;

/**
 * 商品分类服务层
 * 
 * @author zhaoxi
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private JedisClient jedisClient;

	private List<CategoryGroud> categoryGroupList = new ArrayList<CategoryGroud>();

	@Override
	public LianjiuResult selectByParentId(Long pid) {
		// 根据parentid查询节点列表
		List<Category> productCategorys = categoryMapper.selectByParentId(pid);
		LianjiuResult result = new LianjiuResult(productCategorys);
		result.setStatus(200);
		result.setMsg("ok");
		result.setTotal(productCategorys.size());
		return result;
	}

	@Override
	public LianjiuResult insertCategory(Category category) {
		if (null == category) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		// '状态。可选值:1(正常),2(删除)',
		category.setCategoryStatus(1);
		category.setCategorySortOrder(1);
		// 创建时间
		category.setCategoryCreated(new Date());
		category.setCategoryUpdated(new Date());
		// 插入数据库
		int rowsAffected = categoryMapper.insert(category);
		if (0 < rowsAffected) {
			/**
			 * 清除相关缓存
			 */
			Set<String> set = jedisClient.keys(GlobalValueJedis.SELECT_DESCENDANTS + "*");
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String keyStr = it.next();
				System.out.println(keyStr);
				jedisClient.del(keyStr);
			}
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult delectCategoryById(long id) {
		if (0 > id) {
			return LianjiuResult.build(501, "请指定要删除的模板id");
		}
		int rowsAffected = categoryMapper.deleteByPrimaryKey(id);
		if (0 < rowsAffected) {
			/**
			 * 清除相关缓存
			 */
			Set<String> set = jedisClient.keys(GlobalValueJedis.SELECT_DESCENDANTS + "*");
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String keyStr = it.next();
				System.out.println(keyStr);
				jedisClient.del(keyStr);
			}
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult selectDescendantsByParentId(Long id) {
		LianjiuResult result = new LianjiuResult();
		result.setStatus(200);
		result.setMsg("ok");
		/**
		 * 使用缓存优化
		 */
		String jsonCategoryList = jedisClient.get(GlobalValueJedis.SELECT_DESCENDANTS + id);
		if (Util.notEmpty(jsonCategoryList)) {
			categoryGroupList = JsonUtils.jsonToList(jsonCategoryList, CategoryGroud.class);
			if (null != categoryGroupList && 0 < categoryGroupList.size()) {
				result.setTotal(categoryGroupList.size());
				result.setLianjiuData(categoryGroupList);
				/**
				 * 重新设置时间
				 */
				jedisClient.expire(GlobalValueJedis.SELECT_DESCENDANTS + id, 3600);
				return result;
			} else {
				categoryGroupList = new ArrayList<CategoryGroud>();
			}
		}
		System.out.println("重新查询，清空容器,启动同步块");
		synchronized (this) {
			jsonCategoryList = jedisClient.get(GlobalValueJedis.SELECT_DESCENDANTS + id);// 判断是否已经处理了
			if (Util.isEmpty(jsonCategoryList)) {
				categoryGroupList.clear();
				// 递归查询
				this.getDescendants(id);
				jedisClient.set(GlobalValueJedis.SELECT_DESCENDANTS + id, JsonUtils.objectToJson(categoryGroupList));
				jedisClient.expire(GlobalValueJedis.SELECT_DESCENDANTS + id, 3600);
			}
		}
		result.setTotal(categoryGroupList.size());
		result.setLianjiuData(categoryGroupList);
		return result;
	}

	private void getDescendants(Long categoryId) {
		List<Category> categoryChildren = categoryMapper.selectByParentId(categoryId);
		// 边界条件
		if (null == categoryChildren || categoryChildren.size() == 0) {
			return;
		}
		categoryGroupList.add(new CategoryGroud(categoryId, categoryChildren));
		// 遍历
		Long nextPid;
		for (Category category : categoryChildren) {
			// 获取id作为下个查询的父级id
			nextPid = category.getCategoryId();
			// 递归
			getDescendants(nextPid);
		}
	}

	@Override
	public LianjiuResult updateCategory(Category category) {
		if (null == category) {
			return LianjiuResult.build(500, "传入对象为空");
		}
		if (0 > category.getCategoryId()) {
			return LianjiuResult.build(501, "请指定要更新的商品id");
		}
		// 存入数据库
		int rowsAffected = categoryMapper.updateByPrimaryKeySelective(category);
		if (0 < rowsAffected) {
			/**
			 * 清除相关缓存
			 */
			Set<String> set = jedisClient.keys(GlobalValueJedis.SELECT_DESCENDANTS + "*");
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String keyStr = it.next();
				System.out.println(keyStr);
				jedisClient.del(keyStr);
			}
		}
		return LianjiuResult.ok(rowsAffected);
	}

	@Override
	public LianjiuResult selectCategoryById(Long categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		return LianjiuResult.ok(category);
	}

}
