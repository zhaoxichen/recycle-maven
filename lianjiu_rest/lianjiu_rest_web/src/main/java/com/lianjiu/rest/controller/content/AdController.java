package com.lianjiu.rest.controller.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.rest.service.content.AdService;

/**
 * 广告
 * 
 * @author xyz
 *
 */
@Controller
@RequestMapping("/ad/")
public class AdController {
	@Autowired
	private AdService adService;
	/**
	 * 轮播图添加
	 * @param record
	 * @return
	 */
	
	/**
	 * 按轮播图id查询
	 * @param caId :轮播图id
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectCararouseById/{caId}/caId", method = RequestMethod.GET)
	public LianjiuResult selectByPrimaryKey(@PathVariable String caId) {
		if(Util.isEmpty(caId)){
			return LianjiuResult.build(401, "请指定要查询的轮播图Id");
		}
		return adService.selectByPrimaryKey(caId);
	}
	/**
	 * 根据页码查询轮播图list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectCararouseList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectCararouseList(@PathVariable int pageNum,@PathVariable  int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectCararouseList(pageNum,pageSize);
	}
	
	/**
	 * 根据页码查询轮播图list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectCararouseFour", method = RequestMethod.GET)
	public LianjiuResult selectCararouseFour() {
		return adService.selectCararouseFour();
	}
	/**
	 * 根据页码查询轮播图list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectCararouseFourType/{type}", method = RequestMethod.GET)
	public LianjiuResult selectCararouseFourType(@PathVariable int type) {
		return adService.selectCararouseFourType(type);
	}
	
	/**
	 * 按电器广告id查询
	 * @param eleId :电器广告id
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectElectronicById/{eleId}/eleId", method = RequestMethod.GET)
	public LianjiuResult selectElectronicById(@PathVariable String eleId) {
		if(Util.isEmpty(eleId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectElectronicById(eleId);
	}
	/**
	 * 根据页码查询电器广告list
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="selectElectronicList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectElectronicList(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectElectronicList(pageNum,pageSize);
	}
	
	
	/**
	 * 按热卖产品id查询
	 * @param hotId :热卖产品id
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectHotProductById/{hotId}/hotId", method = RequestMethod.GET)
	public LianjiuResult selectHotProductById(@PathVariable String hotId) {
		if(Util.isEmpty(hotId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectHotProductById(hotId);
	}
	
	/**
	 * 根据页码查询热卖产品list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectHotProductList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectHotProductList(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectHotProductList(pageNum,pageSize);
	}
	
	/**
	 *	h5首页前4个热门产品 
	 * @author xyz
	 */
	@ResponseBody
	@RequestMapping(value="selectHotProductFour", method = RequestMethod.GET)
	public LianjiuResult selectHotProductFour() {
		return adService.selectHotProductFour();
	}
	
	 /** 
	  * 根据页码查询精品热卖list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdEssenceHotFour", method = RequestMethod.GET)
	public LianjiuResult selectAdEssenceHotList() {
		return adService.selectAdEssenceHotFour();
	}
	
	/**
	 * 按二层广告id查询
	 * @param topId :热门话题id
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdSecondById/{secId}/secId", method = RequestMethod.GET)
	public LianjiuResult selectAdSecondById(@PathVariable String secId) {
		if(Util.isEmpty(secId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectAdSecondById(secId);
	}
	/**
	 * 根据页码查询二层广告list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdSecondList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectAdSecondList(@PathVariable int pageNum,@PathVariable  int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectAdSecondList(pageNum,pageSize);
	}
	

	/**
	 * 按广告主题id查询
	 * @param theId :广告主题id
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdThemeById/{theId}/theId", method = RequestMethod.GET)
	public LianjiuResult selectAdThemeById(@PathVariable String theId) {
		if(Util.isEmpty(theId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectAdThemeById(theId);
	}
	
	/**
	 * 根据页码查询广告主题list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdThemeList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectAdThemeList(@PathVariable int pageNum,@PathVariable  int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectAdThemeList(pageNum,pageSize);
	}
	
	/**
	 * 按三层广告id查询
	 * @param thiId :三层广告id
	 * @return
	 *
	 */
	@ResponseBody	
	@RequestMapping(value="selectAdThirdById/{thiId}/thiId", method = RequestMethod.GET)
	public LianjiuResult selectAdThirdById(@PathVariable String thiId) {
		if(Util.isEmpty(thiId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectAdThirdById(thiId);
	}
	
	 /** 
	  * 根据页码查询三层广告list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdThirdList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectAdThirdList(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectAdThirdList(pageNum,pageSize);
	}
	
}
