package com.lianjiu.rest.controller.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.ExceptionUtil;
import com.lianjiu.rest.service.search.SearchManagerService;

/**
 * 管理搜索引擎字段
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/searchManager")
public class SearchManagerController {
	@Autowired
	private SearchManagerService searchManagerService;

	@RequestMapping("/importall")
	@ResponseBody
	public LianjiuResult importAll() {
		LianjiuResult result = null;
		try {
			result = searchManagerService.importItemToIndex();
		} catch (Exception e) {
			e.printStackTrace();
			return LianjiuResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return result;
	}
}
