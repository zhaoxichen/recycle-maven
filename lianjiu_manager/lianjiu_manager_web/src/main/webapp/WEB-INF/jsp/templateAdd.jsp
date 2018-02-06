<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/init.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript"
	src="js/jquery-easyui-1.4.1/jquery.min.js"></script>
<title>参数模版添加</title>
<script type="text/javascript">
	function addInputItem() {
		$("#submit_before").before("<input>");
		$("#submit_before").before("<input>");

		$("#submit_before").before("<input onclick='appendToJson()' type='button' value='确定'>");
		$("#submit_before").before("<br>");
	}
	function appendToJson() {
		str = $(this).prev()
		$("#json_edit").val(str);
	}
</script>
</head>
<body>
	<div>
		<form id="param_edit" action="${ctx }/param/addTemplate" method="post">
			模版名称:<input name="name" type="text">
			json串: <input id="json_edit" name="paramTemplateData" type="text" value="">
			<br> <input onclick="addInputItem()" type="button" value="增加参数"><br>
			<input id="submit_before" type="submit" value="提交">
		</form>
	</div>
</body>
</html>