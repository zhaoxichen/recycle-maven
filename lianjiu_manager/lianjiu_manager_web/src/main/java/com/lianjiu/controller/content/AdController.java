package com.lianjiu.controller.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AdCarousel;
import com.lianjiu.model.AdElectronic;
import com.lianjiu.model.AdEssenceBrand;
import com.lianjiu.model.AdEssenceHot;
import com.lianjiu.model.AdHotProduct;
import com.lianjiu.model.AdSecond;
import com.lianjiu.model.AdTheme;
import com.lianjiu.model.AdThird;
import com.lianjiu.service.content.AdService;

/**
 * 广告
 * 
 * @author xiangyang
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
	@ResponseBody
	@RequestMapping(value="addCararouse", method = RequestMethod.POST)
	public LianjiuResult insertCarousel(HttpServletRequest request, AdCarousel record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertCarousel(record);
	}
	/**
	 *  轮播图删除
	 * @param caId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteCararouse", method = RequestMethod.POST)
	public LianjiuResult deleteCararouse(String caId) {
		if(Util.isEmpty(caId)){
			return LianjiuResult.build(401, "请指定要删除的轮播图Id");
		}
		return adService.deleteCararouse(caId);
	}
	/**
	 * 更新修改轮播图内容
	 * @param AdActivity :活动广告
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateCararouse", method = RequestMethod.POST)
	public LianjiuResult updateCararouse(AdCarousel record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateByPrimaryKeySelective(record);
	}
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
	 * 电器广告添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertElectronic", method = RequestMethod.POST)
	public LianjiuResult insertElectronic(HttpServletRequest request, AdElectronic record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertElectronic(record);
	}
	/**
	 *  电器广告删除
	 * @param caId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteElectronic", method = RequestMethod.POST)
	public LianjiuResult deleteElectronic(String eleId) {
		if(Util.isEmpty(eleId)){
			return LianjiuResult.build(401, "请指定要删除的电器广告Id");
		}
		return adService.deleteElectronic(eleId);
	}
	/**
	 * 更新修改电器广告内容
	 * @param AdActivity :活动广告
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateElectronic", method = RequestMethod.POST)
	public LianjiuResult updateElectronic(AdElectronic record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateElectronic(record);
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
	 *
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
	 * 热卖产品添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertHotProduct", method = RequestMethod.POST)
	public LianjiuResult insertHotProduct(HttpServletRequest request, AdHotProduct record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertHotProduct(record);
	}
	/**
	 *  热卖产品删除
	 * @param hotId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteHotProduct", method = RequestMethod.POST)
	public LianjiuResult deleteHotProduct(String hotId) {
		if(Util.isEmpty(hotId)){
			return LianjiuResult.build(401, "请指定要删除的热门产品Id");
		}
		return adService.deleteHotProduct(hotId);
	}
	/**
	 * 更新热卖产品内容
	 * @param 
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateHotProduct", method = RequestMethod.POST)
	public LianjiuResult updateHotProduct(AdHotProduct record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateHotProduct(record);
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
	 * 二层广告添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertAdSecond", method = RequestMethod.POST)
	public LianjiuResult insertAdSecond(HttpServletRequest request, AdSecond record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertAdSecond(record);
	}
	
	/**
	 *   二层广告删除
	 * @param hotId  二层广告id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteAdSecond", method = RequestMethod.POST)
	public LianjiuResult deleteAdSecond(String secId) {
		if(Util.isEmpty(secId)){
			return LianjiuResult.build(401, "请指定要删除的热门产品Id");
		}
		return adService.deleteAdSecond(secId);
	}
	/**
	 * 更新二层广告内容
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateAdSecond", method = RequestMethod.POST)
	public LianjiuResult updateAdSecond(AdSecond record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateAdSecond(record);
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
	 * 广告主题添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertAdTheme", method = RequestMethod.POST)
	public LianjiuResult insertAdTheme(HttpServletRequest request, AdTheme record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertAdTheme(record);
	}
	
	/**
	 *   广告主题删除
	 * @param theId   广告主题id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteAdTheme", method = RequestMethod.POST)
	public LianjiuResult deleteAdTheme(String theId) {
		if(Util.isEmpty(theId)){
			return LianjiuResult.build(401, "请指定要删除的热门产品Id");
		}
		return adService.deleteAdTheme(theId);
	}
	/**
	 * 更新广告主题内容
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateAdTheme", method = RequestMethod.POST)
	public LianjiuResult updateAdTheme(AdTheme record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		
		return adService.updateAdTheme(record);
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
	 * 三层广告添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertAdThird", method = RequestMethod.POST)
	public LianjiuResult insertAdThird(HttpServletRequest request, AdThird record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertAdThird(record);
	}
	
	/**
	 *   三层广告删除
	 * @param hotId  三层广告id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteAdThird", method = RequestMethod.POST)
	public LianjiuResult deleteAdThird(String thiId) {
		if(Util.isEmpty(thiId)){
			return LianjiuResult.build(401, "请指定要删除的热门产品Id");
		}
		return adService.deleteAdThird(thiId);
	}
	/**
	 * 更新三层广告内容
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateAdThird", method = RequestMethod.POST)
	public LianjiuResult updateAdThird(AdThird record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateAdThird(record);
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
	
	/**
	 * 精品热卖添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertAdEssenceHot", method = RequestMethod.POST)
	public LianjiuResult insertAdEssenceHot(HttpServletRequest request, AdEssenceHot record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertAdEssenceHot(record);
	}
	/**
	 *   精品热卖删除
	 * @param essId  精品热卖id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteAdEssenceHot", method = RequestMethod.POST)
	public LianjiuResult deleteAdEssenceHot(String essId) {
		if(Util.isEmpty(essId)){
			return LianjiuResult.build(401, "请指定要删除的热门产品Id");
		}
		return adService.deleteAdEssenceHot(essId);
	}
	/**
	 * 更新精品热卖广告内容
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateAdEssenceHot", method = RequestMethod.POST)
	public LianjiuResult updateAdEssenceHot(AdEssenceHot record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateAdEssenceHot(record);
	}
	/**
	 * 按精品热卖id查询
	 * @param essId :精品热卖id
	 * @return
	 *
	 */
	@ResponseBody	
	@RequestMapping(value="selectAdEssenceHotById/{essId}/essId", method = RequestMethod.GET)
	public LianjiuResult selectAdEssenceHotById(@PathVariable String essId) {
		if(Util.isEmpty(essId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectAdEssenceHotById(essId);
	}

	 /** 
	  * 根据页码查询精品热卖list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdEssenceHotList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectAdEssenceHotList(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectAdEssenceHotList(pageNum,pageSize);
	}
	
	/**
	 * 二手精品 品牌墙添加
	 * @param record
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="insertAdEssenceBrand", method = RequestMethod.POST)
	public LianjiuResult insertAdEssenceBrand(HttpServletRequest request, AdEssenceBrand record) {
		HttpSession session = request.getSession();
		session.setAttribute("user", "123456");//测试用户 以后删除
		String user =(String)request.getSession().getAttribute("user");
		record.setAddPerson(user);
		return adService.insertAdEssenceBrand(record);
	}
	/**
	 *   二手精品 品牌墙删除
	 * @param essId  精品热卖id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="deleteAdEssenceBrand", method = RequestMethod.POST)
	public LianjiuResult deleteAdEssenceBrand(String brId) {
		if(Util.isEmpty(brId)){
			return LianjiuResult.build(401, "请指定要删除的热门产品Id");
		}
		return adService.deleteAdEssenceBrand(brId);
	}
	/**
	 * 更新二手精品 品牌墙内容
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="updateAdEssenceBrand", method = RequestMethod.POST)
	public LianjiuResult updateAdEssenceBrand(AdEssenceBrand record) {
		if(null == record){
			return LianjiuResult.build(401, "record对象绑定错误");
		}
		return adService.updateAdEssenceBrand(record);
	}
	/**
	 * 按二手精品 品牌墙id查询
	 * @param essId :精品热卖id
	 * @return
	 *
	 */
	@ResponseBody	
	@RequestMapping(value="selectAdEssenceBrandById/{brId}/brId", method = RequestMethod.GET)
	public LianjiuResult selectAdEssenceBrandById(@PathVariable String brId) {
		if(Util.isEmpty(brId)){
			return LianjiuResult.build(401, "请指定要查询的电器广告Id");
		}
		return adService.selectAdEssenceBrandById(brId);
	}
	 /** 
	  * 根据页码查询二手精品 品牌墙list
	 * @return
	 *
	 */
	@ResponseBody
	@RequestMapping(value="selectAdEssenceBrandList/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public LianjiuResult selectAdEssenceBrandList(@PathVariable int pageNum, @PathVariable int pageSize) {
		if (1 > pageNum) {
			return LianjiuResult.build(401, "请传入大于0的pageNum");
		}
		if (1 > pageSize){
			return LianjiuResult.build(402, "请传入大于0的pageSize");
		}
		return adService.selectAdEssenceBrandList(pageNum,pageSize);
	}
}
