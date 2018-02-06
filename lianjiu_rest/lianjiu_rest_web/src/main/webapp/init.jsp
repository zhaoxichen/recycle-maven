<%@page import="java.net.InetAddress"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
%>
<%
String LocalPath=request.getLocalAddr();
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String serverPath=request.getServerName();
request.setAttribute("rootPath",basePath);
String ip=InetAddress.getLocalHost().getHostAddress();
%>
<c:set value="${localPath}/upload" var="path"/>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<c:set value="http://192.168.0.121:8081" var="MANAGER_BASE_URL"/>
<c:set value="http://192.168.0.121:8082" var="PORTAL_BASE_URL"/>
<c:set value="http://192.168.0.129:8083" var="SSO_BASE_URL"/>
<c:set value="http://192.168.0.121:8084" var="ORDER_BASE_URL"/>
<c:set value="http://192.168.0.121:8085" var="REST_BASE_URL"/>
<script type="text/javascript">
	var rootPath="<%=basePath%>";
	var localPath="<%=LocalPath%>";
	var path="/upload";
</script>
