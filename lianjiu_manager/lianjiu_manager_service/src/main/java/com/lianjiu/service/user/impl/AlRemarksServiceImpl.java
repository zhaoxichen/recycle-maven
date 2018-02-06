package com.lianjiu.service.user.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.common.utils.Util;
import com.lianjiu.model.AllianceBuappRemarks;
import com.lianjiu.model.vo.SearchObjecVo;
import com.lianjiu.rest.mapper.user.AllianceBuappRemarksMapper;
import com.lianjiu.service.user.AlRemarksService;

@Service
public class AlRemarksServiceImpl implements AlRemarksService {
	
	@Autowired
	private AllianceBuappRemarksMapper allianceBuappRemarksMapper;
	/**
	 * 添加加盟商申请表备注
	 */
	@Override
	public LianjiuResult addAllBusinessRemarks(AllianceBuappRemarks allianceBuappRemarks) {
		if (null == allianceBuappRemarks) {
			return LianjiuResult.build(502, "传入的对象为空");
		}
		//存入数据库
		allianceBuappRemarks.setAlBuappRemarksId(IDUtils.genABIDs());
		allianceBuappRemarks.setAlBuappRemarksCreated(new Date());
		int rowsAffected = allianceBuappRemarksMapper.insertSelective(allianceBuappRemarks);
		return LianjiuResult.ok(rowsAffected);
	}
	/**
	 * whd 2017年09月11日 descrption:获取当前的加盟商申请备注信息分页
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public LianjiuResult selectAllBusinessRemarks(String albnessApplicationId, int pageNum, int pageSize) {
		AllianceBuappRemarks allianceBuappRemarks = new AllianceBuappRemarks();
		allianceBuappRemarks.setAlbnessApplicationId(albnessApplicationId);
		SearchObjecVo vo = new SearchObjecVo(allianceBuappRemarks);
		// 设置分页信息
		PageHelper.startPage(pageNum, pageSize);
		// 根据parentid查询节点列表
		List<AllianceBuappRemarks> listRemarks = allianceBuappRemarksMapper.selectBySearchObjecVo(vo);
		// 分页后，实际返回的结果list类型是Page<E>，如果想取出分页信息，需要强制转换为Page<E>，
		Page<AllianceBuappRemarks> listPageRemarks = (Page<AllianceBuappRemarks>) listRemarks;
		System.out.println("总页数：" + listPageRemarks.getPages());
		// 封装成相应数据对象
		LianjiuResult result = new LianjiuResult(listRemarks);
		Util.printModelDetails(result);
		result.setTotal(listPageRemarks.getTotal());
		return result;
	}
	/**
	 * whd 2017年09月11日 descrption:获取当前加盟商的所有申请备注信息
	 * @return
	 */
	@Override
	public LianjiuResult selectAllBusinessRemarksByCid(String albnessApplicationId) {
		/*if (Util.isEmpty(albnessApplicationId)) {
			return LianjiuResult.build(501, "请指定要查询的商品id");
		}
		AllianceBuappRemarks allianceBuappRemarks = allianceBuappRemarksMapper.selectByAapId(albnessApplicationId);
		return LianjiuResult.ok(allianceBuappRemarks);*/
		AllianceBuappRemarks allRemarks = new AllianceBuappRemarks();
		allRemarks.setAlbnessApplicationId(albnessApplicationId);
		SearchObjecVo vo = new SearchObjecVo(allRemarks);
		// 执行查询
		List<AllianceBuappRemarks> remarks = allianceBuappRemarksMapper.selectBySearchObjecVo(vo);
		LianjiuResult result = new LianjiuResult(remarks);
		return result;
	}

}
