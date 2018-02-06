package com.lianjiu.sso.service;


import com.lianjiu.common.pojo.LianjiuResult;

public interface AllianceUserService {
	LianjiuResult getAllianceUserByToken(String token);

	LianjiuResult checkAlliancePhone(String phone,String code);

	LianjiuResult updateAlliancePhone(String allianceId, String phone, String code);

	LianjiuResult updateAlliancePassword(String allinaceId, String password, String code);

	LianjiuResult allianceLogin(String username, String password);

	LianjiuResult allianceUserLogin(String usernamae, String password, String token);

	LianjiuResult sendSms(String allianceId, String phone);

	LianjiuResult sendSmsAlliance(String allianceId,String phone);

	LianjiuResult Logout(String allianceId);

	LianjiuResult sendSMSPwd(String allianceId);

}
