package com.lianjiu.upload.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.Util;
import com.lianjiu.upload.service.FileService;

/**
 * 上传图片处理
 * <p>
 * Title: PictureController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @version 1.0
 */
@Controller
@RequestMapping("/")
public class PictureController {

	@Autowired
	private FileService pictureService;

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption:上传图片
	 * 
	 * @param uploadFile
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult pictureUpload(MultipartFile uploadFile) {
		if (null == uploadFile) {
			return LianjiuResult.build(401, "uploadFile对象绑定错误");
		}
		System.out.println(uploadFile);
		return pictureService.uploadFile(uploadFile);
	}

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption:多文件上传
	 * 
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = "/upload/multi", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult springUpload(@RequestParam("file") List<MultipartFile> uploadFiles) {
		if (null == uploadFiles) {
			return LianjiuResult.build(401, "uploadFiles对象绑定错误");
		}
		for (MultipartFile multipartFile : uploadFiles) {
			System.out.println(multipartFile.getOriginalFilename());
		}
		return pictureService.uploadFile(uploadFiles);
	}

	/**
	 * 
	 * zhaoxi 2017年11月17日 descrption:多文件上传,返回对象
	 * 
	 * @param uploadFiles
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/upload/multi/list", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult multiUpload(@RequestParam("file") List<MultipartFile> uploadFiles) {
		if (null == uploadFiles) {
			return LianjiuResult.build(401, "uploadFiles对象绑定错误");
		}
		for (MultipartFile multipartFile : uploadFiles) {
			System.out.println(multipartFile.getOriginalFilename());
		}
		return pictureService.uploadFiles(uploadFiles);
	}

	/**
	 * 
	 * zhaoxi 2017年8月31日 descrption:更新图片
	 * 
	 * @param uploadFile
	 * @return LianjiuResult
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public LianjiuResult pictureUpdate(MultipartFile uploadFile, String fileName) {
		if (null == uploadFile) {
			return LianjiuResult.build(401, "uploadFile对象绑定错误");
		}
		if (Util.isEmpty(fileName)) {
			return LianjiuResult.build(402, "指定要更新的图片url");
		}
		System.out.println(fileName);
		return pictureService.updateFile(uploadFile, fileName);
	}
}
