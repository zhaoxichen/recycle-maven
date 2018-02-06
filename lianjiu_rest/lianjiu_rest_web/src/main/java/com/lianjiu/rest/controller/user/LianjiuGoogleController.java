package com.lianjiu.rest.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.utils.HttpClientUtil;

import net.sf.json.JSONObject;

/**
 * 谷歌地图
 * 
 * @author zhaoxi
 *
 */
@Controller
@RequestMapping("/lianjiuGoogle")
public class LianjiuGoogleController {

	@RequestMapping(value = "/search/{city}/{keywords}", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject addressSearch(@PathVariable String city, @PathVariable String keywords) {
		System.out.println(city);
		String url = "https://restapi.amap.com/v3/assistant/inputtips?s=rsv3&key=9ad32d2473c2bbdda7e49ecdf2f316fd&city="
				+ city
				+ "&citylimit=true&callback=jsonp_944_&platform=JS&logversion=2.0&sdkversion=1.3&appname=http%3A%2F%2Flocalhost%3A8989%2F%23%2Fditu&csid=C830EB34-A7C5-4D78-9BBC-B30AAEDFEA6F&keywords="
				+ keywords;
		String res = HttpClientUtil.doGet(url);
		res =  res.substring(11,res.length()-1);
		System.out.println(res);
		JSONObject kkk = JSONObject.fromObject(res);
		return kkk;
	}
}
