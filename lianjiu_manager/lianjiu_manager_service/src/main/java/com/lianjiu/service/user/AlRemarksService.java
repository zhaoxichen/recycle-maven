package com.lianjiu.service.user;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.AllianceBuappRemarks;

/**
 * 备注服务层
 * @author whd
 *
 */
public interface AlRemarksService {

	LianjiuResult addAllBusinessRemarks(AllianceBuappRemarks allianceBuappRemarks);

	LianjiuResult selectAllBusinessRemarks(String albnessApplicationId,int pageNum, int pageSize);

	LianjiuResult selectAllBusinessRemarksByCid(String albnessApplicationId);

}
