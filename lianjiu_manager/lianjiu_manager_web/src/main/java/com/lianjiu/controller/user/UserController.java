package com.lianjiu.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.Recommend;
import com.lianjiu.model.Role;
import com.lianjiu.model.User;
import com.lianjiu.service.user.UserService;

/**
 * 用户管理表现层
 * 
 * @author whd
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 
	 * zhaoxi 2017年8月24日 descrption:获取所有的用户信息
	 * 
	 * @param pageBegin
	 * @param pageTotal
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/userAll/{pageNum}/{pageSize}")
	@ResponseBody
	public LianjiuResult selectAllUser(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return userService.selectAllUser(pageNum, pageSize);
	}

	/**
	 * 
	 * zhaoxi 2017年11月27日 descrption:通过主键查询
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getUser/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult selectUserByUserId(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入userId");
		}
		return userService.selectUserByUserId(userId);
	}

	/**
	 * 
	 * zhaoxi 2017年11月24日 descrption:用户详细信息
	 * 
	 * @param detailsId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/userDetails/{detailsId}")
	@ResponseBody
	public LianjiuResult selectDetailsByUserId(@PathVariable String detailsId) {
		if (Util.isEmpty(detailsId)) {
			return LianjiuResult.build(401, "请传入detailsId");
		}
		return userService.selectDetailsByUserId(detailsId);
	}

	/**
	 * 
	 * huangfu 2017年9月15日 descrption:获取该用户的所有权限对象
	 * 
	 * @param pageBegin
	 * @param pageTotal
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getByPowerId/{userId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getUserByPowerId(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getUserByPowerId(userId);
	}

	/**
	 * 
	 * huangfu 2017年9月15日 descrption:获取该用户的所有角色对象
	 * 
	 * @param pageBegin
	 * @param pageTotal
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getByRoleId/{userId}/ById", method = RequestMethod.GET)
	@ResponseBody
	public LianjiuResult getByRoleId(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.getByRoleId(userId);
	}

	/**
	 * 
	 * huangfu 2017年9月15日 descrption:删除用户
	 * 
	 * @param roleId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/removeUser/{userId}/id=126", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult deleteRole(@PathVariable String userId) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		return userService.deleteUser(userId);
	}

	/**
	 * 
	 * huangfu 2017年9月19日 descrption:用户的角色分配
	 * 
	 * @param userId
	 *            roleList
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/updateRoleAllot/ById", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult updateRoleAllot(String userId, List<Role> roleList) {
		if (Util.isEmpty(userId)) {
			return LianjiuResult.build(401, "请传入正确的userId");
		}
		if (null == roleList) {
			return LianjiuResult.build(401, "roleList对象绑定错误");
		}
		return userService.updateRoleAllot(userId, roleList);
	}

	/**
	 * 
	 * huangfu 2017年9月19日 descrption:推荐人添加
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/addRecommend", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult addRecommend(Recommend recommend) {
		if (null == recommend) {
			return LianjiuResult.build(401, "参数绑定错误");
		}
		if (Util.isEmpty(recommend.getRecommendName())) {
			return LianjiuResult.build(402, "请填写真实姓名");
		}
		if (Util.isEmpty(recommend.getRecommendPhone())) {
			return LianjiuResult.build(403, "请填写手机号");
		}
		if (Util.isEmpty(recommend.getRecemmendOperator())) {
			return LianjiuResult.build(404, "请填写操作人");
		}

		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.addRecommend(recommend);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * huangfu 2017年9月19日 descrption:推荐人修改
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/modifyRecommend", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult modifyRecommend(Recommend recommend) {
		if (null == recommend) {
			return LianjiuResult.build(401, "参数绑定错误");
		}

		if (Util.isEmpty(recommend.getRecommendId())) {
			return LianjiuResult.build(402, "请填写推荐人编号");
		}
		if (Util.isEmpty(recommend.getRecommendName())) {
			return LianjiuResult.build(403, "请填写姓名");
		}
		if (Util.isEmpty(recommend.getRecommendPhone())) {
			return LianjiuResult.build(404, "请填写手机号");
		}

		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.modifyRecommend(recommend);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * huangfu 2017年9月19日 descrption:多参数查询推荐人
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/searchRecommend", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult searchRecommend(String parameter,int pageSize,int pageNum) {
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.searchRecommend(parameter,pageSize,pageNum);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * hongda 2017年9月19日 descrption:根据推荐人查询用户信息
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getUserByRecommend", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getUserByRecommend(String referrer, String startTime, String endTime, int pageNum,
			int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(404, "开始页必须大于0");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(405, "页大小必须大于0");
		}
		// 推荐人-1 开始时间-1 结束时间必须为 1
		if (Util.notEmpty(referrer) && Util.notEmpty(startTime)) {
			if (Util.isEmpty(endTime)) {
				return LianjiuResult.build(401, "请填写结束时间");
			}
			return returnGetUser(referrer, startTime, endTime, pageNum, pageSize);
		}
		// 推荐人-1 结束时间-1 开始时间必须为 1
		if (Util.notEmpty(referrer) && Util.notEmpty(endTime)) {
			if (Util.isEmpty(startTime)) {
				return LianjiuResult.build(402, "请填写开始时间");
			}
			return returnGetUser(referrer, startTime, endTime, pageNum, pageSize);
		}
		if (Util.isEmpty(referrer)) {
			if (Util.isEmpty(startTime) || Util.isEmpty(endTime)) {
				return LianjiuResult.build(403, "请填写完整起始时间");
			}
			return returnGetUser(referrer, startTime, endTime, pageNum, pageSize);
		}
		return returnGetUser(referrer, startTime, endTime, pageNum, pageSize);
	}

	private LianjiuResult returnGetUser(String referrer, String startTime, String endTime, int pageNum, int pageSize) {
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.getUserByRecommend(referrer, startTime, endTime, pageNum, pageSize);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * hongda 2017年9月19日 descrption:根据推荐人查询用户信息
	 * 
	 * @param userId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/getRecordByFaceface", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult getRecordByFaceface(String referrer, String startTime, String endTime, int categoryId, int pageNum,
			int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(404, "开始页必须大于0");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(405, "页大小必须大于0");
		}
		if (Util.isEmpty(categoryId+"")) {
			return LianjiuResult.build(406, "请填写分类编号");
		}
		// 推荐人-1 开始时间-1 结束时间必须为 1
		if (Util.notEmpty(referrer) && Util.notEmpty(startTime)) {
			if (Util.isEmpty(endTime)) {
				return LianjiuResult.build(401, "请填写结束时间");
			}
			return recordByFaceface(referrer, startTime, endTime, categoryId, pageNum, pageSize);
		}
		// 推荐人-1 结束时间-1 开始时间必须为 1
		if (Util.notEmpty(referrer) && Util.notEmpty(endTime)) {
			if (Util.isEmpty(startTime)) {
				return LianjiuResult.build(402, "请填写开始时间");
			}
			return recordByFaceface(referrer, startTime, endTime, categoryId, pageNum, pageSize);
		}
		if (Util.isEmpty(referrer)) {
			if (Util.isEmpty(startTime) || Util.isEmpty(endTime)) {
				return LianjiuResult.build(403, "请填写完整起始时间");
			}
			return recordByFaceface(referrer, startTime, endTime, categoryId, pageNum, pageSize);
		}
		return recordByFaceface(referrer, startTime, endTime, categoryId, pageNum, pageSize);
	}

	private LianjiuResult recordByFaceface(String referrer, String startTime, String endTime, int categoryId, int pageNum,
			int pageSize) {
		try {
			// 返回json数据 状态码+ 响应信息 + 响应数据对象
			LianjiuResult result = userService.getRecordByFaceface(referrer, startTime, endTime, categoryId, pageNum,
					pageSize);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 
	 * huangfu 2017年9月19日 descrption:用户的角色分配测试
	 * 
	 * @param userId
	 *            roleList
	 * @return LianjiuResult
	 */
	/*
	 * 
	 * @RequestMapping(value = "/updateRoleAllot/ById", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public LianjiuResult updateRoleAllot(String userId,String
	 * roleList) { return userService.updateRoleAllot(userId,roleList); }
	 */

	/**
	 * 
	 * huangfu 2017年1月9日 descrption:用户信息模糊查询
	 * 
	 * @param ordersId
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/vagueQuery", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult vagueQuery(User user, int pageNum, int pageSize, String cratedStart, String cratedOver) {
		// if(null == ordersRepair){
		// return LianjiuResult.build(401, "ordersRepair对象绑定错误");
		// }
		System.out.println("user getUserId：" + user.getUserId());
		System.out.println("user getUserPhone：" + user.getUserPhone());
		System.out.println("cratedStart：" + cratedStart);
		System.out.println("cratedOver：" + cratedOver);
		System.out.println("pageNum：" + pageNum);
		System.out.println("pageSize：" + pageSize);

		if (1 > pageNum) {
			return LianjiuResult.build(402, "请传入大于0的pageNum");
		}
		if (1 > pageSize) {
			return LianjiuResult.build(403, "请传入大于0的pageSize");
		}
		return userService.vagueQuery(user, pageNum, pageSize, cratedStart, cratedOver);
	}
}
