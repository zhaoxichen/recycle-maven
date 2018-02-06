package com.lianjiu.sso.service;

import com.lianjiu.common.pojo.LianjiuResult;

public interface UserService {

	LianjiuResult checkData(String content, String callback);

	LianjiuResult getUserByToken(String token);

	LianjiuResult getSmsByToken(String token);

	LianjiuResult userLogin(String username, String userPassword, String udetailsEquipment);

	LianjiuResult createUser(String username, String userPassword);

	LianjiuResult getCodeByToken(String token);

	LianjiuResult sendSms(String phone);

	LianjiuResult modifyPwd(String userId, String userPassword);

	LianjiuResult updatePwd(String userPassword, String code, String phone);

	LianjiuResult sendSmsPhone(String userId, String phone);

	LianjiuResult phoneCheck(String phone, String code);

	LianjiuResult changePwd(String phone, String code, String userPassword);

	LianjiuResult phoneChange(String userId, String phone, String code);

	LianjiuResult isPwd(String username);

	LianjiuResult sendNewPhone(String userId, String phone);

	LianjiuResult OtherLogin(String num, String nickname, String picture, int type);

	LianjiuResult bindingPhone(String userId, String phone, String code);

	LianjiuResult sendSmsByBinding(String phone);

	LianjiuResult getUserMessage(String userId);

	LianjiuResult QWLogin(String openId, Integer type, String udetailsEquipment);

	LianjiuResult CreateUser(String openId, String userPhone, String code, String nickname, String picture,
			Integer type, String udetailsEquipment);

	LianjiuResult loginOut(String userId);

	LianjiuResult sMsLogin(String phone, String uCode, String udetailsEquipment, String udetailsReferrer);

	LianjiuResult sendSMSPwd(String userId);

	LianjiuResult sendSmsLogin(String phone);

	LianjiuResult forgetPwdSms(String phone);
}
