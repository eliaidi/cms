<%@page import="org.apache.shiro.SecurityUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	String username = SecurityUtils.getSubject().getPrincipal().toString();
	//String username = "aa";
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base>

<title>Main</title>
<script type="text/javascript">
var RootPath = '<%=path%>',CurrUser = '<%=username%>',BasePath = '<%=path%>',ScreenWidth = window.screen.availWidth,ScreenHeight = window.screen.availHeight;
</script>
<script src="//cdn.jsdelivr.net/sockjs/1.0.3/sockjs.min.js"></script>
<script type="text/javascript" src="<%=path %>/static/cms/urls.js"></script>

<script id="microloader" type="text/javascript" src="<%=path %>/static/cms/bootstrap.js"></script>

</head>

<body>
	

</body>
</html>
