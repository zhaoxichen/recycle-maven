package com.lianjiu.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lianjiu.common.pojo.LianjiuResult;


public interface FileService {

	LianjiuResult uploadFile(MultipartFile uploadFile);

	LianjiuResult uploadFile(List<MultipartFile> uploadFiles);

	LianjiuResult updateFile(MultipartFile uploadFile, String fileName);

	LianjiuResult updatePictureServer(String oldPictureIp, String newPictureIp);
	
	LianjiuResult uploadFiles(List<MultipartFile> uploadFiles);
}
