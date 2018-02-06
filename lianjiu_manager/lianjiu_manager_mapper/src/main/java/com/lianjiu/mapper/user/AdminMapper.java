package com.lianjiu.mapper.user;

import java.util.List;

import com.lianjiu.model.Admin;
import com.lianjiu.model.AllianceBusiness;
import com.lianjiu.model.vo.SearchObjecVo;

public interface AdminMapper {

	List<Admin> selectBySearchObjecVo(SearchObjecVo vo);
	
    int deleteByPrimaryKey(String adminId);

    int insert(Admin record);

    int insertSelective(Admin record);
    
    Admin selectByPrimaryKey(String adminId);
    
    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
    
    int updateByPrimaryKeySelective(AllianceBusiness record);

    int updateByPrimaryKey(AllianceBusiness record);
}