package com.lianjiu.rest.mapper.user;

import java.util.List;

import com.lianjiu.model.Admin;
import com.lianjiu.model.vo.SearchObjecVo;

public interface AdminMapper {

	List<Admin> selectBySearchObjecVo(SearchObjecVo vo);
	
    
    Admin selectByPrimaryKey(String adminId);
    
}