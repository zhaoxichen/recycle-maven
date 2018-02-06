<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/init.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- form+iframe的方式。空的iframe并且不显示，将form的target设置为iframe，就会提交到iframe上，从而实现不刷新页面上传。 -->
	<form name="uploadForm" id="uploadForm" method="post"
		enctype="multipart/form-data"
		action="https://upload.lianjiuhuishou.com/picture/upload">
		<p style="margin: 10px 0;">
			上传图片(正式环境的): &nbsp;&nbsp; <input type="file" name="uploadFile"
				ID="fupPhoto" /> <input type="submit" id="fileSubmit" name="Submit"
				value="上传" />
		</p>
	</form>
	<!--测试环境的  -->
	<form name="uploadFormTest" id="uploadFormTest" method="post"
		enctype="multipart/form-data"
		action="http://101.132.38.30:8081/picture/upload">
		<p style="margin: 10px 0;">
			上传图片(测试环境的): &nbsp;&nbsp; <input type="file" name="uploadFileTest"
				ID="fupPhotoTest" /> <input type="submit" id="fileSubmitTest"
				name="Submit" value="上传" />
		</p>
	</form>
	<!--正式环境的 ,独立实例 -->
	<form name="uploadFormStand" id="uploadFormStand" method="post"
		enctype="multipart/form-data"
		action="https://upload.lianjiuhuishou.com/pic/upload">
		<p style="margin: 10px 0;">
			上传图片(正式环境,独立实例): &nbsp;&nbsp; <input type="file"
				name="uploadFileStand" ID="fupPhotoStand" /> <input type="submit"
				id="fileSubmitStand" name="Submit" value="上传" />
		</p>
	</form>
	<!--测试环境的 ,独立实例 -->
	<form name="uploadFormStand1" id="uploadFormStand1" method="post"
		enctype="multipart/form-data"
		action="http://101.132.38.30:8081/pic/upload">
		<p style="margin: 10px 0;">
			上传图片(测试环境,独立实例): &nbsp;&nbsp; <input type="file"
				name="uploadFileStand1" ID="fupPhotoStand1" /> <input type="submit"
				id="fileSubmitStand1" name="Submit" value="上传" />
		</p>
	</form>
</body>
</html>