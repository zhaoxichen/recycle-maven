<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${ctx }/category/addCategory" method="post">
		parentId:<input name="parentId" type="text" value=""><br>
		name:<input name="name" type="text" value=""><br>
		retrieveType:<input name="retrieveType" type="text" value=""><br>
		templateId:<input name="templateId" type="text" value=""><br>
		<input type="submit" value="添加分类">
	</form>
</body>
</html>