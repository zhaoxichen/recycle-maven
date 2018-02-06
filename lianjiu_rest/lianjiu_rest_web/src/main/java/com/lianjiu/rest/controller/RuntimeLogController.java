package com.lianjiu.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.logs.SimpleLog;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;

/**
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/log")
public class RuntimeLogController {
	/**
	 * 
	 * zhaoxi 2017年11月28日 descrption:保存日志
	 * 
	 * @param massage
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult inerstVersion(@RequestParam(defaultValue = "h5") String fileName, String massage) {
		SimpleLog simpleLog = new SimpleLog(Util.createDirOnRoot("H5/runtimeLogs") + "/" + fileName + ".log");
		simpleLog.log(massage);
		return LianjiuResult.ok("/runtimeLogs/" + fileName + ".log");
	}
}
