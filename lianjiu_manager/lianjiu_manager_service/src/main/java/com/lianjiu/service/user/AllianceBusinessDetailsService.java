package com.lianjiu.service.user;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AllianceBusinessDetails;

public interface AllianceBusinessDetailsService {
	// 更新加盟商信息
	LianjiuResult updateAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails);

	// 根据ID删除加盟商
	LianjiuResult deleteAllianceBusinessDetailsById(String abunesId);

	// 查询所有加盟商，分页
	LianjiuResult selectAllBusinessDetails(int pageNum, int pageSize);

	// 添加加盟商
	LianjiuResult addAllianceBusinessDetails(AllianceBusinessDetails allianceBusinessDetails);

	// 查询所有加盟商
	LianjiuResult getBusinessDetails();

	// 查询当前加盟商的信息
	LianjiuResult getBusinessDetailsById(String abunesId);

	// 根据与父ID查询所有信息
	LianjiuResult getBusinessDetailsByCid(long categoryId, int pageNum, int pageSize);
	
	// 操作，取消开通资格
	LianjiuResult modifyAccept(String abunesId, int type);

	//查钱包明细
	LianjiuResult selectAssetDetails(String abunesId);

	LianjiuResult vagueQuery(AllianceBusinessDetails allianceBusinessDetails, int pageNum, int pageSize,
			String cratedStart, String cratedOver);
}
