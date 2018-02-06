package com.lianjiu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import com.lianjiu.common.utils.FtpUtil;
import com.lianjiu.common.utils.GlobalValueUtil;
import com.lianjiu.common.utils.HttpClientUtil;

/**
 * 测试图片服务器
 * 
 * @author Administrator
 *
 */
public class FTPTest {

	@Test
	public void testFtpClient() throws Exception {
		// 创建一个FtpClient对象
		FTPClient ftpClient = new FTPClient();
		// 创建ftp连接。默认是21端口
		ftpClient.connect("192.168.129.131", 21);
		// 登录ftp服务器，使用用户名和密码
		ftpClient.login("ftpuser", "ftpuser");
		// 上传文件。
		// 读取本地文件
		FileInputStream inputStream = new FileInputStream(
				new File("D:\\Documents\\Pictures\\images\\2010101415583352_S.jpg"));
		// 设置上传的路径
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		// 修改上传文件的格式
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// 第一个参数：服务器端文档名
		// 第二个参数：上传文档的inputStream
		ftpClient.storeFile("hello1.jpg", inputStream);
		// 关闭连接
		ftpClient.logout();

	}

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param basePath
	 *            FTP服务器根目录
	 * @param filePath
	 *            FTP服务器文件存放路径。例如分日期存放：/2017/08/17。文件的路径为basePath+filePath
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	@Test
	public void testFtpUtil() throws Exception {
		// 图片通过ftp服务上传到/home/ftpuser/www/images目录下
		// 创建图片文件流
		FileInputStream inputStream = new FileInputStream(new File("D:\\Users\\imageTest\\19.gif"));
		// 使用工具类上传图片到 nginx图片服务器 重命名为 hello123.gif
		boolean isSuccess = FtpUtil.uploadFile("192.168.0.188", GlobalValueUtil.FTP_PORT, GlobalValueUtil.FTP_USERNAME,
				GlobalValueUtil.FTP_PASSWORD, GlobalValueUtil.FTP_BASE_PATH, "/2017/08/17", "hello18.gif", inputStream);
		System.out.println(isSuccess);
		// 上传成功后，通过 http://192.168.129.131/images/2017/08/17/hello123.gif
		// 可以访问到资源
		// 如果配置了域名，通过 http://image.lianjiu.com/images/2017/08/17/hello123.gif
		// 可以访问到资源
	}

	@Test
	public void capImagePath() {
		String fileName = "http://101.132.38.30/images/2017/08/31/1504157822028843.png";
		int index = fileName.indexOf(GlobalValueUtil.IMAGE_BASE_URL);
		System.out.println(index);
		String imagePath = fileName.substring(GlobalValueUtil.IMAGE_BASE_URL.length(), fileName.lastIndexOf("/"));
		System.out.println(imagePath);
	}

	public static void main(String[] args) {
		try {
			InputStream inputStream = HttpClientUtil
					.doGetForImputStream(GlobalValueUtil.CERTIFICATE_BASE_URL + "anjpapiclient_cert.p12");
			System.out.println(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
