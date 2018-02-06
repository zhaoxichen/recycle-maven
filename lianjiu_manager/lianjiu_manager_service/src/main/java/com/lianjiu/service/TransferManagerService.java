package com.lianjiu.service;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.model.UserWalletRecord;

public interface TransferManagerService {

	LianjiuResult transferOk(String userId,String recordId);

	LianjiuResult getAllTransfer(String state,int pageNum, int pageSize);

	LianjiuResult transferNo(String userId, String recordId, String msg);

	LianjiuResult getTransferById(String userId, String recordId);

	LianjiuResult vagueQuery(UserWalletRecord userWalletRecord,String state, int pageNum, int pageSize, String cratedStart,
			String cratedOver);
}
