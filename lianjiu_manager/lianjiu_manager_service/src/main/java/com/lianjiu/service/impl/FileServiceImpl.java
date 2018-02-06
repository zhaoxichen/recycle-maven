package com.lianjiu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lianjiu.common.pojo.LianjiuResult;
import com.lianjiu.common.utils.FtpUtil;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.IDUtils;
import com.lianjiu.model.vo.Picture;
import com.lianjiu.service.FileService;

/**
 * 图片上传服务
 * <p>
 * Title: PictureServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 */
@Service
public class FileServiceImpl implements FileService {

	/**
	 * 单文件上传
	 */
	@Override
	public LianjiuResult uploadFile(MultipartFile uploadFile) {
		try {
			// 生成一个新的文件名
			// 取原始文件名
			String oldName = uploadFile.getOriginalFilename();
			// 新文件名+原文件的后缀
			String newName = IDUtils.genImageName() + oldName.substring(oldName.lastIndexOf("."));
			// 图片上传
			String imagePath = new DateTime().toString("/yyyy/MM/dd");
			System.out.println(imagePath);
			boolean result = FtpUtil.uploadFile(GlobalValueUtil.FTP_ADDRESS, GlobalValueUtil.FTP_PORT,
					GlobalValueUtil.FTP_USERNAME, GlobalValueUtil.FTP_PASSWORD, GlobalValueUtil.FTP_BASE_PATH,
					imagePath, newName, uploadFile.getInputStream());
			// 返回结果
			if (!result) {
				return LianjiuResult.build(501, "文件上传失败");
			}
			// 上传成功，返回图片的地址
			return LianjiuResult.ok(GlobalValueUtil.IMAGE_BASE_URL + imagePath + "/" + newName);

		} catch (Exception e) {
			return LianjiuResult.build(502, "文件上传发生异常");
		}
	}

	/**
	 * 多文件上传
	 */
	@Override
	public LianjiuResult uploadFile(List<MultipartFile> uploadFiles) {
		Map<String, String> fileMap = new HashMap<String, String>();
		try {
			String oldName;
			String newName;
			// 图片上传的路径
			String imagePath = new DateTime().toString("/yyyy/MM/dd");
			for (MultipartFile uploadFile : uploadFiles) {
				// 生成一个新的文件名
				// 取原始文件名
				oldName = uploadFile.getOriginalFilename();
				// 新文件名+原文件的后缀
				newName = IDUtils.genImageName() + oldName.substring(oldName.lastIndexOf("."));

				boolean result = FtpUtil.uploadFile(GlobalValueUtil.FTP_ADDRESS, GlobalValueUtil.FTP_PORT,
						GlobalValueUtil.FTP_USERNAME, GlobalValueUtil.FTP_PASSWORD, GlobalValueUtil.FTP_BASE_PATH,
						imagePath, newName, uploadFile.getInputStream());
				// 返回结果
				if (!result) {
					fileMap.put(oldName, "文件上传失败");
				}
				// 上传成功，保存图片的地址
				fileMap.put(oldName, GlobalValueUtil.IMAGE_BASE_URL + imagePath + "/" + newName);
			}
			// 上传成功，返回图片的地址集合
			return LianjiuResult.ok(fileMap);

		} catch (Exception e) {
			return LianjiuResult.build(502, "文件上传发生异常");
		}
	}

	/**
	 * 多文件上传
	 */
	@Override
	public LianjiuResult uploadFiles(List<MultipartFile> uploadFiles) {
		List<Picture> pictures = new ArrayList<>();
		try {
			String oldName;
			String newName;
			// 图片上传的路径
			String imagePath = new DateTime().toString("/yyyy/MM/dd");
			for (MultipartFile uploadFile : uploadFiles) {
				// 生成一个新的文件名
				// 取原始文件名
				oldName = uploadFile.getOriginalFilename();
				// 新文件名+原文件的后缀
				newName = IDUtils.genImageName() + oldName.substring(oldName.lastIndexOf("."));

				boolean result = FtpUtil.uploadFile(GlobalValueUtil.FTP_ADDRESS, GlobalValueUtil.FTP_PORT,
						GlobalValueUtil.FTP_USERNAME, GlobalValueUtil.FTP_PASSWORD, GlobalValueUtil.FTP_BASE_PATH,
						imagePath, newName, uploadFile.getInputStream());
				// 返回结果
				if (!result) {
					pictures.add(new Picture(oldName, "文件上传失败"));
				}
				// 上传成功，保存图片的地址
				pictures.add(new Picture(oldName, GlobalValueUtil.IMAGE_BASE_URL + imagePath + "/" + newName));
			}
			// 上传成功，返回图片的地址集合
			return LianjiuResult.ok(pictures);

		} catch (Exception e) {
			return LianjiuResult.build(502, "文件上传发生异常");
		}
	}

	/**
	 * 更新
	 */
	@Override
	public LianjiuResult updateFile(MultipartFile uploadFile, String fileName) {
		try {
			// 取出后缀
			String suffixStr = fileName.substring(fileName.lastIndexOf("."));
			// 取原始文件名
			String oldName = uploadFile.getOriginalFilename();
			if (!oldName.endsWith(suffixStr)) {
				return LianjiuResult.build(505, "请保持与原文件格式一致，原文件后缀为:" + suffixStr);
			}
			// 截取文件名
			int nameIndex = fileName.lastIndexOf("/");
			if (-1 == nameIndex) {
				return LianjiuResult.build(504, "文件名不正确");
			}
			String newName = fileName.substring(nameIndex + 1);
			System.out.println(newName);
			// 截取文件目录
			int pathIndex = fileName.indexOf(GlobalValueUtil.IMAGE_BASE_URL);
			if (0 != pathIndex) {
				return LianjiuResult.build(503, "文件根目录不正确");
			}
			String imagePath = fileName.substring(GlobalValueUtil.IMAGE_BASE_URL.length(), nameIndex);
			System.out.println(imagePath);
			// 图片上传
			boolean result = FtpUtil.uploadFile(GlobalValueUtil.FTP_ADDRESS, GlobalValueUtil.FTP_PORT,
					GlobalValueUtil.FTP_USERNAME, GlobalValueUtil.FTP_PASSWORD, GlobalValueUtil.FTP_BASE_PATH,
					imagePath, newName, uploadFile.getInputStream());
			// 返回结果
			if (!result) {
				return LianjiuResult.build(501, "文件更新失败");
			}
			// 上传成功，返回图片的地址
			return LianjiuResult.ok(GlobalValueUtil.IMAGE_BASE_URL + imagePath + "/" + newName);

		} catch (Exception e) {
			return LianjiuResult.build(502, "文件更新发生异常");
		}
	}

	/**
	 * 图片服务器迁移
	 */
	@Override
	public LianjiuResult updatePictureServer(String oldPictureIp, String newPictureIp) {
		return null;
	}

}
