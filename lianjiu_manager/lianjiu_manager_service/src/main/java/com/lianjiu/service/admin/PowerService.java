package com.lianjiu.service.admin;

import java.util.List;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.Power;
import com.lianjiu.model.vo.PowerVo;

public interface PowerService {

	LianjiuResult getPowerAll();

	LianjiuResult getPowerAll(int pageNum, int pageSize);

	LianjiuResult getPowerById(String powerId);

	LianjiuResult addPower(Power power);

	LianjiuResult updatePower(Power power);

	LianjiuResult deletePower(String powerId);

	LianjiuResult distribution(List<PowerVo> list,String [] drList, String roleId);
	
}
